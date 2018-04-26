package com.frame.process.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.frame.process.core.AbstractService;
import com.frame.process.mapper.ExportConfigMapper;
import com.frame.process.model.ExportConfig;
import com.frame.process.service.ExportConfigService;

/**
 * 
 * Created by zhh on 2018/04/26.
 */
@Service
public class ExportConfigServiceImpl extends AbstractService<ExportConfig> implements ExportConfigService {

	@Autowired
	private ExportConfigMapper exportConfigMapper;
	
	@Override
	public void updateBatch(List<ExportConfig> models, Integer flag) {
	}

	@Override
	public List<ExportConfig> findDatasByLimit(Integer startPos, Integer limit, Integer flag) {
		return null;
	}

	@Override
	public List<String> findFullNames() {
		return exportConfigMapper.selectFullName();
	}

}
