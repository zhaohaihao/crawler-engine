package com.frame.process.config;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.frame.process.process.impl.ExcelProcess;
import com.frame.process.utils.FileUtils;
import com.frame.process.utils.ThreadPoolUtils;

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
	protected String zipLocation;
	
	@Autowired
	private ExcelProcess excelProcess;
	
	@Override
	public void run(String... arg0) throws Exception {
		// 创建文件存放的位置
		new File(fileLocation).mkdir();
		// 创建文件解析读取位置
		new File(zipLocation).mkdir();
		
		
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
	
}
