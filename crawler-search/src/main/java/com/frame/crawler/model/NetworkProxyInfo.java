package com.frame.crawler.model;

import java.io.Serializable;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.alibaba.fastjson.JSON;

/**
 * 代理ip信息
 * Created by zhh on 2018/03/30.
 */
@Table(name = "network_proxy_info")
public class NetworkProxyInfo implements Serializable {
	
	private static final long serialVersionUID = 8244758142724688624L;

	/**
	 * ID
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	/**
	 *  代理Ip
	 */
	private String proxyIp;
	
	/**
	 *  代理端口
	 */
	private String proxyPort;
	
	/**
	 *  网站地址
	 */
	private String networkUrl;
	
	/**
	 *  是否有效，0.有效，1.无效；默认0
	 */
    private Integer isValid;
    
    /**
     *  是否互联网
     */
    private Integer isInternet;
    
    /**
     * 协议类型：http,https等
     */
    private String netType;
    
    /**
     *  用户名
     */
    private String userName;
    
    /**
     *  密码
     */
    private String pwd;
    
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	
	public String getProxyIp() {
		return proxyIp;
	}

	public void setProxyIp(String proxyIp) {
		this.proxyIp = proxyIp;
	}

	public String getProxyPort() {
		return proxyPort;
	}

	public void setProxyPort(String proxyPort) {
		this.proxyPort = proxyPort;
	}

	public String getNetworkUrl() {
		return networkUrl;
	}

	public void setNetworkUrl(String networkUrl) {
		this.networkUrl = networkUrl;
	}
	
	public Integer getIsValid() {
		return isValid;
	}

	public void setIsValid(Integer isValid) {
		this.isValid = isValid;
	}

	public Integer getIsInternet() {
		return isInternet;
	}

	public void setIsInternet(Integer isInternet) {
		this.isInternet = isInternet;
	}

	public String getNetType() {
		return netType;
	}

	public void setNetType(String netType) {
		this.netType = netType;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}
	
}
