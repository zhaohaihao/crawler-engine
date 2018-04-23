package com.frame.crawler.process.interfaces;

import java.util.List;

import com.frame.crawler.model.SearchKeyword;

public interface AnalysisInter {
	
	/**
	 * 线程执行操作
	 * @param searchKeywordList 查询关键词信息列表
	 * @throws Exception
	 */
	public abstract boolean threadExecuteGrab(List<SearchKeyword> searchKeywordList) throws Exception;
	
	/**
	 * 执行抓取操作
	 * @param url 网页 url 地址
	 * @throws Exception
	 */
	public abstract void executeGrab(String url) throws Exception;
	
	/**
	 * 获取页面数
	 * @param url 
	 * @return 网页 url 地址
	 * @throws Exception
	 */
	public abstract int getPageNum(String url) throws Exception;
}
