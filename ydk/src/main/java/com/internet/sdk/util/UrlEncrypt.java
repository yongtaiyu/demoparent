package com.internet.sdk.util;

import java.text.SimpleDateFormat;
import java.util.Enumeration;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.net.util.Base64;

import com.internet.sdk.sso.filter.entity.AuthConst;

//import com.internet.sdk.GlobalFunction;
//import com.sun.jdi.StackFrame;
/*url 加密类
 * 作者：徐川 
 * */
public class UrlEncrypt {
	
	public final static String ENCODING = "UTF-8";    
	 //定义时间格式
	 public final static SimpleDateFormat timeFormat = new SimpleDateFormat("yyyyMMDDHHmmss"); 
	 public  final static String  SHARE_KEY  ="#$XuChuanZeinth&*";//公钥
	/*加密参数串
      * data   要加密的字符串
      * return  加密后的数据
      * */	
	public static String encrypt(String data) throws Exception{
		 Base64 base64=new Base64();
		 byte[]  b= base64.encodeBase64URLSafe(data.getBytes(ENCODING));  
	     return new String(b, ENCODING);
	}
	
	/*解密数据
	 *  data 要解密的字符串
	 *  return  解密后的数据
	 * */
	public static String decryp(String data) throws Exception {
	  byte[] b = Base64.decodeBase64(data.getBytes(ENCODING));  
	   return new String(b, ENCODING);  	
	}
	
	/* 解密 request 请求中的 参数
	 * req     http  request 请求
	 * return  请求参数  HashMap
	 * */
	 public  static HashMap<String, String> DecrypParameter(HttpServletRequest req) {
		 HashMap<String, String> reqHash = new HashMap<String, String>();
		try {  
		      Enumeration E = req.getParameterNames(); //得到传入的参数串
		      while (E.hasMoreElements()) {
		         String name = (String) E.nextElement(); //得到参数名称
		         String[] vals = req.getParameterValues(name); //得到参数的值
		         String key=decryp(name); //解密参数名
		         if(vals.length<=1){ //只有一个参数值才解密
	        		if(key.equals(AuthConst.CLIENT_URL) || key.equals(AuthConst.APPKEY)){ //如果参数名是 客户端url时不解密 或 appkey
		        		reqHash.put(key,vals[0]);
		        	}else{
		        	 
		        	    String value=decryp(vals[0]);
		        	    reqHash.put(key,value);//保存解密后的值
		        	  
		        	}
		         }
		      }
		  }catch (Exception ex) {
			  ex.printStackTrace();  
		  }
		 
		return reqHash;
	 }
	
	  // 测试主函数  
    public static void main(String args[]) {  
    	String name="zt_token";
    	String value="9ac2cb2de27b59e6c0b223c0e4c394b6";
    	
    	
    	UrlEncrypt  util=new UrlEncrypt();
        try {
			String n=util.encrypt(name);
			String v=util.encrypt(value);
			System.out.println("加密串="+n+"="+v);
			String n1=util.decryp(n);
			String v1=util.decryp(v);
			System.out.println("解密串="+n1+"="+v1);
					
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
    }
}
