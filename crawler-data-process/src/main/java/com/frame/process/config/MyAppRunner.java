package com.frame.process.config;

import java.io.File;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.frame.process.constants.GobalConstant;
import com.frame.process.process.impl.ExcelProcess;
import com.frame.process.process.interfaces.CommonProcess;

/**
 * 
 * Created by zhh on 2018/04/21.
 */
@Component
public class MyAppRunner implements CommandLineRunner {
	
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	// 文件打包存放、解析读取的位置
	@Value("${commonProcess.fileLocation}")
	private String fileLocation;
	
	@Autowired
	private ExcelProcess excelProcess;
	
	@Override
	public void run(String... arg0) throws Exception {
		// 创建文件存放、解析读取的位置
		new File(fileLocation).mkdir();
		
		List<String> typeNames = CommonProcess.getInitialCategoriesKeys();
		
		String typeName = GobalConstant.ExcelTableAlias.TIE_BA_CONTENT;
//		excelProcess.datasToFilesConverter(typeName);
		String filePath = "C:/Users/Administrator/Desktop/zip/File_20180423173003328292.zip";
		excelProcess.filesToDatasConverterThreadOption(filePath);
	}
	
}
