package com.frame.process.service;

import java.util.List;

import com.frame.process.core.Service;
import com.frame.process.model.ExportConfig;

/**
 * 
 * Created by zhh on 2018/04/26.
 */
public interface ExportConfigService extends Service<ExportConfig> {
	
	/**
	 * 查找所有导出配置名(类名+包名)
	 * @return
	 */
	List<String> findFullNames();
}
