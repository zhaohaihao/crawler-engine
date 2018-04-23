package com.frame.crawler.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.frame.crawler.core.MyMapper;
import com.frame.crawler.model.BaiduTiebaImage;
/**
 * 
 * Created by zhh on 2018/04/13.
 */
public interface BaiduTiebaImageMapper extends MyMapper<BaiduTiebaImage> {
	
	/**
	 * 筛选未下载图片
	 * @param limit 限制条数
	 * @return
	 */
	List<BaiduTiebaImage> selectNotDownloadImagesByLimit(@Param("limit") Integer limit);
}
