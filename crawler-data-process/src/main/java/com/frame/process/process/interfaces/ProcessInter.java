package com.frame.process.process.interfaces;

/**
 * 核心处理类的接口
 * Created by zhh on 2018/04/19.
 */
public interface ProcessInter {
	
	/**
	 * 数据到文件转换器
	 * @param tableType 表类别
	 * @return
	 */
	String datasToFilesConverter(String tableType);
	
	/**
	 * 文件到数据转换器
	 * @param filePath 待解析Excel文件路径
	 */
	void filesToDatasConverter(String filePath);
	
	/**
	 * 文件到数据转换器(开多线程)
	 * @param filePath 文件夹路径
	 */
	void filesToDatasConverterThreadOption(String filePath);
}
