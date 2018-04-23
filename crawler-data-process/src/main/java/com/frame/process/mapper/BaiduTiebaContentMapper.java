package com.frame.process.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.frame.process.core.MyMapper;
import com.frame.process.model.BaiduTiebaContent;

/**
 * 
 * Created by zhh on 2018/04/13.
 */
public interface BaiduTiebaContentMapper extends MyMapper<BaiduTiebaContent> {
	
	/**
	 * 筛选指定条数贴吧帖子内容
	 * @param startPos 起始位
	 * @param limit 限定数目
	 * @return
	 */
	List<BaiduTiebaContent> selectBaiduTiebaContentByLimit(@Param("startPos") Integer startPos, @Param("limit") Integer limit);
}
