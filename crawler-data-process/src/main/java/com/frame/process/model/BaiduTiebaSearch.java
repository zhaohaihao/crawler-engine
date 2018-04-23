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
 * 百度贴吧信息
 * Created by zhh on 2018/04/13.
 */
@ExportType(GobalConstant.ExcelTableAlias.TIE_BA_SEARCH)
@Table(name = "baidu_tieba_search")
public class BaiduTiebaSearch implements Serializable {

	private static final long serialVersionUID = -8509789986069014247L;

	/**
	 * ID
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	/**
	 * 请求链接
	 */
	@ExportTitle("请求链接")
	private String reqUrl;
	
	/**
	 * 目标链接
	 */
	@ExportTitle("目标链接")
	private String targetUrl;
	
	/**
	 * 贴吧标题
	 */
	@ExportTitle("贴吧标题")
	private String title;
	
	/**
	 * 发帖用户
	 */
	@ExportTitle("发帖用户")
	private String postUser;
	
	/**
	 * 最后一次回帖用户
	 */
	@ExportTitle("最后一次回帖用户")
	private String lastReplyUser;
	
	/**
	 * 最后一次回帖时间
	 */
	@ExportTitle("最后一次回帖时间")
	private String lastReplyTime;
	
	/**
	 * 搜索关键词
	 */
	@ExportTitle("搜索关键词")
	private String keyWord;
	
	/**
	 * 所处页数，默认1
	 */
	@ExportTitle("所处页数")
	private Integer pageNum;
	
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

	public String getReqUrl() {
		return reqUrl;
	}

	public void setReqUrl(String reqUrl) {
		this.reqUrl = reqUrl;
	}

	public String getTargetUrl() {
		return targetUrl;
	}

	public void setTargetUrl(String targetUrl) {
		this.targetUrl = targetUrl;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPostUser() {
		return postUser;
	}

	public void setPostUser(String postUser) {
		this.postUser = postUser;
	}

	public String getLastReplyUser() {
		return lastReplyUser;
	}

	public void setLastReplyUser(String lastReplyUser) {
		this.lastReplyUser = lastReplyUser;
	}

	public String getLastReplyTime() {
		return lastReplyTime;
	}

	public void setLastReplyTime(String lastReplyTime) {
		this.lastReplyTime = lastReplyTime;
	}

	public String getKeyWord() {
		return keyWord;
	}

	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}

	public Integer getPageNum() {
		return pageNum;
	}

	public void setPageNum(Integer pageNum) {
		this.pageNum = pageNum;
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
