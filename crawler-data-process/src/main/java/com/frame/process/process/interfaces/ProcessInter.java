package com.frame.process.process.interfaces;

/**
 * 核心处理类的接口
 * Created by zhh on 2018/04/19.
 */
public interface ProcessInter {
	
	/**
	 * 数据到文件转换器
	 * @param tableType 表类别
	 */
	void datasToFilesConverter(String tableType);
	
	/**
	 * 文件到数据转换器
	 * @param filePaths 待解析文件路径列表
	 */
	void filesToDatasConverter(String filePath);
}
