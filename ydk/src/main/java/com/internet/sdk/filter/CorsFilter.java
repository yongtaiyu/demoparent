package com.internet.sdk.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
 
/**
 * 实现跨域
 * @author Administrator
 *
 */
public class CorsFilter implements Filter {

	public CorsFilter() {
		super();
	}
 
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

 
	@Override
	public void doFilter(ServletRequest requestServlet, ServletResponse responseServlet,FilterChain filterChain) throws IOException, ServletException {
		HttpServletResponse response = (HttpServletResponse) responseServlet;
		HttpServletRequest request = (HttpServletRequest) requestServlet;
		
		String sOrigin = request.getHeader("Origin");//?sOrigin:"*"

		if(sOrigin ==null || "".equals(sOrigin))
		{
			sOrigin = "*";
		}
		response.addHeader("Access-Control-Allow-Origin",sOrigin);  //"http://www.cs.com:7001"
		response.addHeader("Access-Control-Allow-Headers","Origin,Referer,No-Cache,X-Requested-With,If-Modified-Since,Pragma,Last-Modified,Cache-Control,Expires,Content-Type,X-E4M-With,appkey,token,tokenAcc");
		response.addHeader("Access-Control-Allow-Credentials", "true");
		response.addHeader("Access-Control-Allow-Methods","GET,POST,PUT,DELETE,OPTIONS,HEAD");
		response.addHeader("Access-Control-Max-Age", "1209600");

		if("OPTIONS".equals(request.getMethod())) {
			response.setStatus(200);
			return;
		 }
		
		
		filterChain.doFilter(request, response);
	}
 
	@Override
	public void destroy() {
 
	}
}
