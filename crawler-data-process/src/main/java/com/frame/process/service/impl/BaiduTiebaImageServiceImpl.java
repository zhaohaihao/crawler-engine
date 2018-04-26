package com.frame.process.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.frame.process.annotation.ExportService;
import com.frame.process.constants.GobalConstant;
import com.frame.process.core.AbstractService;
import com.frame.process.mapper.BaiduTiebaImageMapper;
import com.frame.process.model.BaiduTiebaImage;
import com.frame.process.service.BaiduTiebaImageService;

/**
 * 
 * Created by zhh on 2018/04/13.
 */
@ExportService(type = GobalConstant.ExcelTableAlias.TIE_BA_IMAGE, beanId = "baiduTiebaImageServiceImpl")
@Service
public class BaiduTiebaImageServiceImpl extends AbstractService<BaiduTiebaImage> implements BaiduTiebaImageService {

	@Autowired
	private BaiduTiebaImageMapper baiduTiebaImageMapper;

	@Override
	public List<BaiduTiebaImage> findDatasByLimit(Integer startPos, Integer limit, Integer flag) {
		return baiduTiebaImageMapper.selectBaiduTiebaImageByLimit(startPos, limit, flag);
	}

	@Override
	public void updateBatch(List<BaiduTiebaImage> models, Integer flag) {
		baiduTiebaImageMapper.updateBatch(models, flag);
	}
}
