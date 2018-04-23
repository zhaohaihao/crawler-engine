package com.frame.crawler.mapper;

import java.util.List;

import com.frame.crawler.core.MyMapper;
import com.frame.crawler.model.SearchKeyword;
/**
 * 
 * Created by zhh on 2018/04/08.
 */
public interface SearchKeywordMapper extends MyMapper<SearchKeyword> {
	
	/**
	 * 查询所有满足条件的搜索关键词(有效且在时间范围内)
	 * @param timeInterval 时间间隔, 单位: 天
	 * @return
	 */
	List<SearchKeyword> selectMatchConditionKeyWord(Integer timeInterval);
}
