package com.frame.crawler.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.alibaba.fastjson.JSON;

/**
 * 百度贴吧帖子内容
 * Created by zhh on 2018/04/13.
 */
@Table(name = "baidu_tieba_content")
public class BaiduTiebaContent implements Serializable {

	private static final long serialVersionUID = -8486576598913906970L;
	
	/**
	 * ID
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	/**
	 * 回帖用户ID
	 */
	private Integer replyUserId;
	
	/**
	 * 回帖用户
	 */
	private String replyUser;
	
	/**
	 * 回帖信息
	 */
	private String replyContent;
	
	/**
	 * 回帖日期
	 */
	private Date replyDate;
	
	/**
	 * 关联帖子UUID
	 */
	private String searchUuid;
	
	/**
	 * 所处页数，默认1
	 */
	private Integer pageNum;
	
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
	
	public Integer getReplyUserId() {
		return replyUserId;
	}

	public void setReplyUserId(Integer replyUserId) {
		this.replyUserId = replyUserId;
	}

	public String getReplyUser() {
		return replyUser;
	}

	public void setReplyUser(String replyUser) {
		this.replyUser = replyUser;
	}

	public String getReplyContent() {
		return replyContent;
	}

	public void setReplyContent(String replyContent) {
		this.replyContent = replyContent;
	}

	public Date getReplyDate() {
		return replyDate;
	}

	public void setReplyDate(Date replyDate) {
		this.replyDate = replyDate;
	}

	public String getSearchUuid() {
		return searchUuid;
	}

	public void setSearchUuid(String searchUuid) {
		this.searchUuid = searchUuid;
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
