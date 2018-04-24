package com.frame.process.schedule;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.frame.process.constants.GobalConstant;
import com.frame.process.process.impl.ExcelProcess;
import com.frame.process.process.interfaces.CommonProcess;
import com.frame.process.utils.FileUtils;

/**
 * 定时任务调度器
 * Created by zhh on 2018/04/24.
 */
@Configuration
@EnableScheduling
public class TaskScheduler {
	
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	// 文件打包存放位置
	@Value("${commonProcess.fileLocation}")
	private String fileLocation;
	
	// 文件解析读取位置
	@Value("${commonProcess.zipLocation}")
	private String zipLocation;
	
	@Autowired
	private ExcelProcess excelProcess;
	
	/**
	 * 数据转文件定时调度器
	 */
	@Scheduled(cron = "0/20 * * * * ?")
	public void datas2FilesScheduler() {
		// 获取当前扫描中文件
//		List<String> childFilePaths = new ArrayList<>();
//		FileUtils.getAllFilePath(fileLocation, childFilePaths);
		//
		List<String> typeNames = CommonProcess.getInitialCategoriesKeys();
		excelProcess.datasToFilesConverterThreadOption(typeNames);
	}
	
	/**
	 * 文件转数据定时调度器
	 */
	@Scheduled(cron = "0/30 * * * * ?")
	public void files2DatasScheduler() {
		// 获取当前扫描中文件
		List<String> childFilePaths = new ArrayList<>();
		FileUtils.getAllFilePath(zipLocation, childFilePaths);
		//
		List<String> childZipFilePaths = new ArrayList<>();
		for (String childFilePath : childFilePaths) {
			if(childFilePath.endsWith(GobalConstant.FileType.ZIP)) {
				childZipFilePaths.add(childFilePath);
			}
		}
		
		for (String childZipFilePath : childZipFilePaths) {
			try {
				excelProcess.filesToDatasConverterThreadOption(childZipFilePath);
			} catch (Exception e) {
				// 异常继续执行
				logger.info("=== 当前文件出现异常不进行处理, 文件名：{} ===", childZipFilePath);
				continue;
			}
		}
	}
}
