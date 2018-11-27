package com.internet.sdk.filter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.internet.sdk.GlobalNames;
import com.internet.sdk.redis.RedisSession;

public class LoginFilter implements Filter {

	@SuppressWarnings("unused")
	private static final long serialVersionUID = 1L;

	// 系统应用路径
	@SuppressWarnings("unused")
	private String contextPath = null;

	// 服务器路径
	private String serverPath = null;

	// 例外路径，多个以英文逗号分隔
	private String EXCLUDE_PATH;

	// 例外路径列表
	private String[] EXCLUDE_PATH_ARRAY;
	
	// 例外后缀，多个以英文逗号分隔
	private String EXCLUDE_POSTFIX;
	// 例外后缀列表
	private String[] EXCLUDE_POSTFIX_ARRAY;

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		this.contextPath = request.getContextPath();
		this.serverPath = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();

		String strURL = request.getRequestURL().toString();
		String qstring = request.getQueryString(); 

		// 当是 http://localhost:8089/portal/ 这种情况 则按照项目所属进行登录
		if (strURL.toLowerCase().endsWith("/")) {
			response.sendRedirect(this.serverPath + "/pages/error/error404.html");
			return;
		}

		boolean isExclude = false;
		// 例外后缀 放行
		for (String fix : this.EXCLUDE_POSTFIX_ARRAY) {
			if (strURL.toLowerCase().endsWith(fix)) {
				isExclude = true;
				break;
			}
		}
		// 例外路径 放行
		for (String page : this.EXCLUDE_PATH_ARRAY) {
			if (strURL.indexOf(page)>-1) {
				isExclude = true;
				break;
			}
		}
		if (isExclude) {
			filterChain.doFilter(request, response);
			return;
		}
		
		
		String sessionId=null;
		//如果用Redis做共享Session
		if ("Redis".equals(GlobalNames.SESSION_TYPE)) {
			RedisSession redis = new RedisSession();
			//如果hearder里面已经存在了全局的Jsessionid,则直接返回
			if(request.getHeader("zid")!=null && !"".equals(request.getHeader(""))){
				sessionId = request.getHeader("zid");
			}else{
				if (sessionId == null) {
					Cookie[] cookies = request.getCookies();
					if (cookies != null) {
						for (Cookie cookie : cookies) {
							if (cookie.getName().equals("JSESSIONID")) {  // 查看请求中是否有对应的Cookie记录
								sessionId = cookie.getValue();            // 本地记录此次请求的sessionId，防止在初次请求时后台多次获取session，获取的session均不同
							    break;
							}
						}
					}
				}
			}
			if(sessionId == null){
				sessionId = request.getSession().getId();
			}
			if (sessionId != null) {
				try {
					if (redis.existsRedis(sessionId).booleanValue()) {
						redis.setExpire(sessionId, GlobalNames.REDIS_SESSION_TIME);
						// 正常跳转
						filterChain.doFilter(request, response);
					}
					else
					{
						//response.sendRedirect(this.serverPath + "/logout.html");
						response.setStatus(403);
						return;
					}
				} catch (Exception ex) {
					response.setStatus(500);
					return;
				}
			}
			else
			{
				//response.sendRedirect(this.serverPath + "/logout.html");
				response.setStatus(403);
				return;
			}
			
		} else {
			// 没有登录系统或者session失效, 需要重新登录
			HttpSession session = request.getSession(false);// 不再创建新的session
			boolean bExitsSession = false;

			bExitsSession = !(session == null || session.isNew()); // true为存在session,false为不存在session
			Object userinfo = null;

			if (session != null) {
				userinfo = session.getAttribute("USER-INFO");
			}

			if (bExitsSession == false || userinfo == null) {
				// 未登录，跳转到登录页面
				//response.sendRedirect(this.serverPath + "/logout.html");
				response.setStatus(403);
				return;
			} else {
				// 正常跳转
				filterChain.doFilter(request, response);
			}
		}
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// 获取系统例外路径列表
		this.EXCLUDE_PATH = filterConfig.getInitParameter("EXCLUDE_PATH");
		if (this.EXCLUDE_PATH != null && !"".equals(this.EXCLUDE_PATH)) {
			this.EXCLUDE_PATH_ARRAY = this.EXCLUDE_PATH.split(",");
		}
		// 获取系统例外文件后缀
		this.EXCLUDE_POSTFIX = filterConfig.getInitParameter("EXCLUDE_POSTFIX");
		if (this.EXCLUDE_POSTFIX != null && !"".equals(this.EXCLUDE_POSTFIX)) {
			this.EXCLUDE_POSTFIX_ARRAY = this.EXCLUDE_POSTFIX.split("[|]");
		}
	}
	
	
	@Override
	public void destroy() {
		//
	}


}
