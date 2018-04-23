package com.frame.process.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.frame.process.core.MyMapper;
import com.frame.process.model.BaiduTiebaSearch;

/**
 * 
 * Created by zhh on 2018/04/13.
 */
public interface BaiduTiebaSearchMapper extends MyMapper<BaiduTiebaSearch> {
	
	/**
	 * 筛选指定条数百度贴吧信息
	 * @param startPos 起始位
	 * @param limit 限定数目
	 * @return
	 */
	List<BaiduTiebaSearch> selectBaiduTiebaSearchByLimit(@Param("startPos") Integer startPos, @Param("limit") Integer limit);
}
