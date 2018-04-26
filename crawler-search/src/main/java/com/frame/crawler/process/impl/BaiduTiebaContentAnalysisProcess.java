package com.frame.crawler.process.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.regex.Pattern;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.frame.crawler.constants.GobalConstant;
import com.frame.crawler.model.BaiduTiebaContent;
import com.frame.crawler.model.BaiduTiebaImage;
import com.frame.crawler.model.SearchKeyword;
import com.frame.crawler.process.interfaces.AbstractAnalysisProcess;
import com.frame.crawler.service.BaiduTiebaContentService;
import com.frame.crawler.service.BaiduTiebaImageService;
import com.frame.crawler.util.ThreadPoolUtils;
import com.frame.crawler.util.UUIDUtils;

/**
 * 百度贴吧具体内容爬取逻辑
 * Created by zhh on 2018/04/13.
 */
@Service
public class BaiduTiebaContentAnalysisProcess extends AbstractAnalysisProcess {
	
	@Autowired
	private BaiduTiebaContentService baiduTiebaContentService;
	
	@Autowired
	private BaiduTiebaImageService baiduTiebaImageService;
	
	@Override
	public Document getDocument(String url) {
		return super.getDocumentByURL(url);
	}
	
	/**
	 * 执行抓取操作
	 * @param url 网页url地址
	 * @param id 关联searchUuid
	 * @throws Exception
	 */
	public void executeGrab(String url, String id) throws Exception {
		Document pageDoc = getDocument(url);
		analysisDocument(pageDoc, id);
	}

	/**
	 * 分析返回页面结构
	 * @param pageDoc 页面结构
	 * @param searchUuid 关联id
	 * @throws Exception
	 */
	public void analysisDocument(Document pageDoc, String searchUuid) throws Exception {
		// 获取请求url, 例：https://tieba.baidu.com/p/5645078496?pn=1
		String baseUri = pageDoc.baseUri();
		// 参数串
		String paramStr = baseUri.substring(baseUri.indexOf("?") + 1);
		// 页面信息获取
		Integer pageNum = Integer.valueOf(paramStr.split("=")[1]);
		
		// 贴吧具体内容
		Elements contentElements = pageDoc.getElementsByClass("l_post l_post_bright j_l_post clearfix  ");
		
		List<BaiduTiebaContent> baiduTiebaContents = new ArrayList<>();
		BaiduTiebaContent baiduTiebaContent;
		
		// 顺序号
		int sort = 0;
		Integer replyUserId = null;
		String replyUserIdStr;
		String replyUser = "";
		String replyContent = "";
		String replyDate = "";
		Date replyDateParse = null;
		for (Element contentElement : contentElements) {
			
			Elements adElements = contentElement.getElementsByClass("now_date");
			if (adElements.size() != 0) {
				// 这个是广告帖子信息, 忽略此项
				continue;
			}
			Elements childContentElements;
			try {
				// 获取回帖用户Id
				Elements nameElements = contentElement.getElementsByClass("d_name");
				replyUserIdStr = nameElements.attr("data-field");
				replyUserId = getUserId(replyUserIdStr);
				
				// 获取回帖用户
				Elements authorElements = contentElement.getElementsByClass("p_author_name j_user_card");
				replyUser = authorElements.text();
				
				// 获取回帖信息
				childContentElements = contentElement.getElementsByClass("d_post_content j_d_post_content ");
				replyContent = childContentElements.text();
				replyContent = removeNonBmpUnicode(replyContent);
				
				// 获取回帖时间
				Elements postTailWraps = contentElement.getElementsByClass("post-tail-wrap");
				replyDate = postTailWraps.get(0).children().last().text();
				// 处理时间
				replyDateParse = "".equals(replyDate) ? new Date() : new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(replyDate);
			} catch (Exception e) {
				// 内容解析发生异常不做处理, 跳过继续执行
				continue;
			}
			
			sort++;
			String contentUuid = UUIDUtils.getId();
			baiduTiebaContent = new BaiduTiebaContent();
			baiduTiebaContent.setReplyUserId(replyUserId);
			baiduTiebaContent.setReplyUser(replyUser);
			baiduTiebaContent.setReplyContent(replyContent);
			baiduTiebaContent.setReplyDate(replyDateParse);
			baiduTiebaContent.setSearchUuid(searchUuid);
			baiduTiebaContent.setPageNum(pageNum);
			baiduTiebaContent.setSort(sort);
			baiduTiebaContent.setImportDate(new Date());
			baiduTiebaContent.setUuid(contentUuid);
			baiduTiebaContent.setFlag(GobalConstant.ReadFlag.NOT_READ);
			
//			baiduTiebaContentService.insert(baiduTiebaContent);
			baiduTiebaContents.add(baiduTiebaContent);
			logger.info(">>> 存储baiduTiebaContent信息: {} >>>", baiduTiebaContent);
			
			// 图片顺序号
			int imgSort = 0;
			List<BaiduTiebaImage> baiduTiebaImages = new ArrayList<>();
			BaiduTiebaImage baiduTiebaImage;
			// 获取回帖中的图片信息
			Elements imageElements = childContentElements.get(0).getElementsByTag("img");
			for (Element imageElement : imageElements) {
				
				imgSort++;
				// 存储结果
				baiduTiebaImage = new BaiduTiebaImage();
				// 设置存储信息
				baiduTiebaImage.setOriginalUrl(imageElement.attr("src"));
				baiduTiebaImage.setRootPath("");
				baiduTiebaImage.setTargetUrl("");
				baiduTiebaImage.setContentUuid(contentUuid);
				baiduTiebaImage.setSort(imgSort);
				baiduTiebaImage.setImportDate(new Date());
				baiduTiebaImage.setUuid(contentUuid);
				baiduTiebaImage.setFlag(GobalConstant.ReadFlag.NOT_READ);
				
				baiduTiebaImages.add(baiduTiebaImage);
				logger.info(">>> 存储baiduTiebaImage信息: {} >>>", baiduTiebaImage);
			}
			
			// 存储信息
			if (baiduTiebaImages.size() != 0) {
				baiduTiebaImageService.save(baiduTiebaImages);
			}
		}
		
		// 存储信息
		if (baiduTiebaContents.size() != 0) {
			baiduTiebaContentService.save(baiduTiebaContents);
		}
	}
	
