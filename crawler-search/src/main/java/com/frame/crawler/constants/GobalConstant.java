package com.frame.crawler.constants;
/**
 * 全局静态变量
 * Created by zhh on 2018/03/30.
 */
public interface GobalConstant {
	
	/** 爬取来源类型 **/
	interface CrawleSrcType {
		// 所有类型
		Integer ALL = 0;
		// 百度
		Integer BAI_DU = 1;
		// 搜狗
		Integer SO_GOU = 2;
		// 360
		Integer SO_360 = 3;
	}
	
	/** 搜索关键词分类 **/
	interface KeyWordType {
		// 诈骗
		Integer SWINDLE = 1;
	}
	
	/** 代理有效性 **/
	interface ProxyValid {
		// 有效
		Integer VALID = 0;
		// 无效
		Integer INVALID = 1;
	}
	
	/** 协议类型 **/
	interface NetType {
		// http
		String HTTP = "http";
		// https
		String HTTPS = "https";
		// ftp
		String FTP = "ftp";
	}
	
	/** 文件目录 **/
	interface FilePath {
		// 文件目录
		String FILES = "files";
		// 图片目录
		String IMAGES = "images";
	}
	
	/** 符号常量 **/
	interface Symbol {
		// 正斜杠
		String FORWARD_SLASH = "/";
	}
	
	/** 数据读取标志 **/
	interface ReadFlag {
		// 未读
		Integer NOT_READ = 0;
		// 已读
		Integer READ = 1;
	}
}
