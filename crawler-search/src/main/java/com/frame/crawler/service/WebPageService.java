package com.frame.crawler.service;

import java.util.List;

import com.frame.crawler.core.Service;
import com.frame.crawler.model.WebPage;
import com.frame.crawler.model.page.PageInfo;
import com.frame.crawler.model.query.BaseQuery;

/**
 * 
 * Created by zhh on 2018/04/02.
 */
public interface WebPageService extends Service<WebPage> {
	
	/**
	 * 插入数据
	 * @param webPage 页面数据
	 * @return 主键
	 */
	Integer insert(WebPage webPage);
	
	/**
	 * 根据来源类型查询所有页面数据
	 * @param type 来源类型
	 * @return
	 */
	List<WebPage> getAllWebPageByType(Integer type);
	
	/**
	 * 根据来源类型分页查询页面数据
	 * @param type 来源类型
	 * @return
	 */
	PageInfo<WebPage> getLimitWebPageByType(Integer type, BaseQuery baseQuery);
}
