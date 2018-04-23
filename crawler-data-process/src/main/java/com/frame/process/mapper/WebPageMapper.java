package com.frame.process.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.frame.process.core.MyMapper;
import com.frame.process.model.WebPage;

/**
 * 
 * Created by zhh on 2018/04/02.
 */
public interface WebPageMapper extends MyMapper<WebPage> {
	
	/**
	 * 筛选指定条数页面内容
	 * @param startPos 起始位
	 * @param limit 限定数目
	 * @return
	 */
	List<WebPage> selectWebPageByLimit(@Param("startPos") Integer startPos, @Param("limit") Integer limit);
}
