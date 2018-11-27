package com.internet.sdk;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.Writer;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.TimeZone;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.MultivaluedMap;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;

import com.internet.sdk.db.DbConn;
import com.internet.sdk.redis.RedisSession;
import com.internet.sdk.util.json.parse.JsonParse;
import com.zenith.portal.db.dao.mh_cslogDao;

/*全局 公共函数 （静态函数）
 * 作者： 徐川
 * 时间： 2016年3月2日
 * */
public class GlobalFunction {

	/*
	 * 获取编码表中的信息 session session 对象 codename 代码名称
	 */
	public static HashMap getCode(HttpSession session, String codename) {
		// 得到context 上下文对象
		ServletContext context = session.getServletContext();
		LinkedHashMap codeMap = (LinkedHashMap) session.getServletContext().getAttribute(codename.toUpperCase());
		return codeMap;
	}

	/**
	 * 得到JSESSIONID,如果Cookies中没就通过HttpSession生成一个
	 * @param request
	 * @return
	 */
	public static String getJSESSIONID(HttpServletRequest request)
	{
		String sessionId = null;
		Cookie[] cookies = request.getCookies();
		//如果hearder里面已经存在了全局的Jsessionid,则直接返回
		if(request.getHeader("zid")!=null && !"".equals(request.getHeader(""))){
			return request.getHeader("zid");
		}
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("JSESSIONID")) {  // 查看请求中是否有对应的Cookie记录
					sessionId = cookie.getValue();            // 本地记录此次请求的sessionId，防止在初次请求时后台多次获取session，获取的session均不同
				    break;
				}
			}
		}
		
		if(sessionId == null)
		{
			sessionId = request.getSession().getId();
		}
		return sessionId;
	}
	
	
	/**
	 * 得到WEB-INF这个文件夹的绝对路径
	 * 
	 * @author
	 * @since 2009年6月15日
	 */
	public static String getWebInfoPath() throws Exception {
		String webInfo_path = "";
		try {
			String classes_path = getClassesPath();
			if (classes_path != null && classes_path.length() > 0) {
				webInfo_path = classes_path.substring(0, classes_path.indexOf("WEB-INF"));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return webInfo_path + "WEB-INF";
	}

	// 取webroot 绝对路径
	public static String getWebRootPath() throws Exception {
		String webInfo_path = "";
		try {
			String classes_path = getClassesPath();
			if (classes_path != null && classes_path.length() > 0) {
				webInfo_path = classes_path.substring(0, classes_path.indexOf("WEB-INF"));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return webInfo_path;
	}

	/**
	 * 得到classes这个文件夹的绝对路径
	 * 
	 * @author
	 * @since 2009年6月15日
	 */
	public static String getClassesPath() throws Exception {
		// 得到classes的绝对路径
		String classes_path = new GlobalFunction().getClass().getResource("").toString();
		String packageName = replaceString(GlobalStrings.PACKAGE_NAME, ".", "/");
		// System.out.print("\n 得到sclasses的绝对路径="+classes_path);
		classes_path = classes_path.substring(0, classes_path.indexOf(packageName));
		/* 当路径中含有中文的时候,由于得到的是utf-8编码,所以需要转换为GBK */
		classes_path = java.net.URLDecoder.decode(classes_path, "utf-8");

		switch (GlobalNames.APPLIATION_SERVERS) { // 判断当前当前应用服务器类型
		case 1: // weblogic服务器
			classes_path = classes_path.substring(("file:").length());
			break;
		case 2: // tomcat服务器
			classes_path = classes_path.substring(("file:").length());
			break;
		case 3: // jboos服务器
			classes_path = classes_path.substring(("file:").length());
			break;
		case 4: // webspher服务器
			classes_path = classes_path.substring(("file:").length());
			break;
		}
		// 如果是 linux 操作系统
		if ("linux".equals(GlobalNames.OS_TYPE)) {

			classes_path = "/" + classes_path;
		}
		// System.out.print("\n 当前 classes_path=="+classes_path);
		return classes_path;//

	}

	/**
	 * 将老字符串中的一种字符替换成另一种字符
	 * 
	 * @param strOld
	 *            老字符串
	 * @param strLook
	 *            要查找的字符
	 * @param strReplace
	 *            要替换的字符
	 */
	public static String replaceString(String strOld, String strLook, String strReplace) {
		String strNew = "";
		if (strOld == null) { // 为null返回""
			return "";
		}

		if (strLook == null || strReplace == null) { // 其中一个为null,返回oldstr;
			return strOld;
		}

		int i = strOld.indexOf(strLook);
		if (i != -1) {
			while (i != -1) {
				strNew = strNew + strOld.substring(0, i) + strReplace;
				strOld = strOld.substring(i + strLook.length());
				// i=strOld.indexOf(strLook);
				i = strOld.indexOf(strLook);
			}
			strNew = strNew + strOld;
		} else {
			strNew = strOld;
		}
		return strNew; // 返回组成的新串

	}

	public static Date StrToSqlDate(String objDate, String format) {

		if (objDate != null && !"".equals(objDate)) {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			try {
				java.util.Date dt = sdf.parse(objDate);
				return new java.sql.Date(dt.getTime());
			} catch (java.text.ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 取系统当前的日期和时间
	 * 
	 * @return 当前日期和时间
	 */
	public static String getSystemDateTime() {
		Calendar cal = Calendar.getInstance();
		// 去掉时间加八小时，通过设置时区来格式化
		// cal.add(Calendar.HOUR, 8);
		java.util.Date date = cal.getTime();
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		sDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+8"));

		String todayTime = sDateFormat.format(date);

		return todayTime;
	}

	/**
	 * 取系统当前的日期和时间
	 * 
	 * @return 当前日期和时间
	 */
	public static String getSystemDate() {

		Calendar cal = Calendar.getInstance();
		// cal.add(Calendar.HOUR, 8);
		java.util.Date date = cal.getTime();
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String todayTime = sDateFormat.format(date);

		return todayTime;
	}

	/**
	 * 取系统当前的日期和时间 MM/dd/YYYY 格式
	 * 
	 * @return 当前日期和时间
	 */
	public static String getSystemDateByFormat(String format) {
		Calendar cal = Calendar.getInstance();
		// cal.add(Calendar.HOUR, 8);
		java.util.Date date = cal.getTime();
		SimpleDateFormat sDateFormat = new SimpleDateFormat(format);
		String todayTime = sDateFormat.format(date);
		return todayTime;
	}

	/**
	 * 文件夹创建函数
	 * 
	 * @param sPath
	 *            文件夹路径
	 * @return String
	 * @auther 徐川
	 */
	public static String createFolder(String sPath) {
		sPath = sPath.replaceAll("\\\\", "/");
		File f = null;
		String sErr = "";
		String sTmpPath;
		String[] aP;
		sTmpPath = sPath;
		if (sTmpPath.indexOf("/") == -1) {
			sErr = "路径错误！";
		} else {
			aP = sPath.split("/");
			try {
				for (int i = 0; i < aP.length; i++) {
					sTmpPath = "";
					for (int j = 0; j <= i; j++) {
						sTmpPath += aP[j] + "/";
					}
					f = new File(sTmpPath);
					if (!f.exists()) {
						f.mkdir();
					}
				}
			} catch (Exception e) {
				sErr = e.getMessage();
			}
		}
		return sErr;
	}

	/**
	 * 文本文件保存
	 * 
	 * @param path
	 *            String 文件路径
	 * @param content
	 *            String 文件内容
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @auther 徐川
	 */
	public static void writeFile(String path, String content, String codetype)
			throws FileNotFoundException, IOException {
		// 创建文件夹
		String path_dir = path.replaceAll("\\\\", "/");
		path_dir = path_dir.substring(0, path_dir.lastIndexOf("/"));
		createFolder(path_dir);
		File file = new File(path);
		Writer writer = new OutputStreamWriter(new FileOutputStream(file), codetype);

		writer.write(content);
		writer.close();
		/*
		 * FileOutputStream fos = OutputStreamWriter(new FileOutputStream(file),
		 * "UTF-8");; fos.write(content.getBytes()); fos.flush(); fos.close();
		 */

	}

	public static boolean deleteFile(String sPath) {
		boolean flag = false;
		File file = new File(sPath);
		// 路径为文件且不为空则进行删除
		if (file.isFile() && file.exists()) {
			file.delete();
			flag = true;
		}
		return flag;
	}

	public static String ListToJson(LinkedList list) {
		String json = "";
		ObjectMapper mapper = new ObjectMapper();
		try {
			json = mapper.writeValueAsString(list);
			// json="{\"return\":+mapper.writeValueAsString(list)+"}";
			// json="{\"Rows\":"+json+",\"Total\":"+Total+"}";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}

	public static String HashMapToJson(HashMap hash) {
		String json = "";
		ObjectMapper mapper = new ObjectMapper();
		try {
			json = mapper.writeValueAsString(hash);
			// json="{\"return\":+mapper.writeValueAsString(list)+"}";
			// json="{\"Rows\":"+json+",\"Total\":"+Total+"}";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}

	public static String ErrorToJson(String str) {
		str = str.replace(":", " ");
		str = str.replace("[", " ");
		str = str.replace("]", " ");

		String json = "";
		json = "[{\"state\":\"false\",\"msg\":\"" + str + "\"}]";
		return json;
	}

	public static HashMap TransMultiforHash(MultivaluedMap<String, String> formParams) {
		HashMap parameter = new HashMap();
		for (String key : formParams.keySet()) {
			String value = formParams.get(key).get(0);
			parameter.put(key, value);
		}
		return parameter;
	}

	public static HashMap TransformHash(MultivaluedMap<String, String> formParams) {
		String json = "";
		json = formParams.get("json").get(0);
		HashMap parameter = new HashMap();
		if (json != null && !"".equals(json)) {
			parameter = (HashMap) JsonParse.ParseJson(json);
		}
		return parameter;
	}

	public static HashMap TransformHash(HashMap hash) {
		String json = "";
		json = (String) hash.get("json");
		HashMap parameter = new HashMap();
		if (json != null && !"".equals(json)) {
			parameter = (HashMap) JsonParse.ParseJson(json);
		}
		return parameter;
	}

	/*
	 * 得到用户请求的参数HASH 表
	 * 
	 * @param req -用户请求对象
	 * 
	 * @return HashMap 用户请求的参数数据
	 */
	public static HashMap getParameter(HttpServletRequest req) {
		HashMap reqHash = new HashMap();
		try {
			Enumeration E = req.getParameterNames(); // 得到传入的参数串
			while (E.hasMoreElements()) {
				String name = (String) E.nextElement(); // 得到参数名称
				String[] vals = req.getParameterValues(name); // 得到参数的值

				// name = name.toLowerCase(); //将参数名转换为小写
				if (vals.length != 1) {
					reqHash.put(name, vals); // 将参数保存到HASH 表中
				} else {

					if (vals[0] != null && vals[0].equals("null")) {
						reqHash.put(name, "");
					} else {
						/*
						 * //如果参数中要求加密 String
						 * isjb=(String)req.getSession().getAttribute("isjb");
						 * 
						 * if("true".equals(isjb)){ String
						 * jmname=com.mobile.util.UrlEncrypt.decryptData(name);
						 * 
						 * String de
						 * =com.mobile.util.UrlEncrypt.decryptData(vals[0]);
						 * 
						 * 
						 * reqHash.put(jmname, de); }else{
						 * 
						 * reqHash.put(name, vals[0]); }
						 */
						reqHash.put(name, vals[0]);
						// }
						// System.out.print("name="+name+"
						// value="+vals[0]+"\n");
					}
				}

			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		req.getSession().setAttribute("isjb", "");
		return reqHash;
	}

	/* 得到求总数的sql 语句 */

	public static String getSqlCount(String sql) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("select");
		buffer.append("  count(*) as total"); // 增加 select 子句
		buffer.append(" from (");
		buffer.append(sql);
		buffer.append(") tmptable");
		return buffer.toString();
	}

	/*
	 * 得到分页SQL语句 dbtype 数据库类型 1 oracle 2 Sqlserver 3 Mysql 4 sybase strsql 原始sql
	 * 语句 orderby ordarby 字段 sqlserver 数据库使用
	 */
	public static String getLimitString(int dbtype, String strsql, String orderby) {

		StringBuffer pagingSelect = new StringBuffer();
		if (dbtype == 1) { // oracle
			pagingSelect.append("select * from (select row_.*,ROWNUM ROWNUM_ from (");
			pagingSelect.append(strsql);
			pagingSelect.append(") row_ ) where ROWNUM_ BETWEEN ? AND ? ");
		} else if (dbtype == 2) { // Sqlserver 需测试

			StringBuffer strsqls = new StringBuffer();
			String strsqlnew = "";

			// 增加首先去掉order by,只能去掉from 后面的order by,如果有子sql则去掉from和下一个select
			// 之间的order by
			// 如:from xx order by (select row_number() over (order by xx) as
			// rownumber
			if (strsql.indexOf("order") > -1) { // 如果有 order by
				String strsqlvalue = strsql;
				while (strsqlvalue.indexOf(" from ") > -1) {
					int offindex = strsqlvalue.indexOf(" from ");
					strsqls.append(strsqlvalue.substring(0, offindex + 6));
					strsqlvalue = strsqlvalue.substring(offindex + 6);

					int nextselectindex = strsqlvalue.indexOf("select ");
					if (nextselectindex > -1) {
						strsqlnew = strsqlvalue.substring(0, nextselectindex);
						strsqlvalue = strsqlvalue.substring(nextselectindex);

					} else {
						strsqlnew = strsqlvalue;

					}
					while (strsqlnew.indexOf("order ") > -1
							&& (strsqlnew.charAt(strsqlnew.indexOf("order ") - 1) == '('
									|| strsqlnew.charAt(strsqlnew.indexOf("order ") - 1) == ' ')
							&& strsqlnew.indexOf(" by ") > -1) {
						int startindex = strsqlnew.indexOf("order ");
						int startindexby = startindex + strsqlnew.substring(startindex).indexOf(" by ");

						if (startindex < startindexby) {
							String sqlstart = strsqlnew.substring(0, startindexby + 4);
							String sqlend = strsqlnew.substring(startindexby + 4);
							// 找到order by后面的字符
							int orderbyendindex = 0;
							while (orderbyendindex < sqlend.length()) {
								if (sqlend.charAt(orderbyendindex) == ')') {

									break;
								}
								orderbyendindex++;
							}
							sqlend = sqlend.substring(orderbyendindex);
							strsqlnew = sqlstart + sqlend;
							strsqlnew = strsqlnew.replaceFirst("order ", " ");
							strsqlnew = strsqlnew.replaceFirst(" by ", " ");

						}
						// System.out.println(strsqlnew);

					}
					strsqls.append(strsqlnew);
				}

				pagingSelect.append(
						"Select * FROM (select ROW_NUMBER() Over (order by a." + orderby + ") as rowId,* from (");
				pagingSelect.append(strsqls);
				pagingSelect.append(")as a) as mytable where rowId between ? and  ? ");
			} else { // ordery by 判断
				pagingSelect.append("Select * FROM (select ROW_NUMBER() as rowId,* from (");
				pagingSelect.append(strsql);
				pagingSelect.append(" )as a) as mytable where rowId between ? and  ? ");
			}

		} else if (dbtype == 3) { // Mysql
			pagingSelect.append("select * from (");
			pagingSelect.append(strsql);
			pagingSelect.append(" ) tmptable WHERE 1 = 1 limit ?,?");
		} else if (dbtype == 5) { // GBase
			pagingSelect.append("select SKIP ? FIRST ? * from (");
			pagingSelect.append(strsql);
			pagingSelect.append(" ) tmptable WHERE 1 = 1");
		}
		// System.out.print("\n sql=" + pagingSelect.toString());
		return pagingSelect.toString();
	}

	public static String OkToJson(String value) {
		String json = "";
		json = "[{\"state\":\"true\",\"pid\":\"" + value + "\"}]";
		return json;
	}

	public static String OkToJsonMsg(String value) {

		String json = "";
		json = "[{\"state\":\"true\",\"msg\":\"" + value + "\"}]";
		return json;
	}

	/**
	 * 判断数据库类型 1 oracle 2 Sqlserver 3 Mysql 4 sybase 5 Gbase 此方法无法用于数据库表初始化
	 * 
	 * @return
	 * @throws SQLException
	 */
	public static int getDataBaseType(DbConn dbConn, String jndiname) throws Exception {
		// 通过driverName是否包含关键字判断
		if (StringUtils.isNotBlank(jndiname)) {
			dbConn.setConnection(jndiname);
		} else {
			dbConn.setConnection();
		}
		Connection connection = dbConn.getConnection();
		String driverName = connection.getMetaData().getDriverName().toUpperCase();
		if (driverName.indexOf("ORACLE") != -1) {
			return 1;
		} else if (driverName.indexOf("SQL SERVER") != -1) {
			return 2;
		} else if (driverName.indexOf("MYSQL") != -1) {
			return 3;
		} else if (driverName.indexOf("SYBASE") != -1) {
			return 4;
		} else if (driverName.indexOf("INFORMIX") != -1) {
			return 5;
		} else {
			return 3;
		}
	}

	public static int getDataBaseTypeBySql(DbConn dbConn, String jndiname) throws SQLException {
		// 通过driverName是否包含关键字判断
		Connection connection = dbConn.getConnection();
		String driverName = connection.getMetaData().getDriverName().toUpperCase();
		if (driverName.indexOf("ORACLE") != -1) {
			return 1;
		} else if (driverName.indexOf("SQL SERVER") != -1) {
			return 2;
		} else if (driverName.indexOf("MYSQL") != -1) {
			return 3;
		} else if (driverName.indexOf("SYBASE") != -1) {
			return 4;
		} else if (driverName.indexOf("INFORMIX") != -1) {
			return 5;
		} else {
			return 3;
		}
	}
	
	/**
	 * 获取Session中的用户相关信息
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public static HashMap<String, String> GetUserSession(HttpServletRequest request) throws Exception {
		 RedisSession redis = new RedisSession();
		 String sessionId = GlobalFunction.getJSESSIONID(request);
		 HashMap<String,String> sessionMap = null;
		 if ("Redis".equals(GlobalNames.SESSION_TYPE)) {
				if(redis.existsRedis(sessionId).booleanValue()) {
					Map<String, String> sessionRedis = redis.getSession(sessionId);
					if(sessionRedis != null && sessionRedis.size()>0)
					{
					     sessionMap = (HashMap<String,String>)sessionRedis;
					}
				}
			} else {
				Object sessionObject = request.getSession().getAttribute(sessionId);
				if(sessionObject!=null){
				     sessionMap = (HashMap<String,String>)sessionObject;
				}
			}
		 return sessionMap;
	 }
	
	public static void log(Object e ,String location) throws Exception{
		mh_cslogDao dao = new mh_cslogDao();
		HashMap<String,String>parameter = new HashMap<String,String>();
		parameter.put("message", e.toString());
		parameter.put("msglocation", location);
		dao.insert(parameter);
	}
}
