package com.frame.process.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.frame.process.annotation.ExportService;
import com.frame.process.constants.GobalConstant;
import com.frame.process.core.AbstractService;
import com.frame.process.mapper.BaiduTiebaContentMapper;
import com.frame.process.model.BaiduTiebaContent;
import com.frame.process.service.BaiduTiebaContentService;

/**
 * 
 * Created by zhh on 2018/04/13.
 */
@ExportService(type = GobalConstant.ExcelTableAlias.TIE_BA_CONTENT, beanId = "baiduTiebaContentServiceImpl")
@Service
public class BaiduTiebaContentServiceImpl extends AbstractService<BaiduTiebaContent> implements BaiduTiebaContentService {

	@Autowired
	private BaiduTiebaContentMapper baiduTiebaContentMapper;
	
	@Override
	public List<BaiduTiebaContent> findDatasByLimit(Integer startPos, Integer limit, Integer flag) {
		return baiduTiebaContentMapper.selectBaiduTiebaContentByLimit(startPos, limit, flag);
	}

	@Override
	public void updateBatch(List<BaiduTiebaContent> models, Integer flag) {
		baiduTiebaContentMapper.updateBatch(models, flag);
	}

}
