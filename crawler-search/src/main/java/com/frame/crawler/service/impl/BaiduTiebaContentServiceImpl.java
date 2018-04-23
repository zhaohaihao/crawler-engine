package com.frame.crawler.service.impl;

import org.springframework.stereotype.Service;

import com.frame.crawler.core.AbstractService;
import com.frame.crawler.model.BaiduTiebaContent;
import com.frame.crawler.service.BaiduTiebaContentService;

/**
 * 
 * Created by zhh on 2018/04/13.
 */
@Service
public class BaiduTiebaContentServiceImpl extends AbstractService<BaiduTiebaContent> implements BaiduTiebaContentService {

	@Override
	public Integer insert(BaiduTiebaContent baiduTiebaContent) {
		saveReturnGeneratedKeys(baiduTiebaContent);
		return baiduTiebaContent.getId();
	}

}
