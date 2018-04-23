package com.frame.process.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.alibaba.fastjson.JSON;
import com.frame.process.annotation.ExportTitle;
import com.frame.process.annotation.ExportType;
import com.frame.process.constants.GobalConstant;

/**
 * 百度贴吧图片信息
 * Created by zhh on 2018/04/13.
 */
@ExportType(GobalConstant.ExcelTableAlias.TIE_BA_IMAGE)
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
	@ExportTitle("原始图片url")
	private String originalUrl;
	
	/**
	 * 存储文件根路径
	 */
	@ExportTitle("存储文件根路径")
	private String rootPath;
	
	/**
	 * 本地图片相对路径
	 */
	@ExportTitle("本地图片相对路径")
	private String targetUrl;
	
	/**
	 * 关联留言UUID
	 */
	@ExportTitle("关联贴吧帖子内容UUID")
	private String contentUuid;
	
	/**
	 * 顺序，默认1
	 */
	@ExportTitle("顺序号")
	private Integer sort;
	
	/**
     * 读取标志；0.未读，1.已读；默认0.未读
     */
    @ExportTitle("读取标志")
    private Integer flag;
	
	/**
	 * 导入日期
	 */
	private Date importDate = new Date();
	
	/**
     * 关联ID
     */
    @ExportTitle("关联ID")
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
	
	public String getContentUuid() {
		return contentUuid;
	}

	public void setContentUuid(String contentUuid) {
		this.contentUuid = contentUuid;
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
