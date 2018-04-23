package com.frame.crawler.service;

import com.frame.crawler.core.Service;
import com.frame.crawler.model.BaiduTiebaSearch;

/**
 * 
 * Created by zhh on 2018/04/13.
 */
public interface BaiduTiebaSearchService extends Service<BaiduTiebaSearch> {
	
	/**
	 * 插入数据
	 * @param baiduTiebaContent 百度贴吧搜索信息
	 * @return 主键
	 */
	Integer insert(BaiduTiebaSearch baiduTiebaSearch);
}
