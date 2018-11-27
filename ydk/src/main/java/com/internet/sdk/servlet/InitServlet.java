package com.internet.sdk.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.internet.sdk.GlobalFunction;
import com.internet.sdk.GlobalNames;
import com.internet.sdk.config.ConfigManage;
import com.internet.sdk.db.SqlUtil;

public class InitServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static String codesql = "select * from pt_code  order by bmlx,pxh,dm";

	public void destroy() {
		super.destroy();
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
		out.println("<HTML>");
		out.println("  <HEAD><TITLE>A Servlet</TITLE></HEAD>");
		out.println("  <BODY>");
		out.print("    This is ");
		out.print(getClass());
		out.println(", using the GET method");
		out.println("  </BODY>");
		out.println("</HTML>");
		out.flush();
		out.close();
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
		out.println("<HTML>");
		out.println("  <HEAD><TITLE>A Servlet</TITLE></HEAD>");
		out.println("  <BODY>");
		out.print("    This is ");
		out.print(getClass());
		out.println(", using the POST method");
		out.println("  </BODY>");
		out.println("</HTML>");
		out.flush();
		out.close();
	}

	public void InitParameter() throws ServletException {
		String FilePath = "";
		try {
			FilePath = "/" + GlobalFunction.getWebInfoPath() + "/" + "config.properties";
			ConfigManage config = new ConfigManage();

			config.InitConfig(FilePath);
			config = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void init() throws ServletException {
		InitParameter();
		try {
			loadcode();
		} catch (Exception e) {
			e.printStackTrace();
		}

		if ("true".equals(GlobalNames.CREATE_CONFIG_JS)) {
			ConfigManage config = new ConfigManage();
			String str = "";
		}
	}

	private void loadcode() throws Exception {
	/*	SqlUtil sqlutil = new SqlUtil();
		sqlutil.setSql(codesql);
		LinkedList<HashMap<String,String>> list = sqlutil.executeParamList();
		String temlx = "";
		LinkedHashMap<String,String> codeMap = new LinkedHashMap<String,String>();
		for (int i = 0; i < list.size(); i++) {
			HashMap<String,String> hash = (HashMap<String,String>) list.get(i);

			String bmlx = (String) hash.get("bmlx");
			String dm = (String) hash.get("dm");
			String value = (String) hash.get("value");

			if (temlx.equals("")) {
				temlx = bmlx;
			}
			if (!temlx.equals(bmlx)) {
				getServletContext().setAttribute(temlx.toUpperCase(), codeMap);
				codeMap = new LinkedHashMap<String,String>();
				codeMap.put(value, dm);
				temlx = bmlx;
			} else {
				codeMap.put(value, dm);
			}

		}
		getServletContext().setAttribute(temlx.toUpperCase(), codeMap);*/
	}
}