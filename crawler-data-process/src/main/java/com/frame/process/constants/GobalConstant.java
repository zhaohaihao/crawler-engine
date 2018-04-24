package com.frame.process.constants;
/**
 * 全局静态变量
 * Created by zhh on 2018/04/20.
 */
public interface GobalConstant {
	
	/** 加密标志 **/
	String ENCRYPT_FLAG = "Encrypt_";
	
	/** 导出Excel表别名(用于实体类) **/
	interface ExcelTableAlias {
		
		String WEB_PAGE = "WebPage";
		
		String TIE_BA_SEARCH = "TieBaSearch";
		
		String TIE_BA_CONTENT = "TieBaContent";
		
		String TIE_BA_IMAGE = "TieBaImage";
	}
	
	/** 查询条件 **/
	interface QueryCondition {
		// 查询限制条数
		Integer QUERY_LIMIT = 10000;
	}
	
	/** 文件目录 **/
	interface FilePath {
		// 文件目录
		String FILES = "files";
		// 图片目录
		String IMAGES = "images";
	}
	
	/** 文件后缀类型 **/
	interface FileType {
		//
		String TXT = ".txt";
		//
		String CSV = ".csv";
		//
		String XLS = ".xls";
		//
		String XLSX = ".xlsx";
		//
		String ZIP = ".zip";
	}
	
	/** 数据读取标志 **/
	interface ReadFlag {
		// 未读
		Integer NOT_READ = 0;
		// 已读
		Integer READ = 1;
	}
}
