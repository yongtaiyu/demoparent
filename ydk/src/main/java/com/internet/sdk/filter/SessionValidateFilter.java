package com.internet.sdk.filter;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.internet.sdk.GlobalFunction;
import com.internet.sdk.GlobalNames;
import com.internet.sdk.redis.RedisSession;
import com.internet.sdk.sso.filter.entity.AuthConst;
/**
 * Filter只负责/api下的接口的过滤,防止末授权访问
 * @author Administrator
 *
 */
public class SessionValidateFilter implements Filter {
	// 例外路径，多个以英文逗号分隔
	private String EXCLUDE_PATH;

	// 例外路径列表
	private String[] EXCLUDE_PATH_ARRAY;
	
	public void doFilter(ServletRequest requestServlet, ServletResponse responseServlet, FilterChain filterChain) throws IOException, ServletException {
		HttpServletResponse response = (HttpServletResponse) responseServlet;
		HttpServletRequest request = (HttpServletRequest) requestServlet;
		String strURL = request.getRequestURL().toString();
		
		boolean isExclude = false;
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
		
		if("OFF".equals(GlobalNames.DEBUG_MODE)){
		    String app_key = request.getHeader("appkey");
		    //如果请求的/api/下的接口，app_key为空或不在允许的key中那么不允许访问
		    if ((app_key == null) || ("".equals(app_key)) || (","+GlobalNames.ALLOW_APPKEYS+",").indexOf(","+app_key+",")<0) {
		    	response.setStatus(403);
			    return;
		    }
		}
		RedisSession redis = new RedisSession();
		String sessionId = GlobalFunction.getJSESSIONID(request);
		Object Is_Login = null;
		HashMap<String, String> mapSession = new HashMap<String, String>();
		if ("Redis".equals(GlobalNames.SESSION_TYPE)) {
			try {
				 mapSession = (HashMap<String, String>)redis.getSession(sessionId);
				 if(mapSession!=null){
					 Is_Login = mapSession.get(AuthConst.IS_LOGIN);
				 }
			} catch (Exception e) {
				response.setStatus(500);
				return;
			}
		} else {
			//非REDIS情况
			   mapSession = (HashMap<String, String>)request.getSession().getAttribute(sessionId);
			   if(mapSession!=null){
				   Is_Login = mapSession.get(AuthConst.IS_LOGIN);
			   }
		}
		if(Is_Login==null) {
			return;
		}
		filterChain.doFilter(request, response);
	}

	public void destroy() {
		// TODO Auto-generated method stub

	}

	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
		// 获取系统例外路径列表
		this.EXCLUDE_PATH = filterConfig.getInitParameter("EXCLUDE_PATH");
		if (this.EXCLUDE_PATH != null && !"".equals(this.EXCLUDE_PATH)) {
			this.EXCLUDE_PATH_ARRAY = this.EXCLUDE_PATH.split(",");
		}
	}

}
