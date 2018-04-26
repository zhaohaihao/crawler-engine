package com.frame.process.config;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadPoolExecutor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.frame.process.constants.GobalConstant;
import com.frame.process.process.impl.ExcelProcess;
import com.frame.process.utils.FileUtils;
import com.frame.process.utils.ThreadPoolUtils;
import com.frame.process.utils.Zip4jUtils;
import com.frame.process.utils.ZipUtils;

/**
 * 
 * Created by zhh on 2018/04/21.
 */
@Component
public class MyAppRunner implements CommandLineRunner {
	
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	// 文件打包存放位置
	@Value("${commonProcess.fileLocation}")
	private String fileLocation;
	
	// 文件解析读取位置
	@Value("${commonProcess.zipLocation}")
	private String zipLocation;
	
	// 文件加解密密码
	@Value("${file.secretKey}")
	private String secretKey;
	
	@Value("${server.receiver}")
	private boolean receiver;
	
	@Value("${server.sender}")
	private boolean sender;
	
	@Autowired
	private ExcelProcess excelProcess;
	
	@Override
	public void run(String... arg0) throws Exception {
		// 创建文件存放的位置
		new File(fileLocation).mkdir();
		// 创建文件解析读取位置
		new File(zipLocation).mkdir();
		
		if (sender) {
			// 启动时, 读取上次未处理完的遗留文件
			List<String> childFilePaths = new ArrayList<>();
			FileUtils.getAllFilePath(fileLocation, childFilePaths);
			
			ThreadPoolExecutor threadPoolExecutor = ThreadPoolUtils.getThreadPoolInstance();
			for (String childFilePath : childFilePaths) {
				System.out.println("==== 开始处理上次未处理完的遗留文件： " + childFilePath + " ===");
				// 开线程通知打包方法
				threadPoolExecutor.execute(new Runnable() {
					@Override
					public void run() {
						excelProcess.notifyPackFile(childFilePath);
					}
				});
			}
		}
		
		if (receiver) {
			// 启动时, 读取上次未处理完的遗留文件
			List<String> childZipPaths = new ArrayList<>();
			FileUtils.getAllFilePath(zipLocation, childZipPaths);
			
			Set<String> deleteFolders = new HashSet<>();
			for (String childZipPath : childZipPaths) {
				System.out.println("==== 开始处理上次未处理完的遗留压缩文件或者文件夹： " + childZipPath + " ===");
				if (!childZipPath.endsWith(GobalConstant.FileType.ZIP)) {
					// 文件
					excelProcess.filesToDatasConverter(childZipPath);
					int pos = childZipPath.lastIndexOf("/");
					if (pos != -1) {
						// 获取父文件夹路径
						String folder = childZipPath.substring(0, pos);
						deleteFolders.add(folder);
					}
				} else if (childZipPath.contains(GobalConstant.ENCRYPT_FLAG) && childZipPath.endsWith(GobalConstant.FileType.ZIP)) {
					// 加密压缩包
					String dstPath = childZipPath.replace(GobalConstant.ENCRYPT_FLAG, "");
					int pos = dstPath.lastIndexOf("/");
					Zip4jUtils.decrypt(childZipPath, dstPath.substring(0, pos), secretKey);
					FileUtils.deleteFolder(childZipPath);
					String unCompressFilePath = ZipUtils.unCompress(dstPath);
					excelProcess.filesToDatasConverterThreadOption(unCompressFilePath);
				} else if (childZipPath.endsWith(GobalConstant.FileType.ZIP)) {
					// 未加密压缩包
					String unCompressFilePath = ZipUtils.unCompress(childZipPath);
					excelProcess.filesToDatasConverterThreadOption(unCompressFilePath);
				}
			}
			
			deleteFolders.forEach(filePath -> {
				FileUtils.deleteFolder(filePath);
			});
		}
		
	}
	
}