	/**
	 * 多线程处理爬取贴吧内容请求
	 * @param contentParams 获取贴吧内容的参数条件(帖子链接, 帖子ID)
	 * @return
	 * @throws Exception
	 */
	public void threadExecuteGrab(Map<String, String> contentParams) throws Exception {
		
		if (CollectionUtils.isEmpty(contentParams)) {
			// 条件集合为空, 不执行以下操作
			return;
		}
		
		/*========================== 线程执行操作 ==============================*/
		// 创建线程池
		ThreadPoolExecutor threadPoolExecutor = ThreadPoolUtils.getThreadPoolInstance();
		
		// 遍历请求内容
		contentParams.forEach((contentUrl, uuid) -> {
			// 读取页面数
			Integer pageNum = -1;
			try {
				pageNum = getPageNum(contentUrl);
				for (int i = 1; i <= pageNum; i++) {
					threadPoolExecutor.execute(new BaiduTiebaContentGrabSearcherRunnable().setId(uuid).setReqUrl(contentUrl).setPageNum(i));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		/*========================== 线程执行操作 ==============================*/
	}
	
	@Override
	public boolean threadExecuteGrab(List<SearchKeyword> searchKeywordList) throws Exception {
		return true;
	}

	@Override
	protected void analysisDocument(Document pageDoc) throws Exception {
	}

	@Override
	protected int getAnalysisPageNum(Document pageDoc) throws Exception {
		
		int pageNum = -1;
		
		// 获取页面信息 div 层
		Elements pageElements = pageDoc.getElementsByClass("l_pager pager_theme_5 pb_list_pager");
		if (pageElements == null || pageElements.size() == 0) {
			return pageNum;
		}
		
		Elements elements = pageElements.get(0).children();
		int size = elements.size();
		
		if (size == 0) {
			pageNum = 1;
		} else {
			// 至少两页数据项, 获取页面 div 层内子元素数量(包括了“下一页”、“尾页”, 需不计算在内) 
			pageNum = size - 2;
		}
		
		logger.info(">>> 所请求页面的页面数pageNum为: {} >>>", pageNum);
		return pageNum;
	}

	/**
	 * 获取用户id
	 * @param userIdStr 用户ID字符串
	 * @return
	 */
	private int getUserId(String userIdStr) {
		// userIdStr格式: {"user_id":96750238}
		if (StringUtils.isEmpty(userIdStr)) {
			return 0;
		}
		// 正则提取字符串中id数字
		userIdStr = Pattern.compile("[^0-9]").matcher(userIdStr).replaceAll("");
		// 返回结果
		return StringUtils.isEmpty(userIdStr) ? 0 : Integer.valueOf(userIdStr);
	}
	
	// 任务线程
	class BaiduTiebaContentGrabSearcherRunnable implements Runnable {
		
		private String id;
		
		private String reqUrl;
		
		private Integer pageNum;
		
		public String getReqUrl() {
			return reqUrl;
		}

		public BaiduTiebaContentGrabSearcherRunnable setReqUrl(String reqUrl) {
			this.reqUrl = reqUrl;
			return this;
		}
		
		public String getId() {
			return id;
		}

		public BaiduTiebaContentGrabSearcherRunnable setId(String id) {
			this.id = id;
			return this;
		}
		
		public Integer getPageNum() {
			return pageNum;
		}

		public BaiduTiebaContentGrabSearcherRunnable setPageNum(Integer pageNum) {
			this.pageNum = pageNum;
			return this;
		}

		@Override
		public void run() {
			System.out.println(">>> " + reqUrl + " >>>");
			try {
				// 抓取数据
				String contentUrl = reqUrl + String.format("?pn=%d", pageNum);
				executeGrab(contentUrl, id);
			} catch (Exception e) {
				logger.debug(">>> 当前线程出现异常, 请求url地址为: {} >>>", reqUrl);
				e.printStackTrace();
			}
		}
	}
	
}
