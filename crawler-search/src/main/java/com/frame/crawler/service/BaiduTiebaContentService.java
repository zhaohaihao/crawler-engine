package com.frame.crawler.service;

import com.frame.crawler.core.Service;
import com.frame.crawler.model.BaiduTiebaContent;

/**
 * 
 * Created by zhh on 2018/04/13.
 */
public interface BaiduTiebaContentService extends Service<BaiduTiebaContent> {

	/**
	 * 插入数据
	 * @param baiduTiebaContent 百度贴吧具体信息
	 * @return 主键
	 */
	Integer insert(BaiduTiebaContent baiduTiebaContent);
}
