package com.frame.crawler.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.frame.crawler.core.AbstractService;
import com.frame.crawler.mapper.SearchKeywordMapper;
import com.frame.crawler.model.SearchKeyword;
import com.frame.crawler.service.SearchKeywordService;

/**
 * 
 * Created by zhh on 2018/04/08.
 */
@Service
public class SearchKeywordServiceImpl extends AbstractService<SearchKeyword> implements SearchKeywordService {

	@Autowired
	private SearchKeywordMapper searchKeywordMapper;
	
	@Override
	public List<SearchKeyword> getMatchConditionKeyWordList(Integer timeInterval) {
		return searchKeywordMapper.selectMatchConditionKeyWord(timeInterval);
	}

}
