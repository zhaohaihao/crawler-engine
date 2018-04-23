package com.frame.crawler.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.frame.crawler.core.AbstractService;
import com.frame.crawler.model.WebPage;
import com.frame.crawler.model.page.PageInfo;
import com.frame.crawler.model.query.BaseQuery;
import com.frame.crawler.service.WebPageService;
import com.github.pagehelper.PageHelper;

/**
 * 
 * Created by zhh on 2018/04/02.
 */
@Service
public class WebPageServiceImpl extends AbstractService<WebPage> implements WebPageService {
	
	@Override
	public Integer insert(WebPage webPage) {
		saveReturnGeneratedKeys(webPage);
		return webPage.getId();
	}

	@Override
	public List<WebPage> getAllWebPageByType(Integer type) {
		return findListBy("type", type);
	}

	@Override
	public PageInfo<WebPage> getLimitWebPageByType(Integer type, BaseQuery baseQuery) {
		PageHelper.startPage(baseQuery.getPageNum(), baseQuery.getPageSize());
		List<WebPage> webPages = getAllWebPageByType(type);
		return new PageInfo<WebPage>(webPages);
	}
}
