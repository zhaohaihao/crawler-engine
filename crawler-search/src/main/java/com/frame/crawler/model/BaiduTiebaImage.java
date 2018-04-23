package com.frame.crawler.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.alibaba.fastjson.JSON;

/**
 * 
 * Created by zhh on 2018/04/13.
 */
@Table(name = "baidu_tieba_image")
public class BaiduTiebaImage implements Serializable {

	private static final long serialVersionUID = 2932440112198211018L;
	
	/**
	 * ID
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	/**
	 * 原始图片url
	 */
	private String originalUrl;
	
	/**
	 * 存储文件根路径
	 */
	private String rootPath;
	
	/**
	 * 本地图片相对路径
	 */
	private String targetUrl;
	
	/**
	 * 关联留言UUID
	 */
	private String contentUuid;
	
	/**
	 * 顺序，默认1
	 */
	private Integer sort;
	
	/**
     * 读取标志；0.未读，1.已读；默认0.未读
     */
    private Integer flag;
	
	/**
	 * 导入日期
	 */
	private Date importDate;
	
	/**
	 * 关联ID
	 */
	private String uuid;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getOriginalUrl() {
		return originalUrl;
	}

	public void setOriginalUrl(String originalUrl) {
		this.originalUrl = originalUrl;
	}
	
	public String getRootPath() {
		return rootPath;
	}

	public void setRootPath(String rootPath) {
		this.rootPath = rootPath;
	}

	public String getTargetUrl() {
		return targetUrl;
	}

	public void setTargetUrl(String targetUrl) {
		this.targetUrl = targetUrl;
	}

	public String getContentUuid() {
		return contentUuid;
	}

	public void setContentUuid(String contentUuid) {
		this.contentUuid = contentUuid;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public Date getImportDate() {
		return importDate;
	}

	public void setImportDate(Date importDate) {
		this.importDate = importDate;
	}
	
	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}
	
	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}
}
