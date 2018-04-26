package com.frame.process.mapper;

import java.util.List;

import com.frame.process.core.MyMapper;
import com.frame.process.model.ExportConfig;
/**
 * 
 * Created by zhh on 2018/04/26.
 */
public interface ExportConfigMapper extends MyMapper<ExportConfig> {
	
	/**
	 * 查找需要导出的全名(类名+包名)
	 * @return
	 */
	List<String> selectFullName();
}
