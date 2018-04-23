package com.frame.process.druid;

import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;

import com.alibaba.druid.support.http.StatViewServlet;

/**
 * Druid 数据源状态监控
 * Created by zhh on 2018/04/19.
 */
@WebServlet(urlPatterns = "/druid/*",
			initParams = {
					@WebInitParam(name = "allow", value = "127.0.0.1"), // IP白名单 (未配置或者为空, 则允许所有访问)
					@WebInitParam(name = "deny", value = "10.0.0.12"), // IP黑名单 (存在共同时, deny优先于allow)
					@WebInitParam(name = "loginUsername", value = "pamo"),
					@WebInitParam(name = "loginPassword", value = "pamo"),
					@WebInitParam(name = "resetEnable", value = "false") // 禁用HTML页面上的"Reset All"功能
			})
public class DruidStatViewServlet extends StatViewServlet {

	private static final long serialVersionUID = 4211416107595657917L;
	
}
