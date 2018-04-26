package com.frame.process.model;

import java.io.Serializable;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.alibaba.fastjson.JSON;

/**
 * 导出配置表
 * Created by zhh on 2018/04/26.
 */
@Table(name = "export_config")
public class ExportConfig implements Serializable {

	private static final long serialVersionUID = -6101771734815771309L;

	/**
	 * ID
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	/**
	 * 包名
	 */
	private String packName;
	
	/**
	 * 类名
	 */
	private String className;
	
	/**
	 * 导出别名
	 */
	private String exportName;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getPackName() {
		return packName;
	}

	public void setPackName(String packName) {
		this.packName = packName;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getExportName() {
		return exportName;
	}

	public void setExportName(String exportName) {
		this.exportName = exportName;
	}
	
	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}
}
