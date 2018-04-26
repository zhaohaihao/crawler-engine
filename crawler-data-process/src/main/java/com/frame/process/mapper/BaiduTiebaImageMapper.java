package com.frame.process.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.frame.process.core.MyMapper;
import com.frame.process.model.BaiduTiebaImage;
/**
 * 
 * Created by zhh on 2018/04/13.
 */
public interface BaiduTiebaImageMapper extends MyMapper<BaiduTiebaImage> {
	
	/**
	 * 筛选指定条数贴吧图片信息
	 * @param startPos 起始位
	 * @param limit 限定数目
	 * @param flag 已读标志
	 * @return
	 */
	List<BaiduTiebaImage> selectBaiduTiebaImageByLimit(@Param("startPos") Integer startPos, @Param("limit") Integer limit, @Param("flag") Integer flag);
}
