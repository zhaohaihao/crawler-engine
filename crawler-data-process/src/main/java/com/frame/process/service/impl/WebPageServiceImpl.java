package com.frame.process.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.frame.process.annotation.ExportService;
import com.frame.process.constants.GobalConstant;
import com.frame.process.core.AbstractService;
import com.frame.process.mapper.WebPageMapper;
import com.frame.process.model.WebPage;
import com.frame.process.service.WebPageService;

/**
 * 
 * Created by zhh on 2018/04/02.
 */
@ExportService(type = GobalConstant.ExcelTableAlias.WEB_PAGE, beanId = "webPageServiceImpl")
@Service
public class WebPageServiceImpl extends AbstractService<WebPage> implements WebPageService {

	@Autowired
	private WebPageMapper webPageMapper;

	@Override
	public List<WebPage> findDatasByLimit(Integer startPos, Integer limit) {
		return webPageMapper.selectWebPageByLimit(startPos, limit);
	}
}
