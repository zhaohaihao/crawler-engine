package com.frame.crawler.service.impl;

import org.springframework.stereotype.Service;

import com.frame.crawler.core.AbstractService;
import com.frame.crawler.model.BaiduTiebaSearch;
import com.frame.crawler.service.BaiduTiebaSearchService;
/**
 * 
 * Created by zhh on 2018/04/13.
 */
@Service
public class BaiduTiebaSearchServiceImpl extends AbstractService<BaiduTiebaSearch> implements BaiduTiebaSearchService {

	@Override
	public Integer insert(BaiduTiebaSearch baiduTiebaSearch) {
		saveReturnGeneratedKeys(baiduTiebaSearch);
		return baiduTiebaSearch.getId();
	}
}
