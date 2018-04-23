package com.frame.crawler.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.frame.crawler.constants.GobalConstant;
import com.frame.crawler.core.AbstractService;
import com.frame.crawler.mapper.BaiduTiebaImageMapper;
import com.frame.crawler.model.BaiduTiebaImage;
import com.frame.crawler.service.BaiduTiebaImageService;

/**
 * 
 * Created by zhh on 2018/04/13.
 */
@Service
public class BaiduTiebaImageServiceImpl extends AbstractService<BaiduTiebaImage> implements BaiduTiebaImageService {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	// 文件存储根目录
	@Value("${download.imagesSaveRoot}")
	private String IMAGES_SAVE_ROOT;
	
	@Autowired
	private BaiduTiebaImageMapper baiduTiebaImageMapper;
	
	@Override
	public List<BaiduTiebaImage> getNotDownloadImagesByLimit(Integer limit) {
		return baiduTiebaImageMapper.selectNotDownloadImagesByLimit(limit);
	}

	@Override
	public void downloadImagesToLocal(List<BaiduTiebaImage> baiduTiebaImages) {
		
		if (CollectionUtils.isEmpty(baiduTiebaImages)) {
			// 没有图片需要下载, 不执行后续操作
			return;
		}
		
		/*========================== 线程执行操作 ==============================*/
		// 线程核心池大小
		int corePoolSize = 10;
		// 线程池最大线程数
		int maximumPoolSize = 20;
		// 空闲线程生存时间
		long keepAliveTime = 30L;
		// 创建线程池
		ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
		// 允许核心线程超时退出
		threadPoolExecutor.allowCoreThreadTimeOut(true);
		
		for (BaiduTiebaImage baiduTiebaImage : baiduTiebaImages) {
			
			// 多线程下载图片
			threadPoolExecutor.execute(new Runnable() {
				
				@Override
				public void run() {
					try {
						// 网络图片路径
						String originalUrl = baiduTiebaImage.getOriginalUrl();
						String targetUrl = downloadImage(originalUrl);
						baiduTiebaImage.setRootPath(IMAGES_SAVE_ROOT);
						baiduTiebaImage.setTargetUrl(targetUrl);
					} catch (IOException e) {
						// 异常不捕获, 跳过继续执行其他图片的下载任务
						baiduTiebaImage.setTargetUrl("图片下载异常, 请检查URL链接是否正确!");
					}
					// 更新当前图片信息
					update(baiduTiebaImage);
				}
			});
		}
		
		/*========================== 线程执行操作 ==============================*/
		
	}
	
	/**
	 * 图片下载
	 * @param imageUrl 图片网络 url
	 * @return
	 * @throws IOException
	 */
	private String downloadImage(String imageUrl) throws IOException {
		
		if (StringUtils.isEmpty(imageUrl)) {
			// 图片路径不存在时, 不执行下述操作
			return "";
		}
		
		logger.info(">>> 开始下载图片, 当前图片url: {} >>>", imageUrl);
		
		String imageName = imageNameFromUrl(imageUrl);
		if (StringUtils.isEmpty(imageName)) {
			return "表情包图片, 忽略!";
		}
		
		String folderName = createFolder(GobalConstant.FilePath.IMAGES);
		// 图片本地的相对路径
		String relativePath = folderName + imageName;
		// 存储的文件的路径
		String outputFilename = IMAGES_SAVE_ROOT + relativePath;
		
		URL url = new URL(imageUrl);
		// 打开网络输入流
		DataInputStream dataInputStream = new DataInputStream(url.openStream());
		
		// 创建新的输出的文件夹
		FileOutputStream fileOutputStream = new FileOutputStream(new File(outputFilename));
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        byte[] buffer = new byte[1024];
        int length;
        // 数据填充
        while ((length = dataInputStream.read(buffer)) > 0) {
        	byteArrayOutputStream.write(buffer, 0, length);
        }
        fileOutputStream.write(byteArrayOutputStream.toByteArray());
        
        byteArrayOutputStream.close();
        dataInputStream.close();
        fileOutputStream.close();
        
        return relativePath;
	}
	
	/**
	 * 从url链接中获取图片名称
	 * @param imageUrl 图片url链接
	 * @return
	 */
	private String imageNameFromUrl(String imageUrl) {
		/*
		 * 目前格式:
		 * 	1. https://tb2.bdstatic.com/tb/editor/images/face/i_f02.png?t=20140803
		 *  2. https://gsp0.baidu.com/5aAHeD3nKhI2p27j8IqW0jdnxx1xbK/tb/editor/images/jd/j_0037.gif
		 */
		if (StringUtils.isEmpty(imageUrl)) {
			return null;
		}
		
		if (imageUrl.contains("/tb/editor/images")) {
			// 图片为百度贴吧默认表情包, 不爬取
			return "";
		}
		
		// 起始和结束截取位
		int startPos = imageUrl.lastIndexOf("/") + 1;
		int endPos = imageUrl.indexOf("?");
		
		if (endPos == -1) {
			return imageUrl.substring(startPos);
		} else {
//			return imageUrl.substring(startPos, endPos);
			// 图片为百度贴吧默认表情包, 不爬取
			return "";
		}
	}
	
	/**
	 * 创建文件夹 按照一定格式
	 * @param 文件分类
	 * @return
	 */
	private String createFolder(String type) {
		// 获取当前系统时间的 年/月/日
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		// example: /images/2017/11/10/
		String filePath = GobalConstant.Symbol.FORWARD_SLASH + type
				+ GobalConstant.Symbol.FORWARD_SLASH + year
				+ GobalConstant.Symbol.FORWARD_SLASH + month 
				+ GobalConstant.Symbol.FORWARD_SLASH + day
				+ GobalConstant.Symbol.FORWARD_SLASH;
		File file = new File(IMAGES_SAVE_ROOT + filePath);
		if (!file.exists()) {
			file.mkdirs();
		}
		return filePath;
	}
}
