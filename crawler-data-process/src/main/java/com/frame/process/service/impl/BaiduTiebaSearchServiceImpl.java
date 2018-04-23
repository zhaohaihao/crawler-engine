package com.frame.process.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.frame.process.annotation.ExportService;
import com.frame.process.constants.GobalConstant;
import com.frame.process.core.AbstractService;
import com.frame.process.mapper.BaiduTiebaSearchMapper;
import com.frame.process.model.BaiduTiebaSearch;
import com.frame.process.service.BaiduTiebaSearchService;
/**
 * 
 * Created by zhh on 2018/04/13.
 */
@ExportService(type = GobalConstant.ExcelTableAlias.TIE_BA_SEARCH, beanId = "baiduTiebaSearchServiceImpl")
@Service
public class BaiduTiebaSearchServiceImpl extends AbstractService<BaiduTiebaSearch> implements BaiduTiebaSearchService {

	@Autowired
	private BaiduTiebaSearchMapper baiduTiebaSearchMapper;

	@Override
	public List<BaiduTiebaSearch> findDatasByLimit(Integer startPos, Integer limit) {
		return baiduTiebaSearchMapper.selectBaiduTiebaSearchByLimit(startPos, limit);
	}
	
}
