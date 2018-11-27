package com.internet.sdk.config;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Properties;

import com.internet.sdk.BaseClass;
import com.internet.sdk.GlobalFunction;
import com.internet.sdk.GlobalNames;

public class ConfigManage extends BaseClass
{
	public ConfigManage()
	{
		super(ConfigManage.class.getName());
		this.log.info("进入了ConfigManage");
	}
	
	@SuppressWarnings("rawtypes")
	public Boolean InitConfig(String path)
	{
		Boolean ok = Boolean.valueOf(true);
		Properties prop = new Properties();
		try
		{
			InputStream in = new BufferedInputStream(new FileInputStream(path));
			prop.load(in);
			Iterator it = prop.stringPropertyNames().iterator();
			while (it.hasNext()) {
				
				String key = (String)it.next();
				if ("SERVER_ROOT".equals(key)) {
					GlobalNames.SERVER_ROOT = prop.getProperty(key);	
				}
				
				if ("SERVER_PORT".equals(key)) {
					GlobalNames.SERVER_PORT = prop.getProperty(key);
				}
				
				if ("SERVER_NAME".equals(key)) {
					GlobalNames.SERVER_NAME = prop.getProperty(key);
				}
				
				if ("MQ_NAMESERVER".equals(key)) {
					GlobalNames.MQ_NAMESERVER = prop.getProperty(key);
				}
				
				if ("DB_OWNER".equals(key)) {
					GlobalNames.DB_OWNER = prop.getProperty(key);
				}
				
				if ("SESSION_TYPE".equals(key)) {
					GlobalNames.SESSION_TYPE = prop.getProperty(key);
				}
				
				if ("ALLOW_APPKEYS".equals(key)) {
					GlobalNames.ALLOW_APPKEYS = prop.getProperty(key);
				}
				
				if ("DEBUG_MODE".equals(key)) {
					GlobalNames.DEBUG_MODE = prop.getProperty(key);
				}
				
				if ("DATABASE_TYPE".equals(key)) {
					String value = prop.getProperty(key);
					if ((value != null) && (!"".equals(value))) {
						GlobalNames.DATABASE_TYPE = Integer.parseInt(value);
					}
				}
				if ("UNIFIED_SSO_FAILURE_TIME".equals(key)) {
					String value = prop.getProperty(key);
					if ((value != null) && (!"".equals(value))) {
						GlobalNames.UNIFIED_SSO_FAILURE_TIME = Integer.parseInt(value);
					}
				}
				
				if ("APPLIATION_SERVERS".equals(key)) {
					String value = prop.getProperty(key);
					if ((value != null) && (!"".equals(value))) {
						GlobalNames.APPLIATION_SERVERS = Integer.parseInt(value);
					}
				}
				
				if ("DB_JNDI_NAME".equals(key)) {
					GlobalNames.DB_JNDI_NAME = prop.getProperty(key);
				}
				
				if ("RESOURCE_SERVER_IP".equals(key)) {
					GlobalNames.RESOURCE_SERVER_IP = prop.getProperty(key);
				}
				
				if ("RESOURCE_SERVER_PORT".equals(key)) {
					GlobalNames.RESOURCE_SERVER_PORT = prop.getProperty(key);
				}
				
				if ("RESOURCE_SERVER_APPNAME".equals(key)) {
					GlobalNames.RESOURCE_SERVER_APPNAME = prop.getProperty(key);
				}
				
				if ("HBASE_DAO_PATH".equals(key)) {
					GlobalNames.HBASE_DAO_PATH = prop.getProperty(key);
				}
				
				if ("CODE_SRC_PATH".equals(key)) {
					GlobalNames.CODE_SRC_PATH = prop.getProperty(key);
				}
				
				if ("HBASE_PORT".equals(key)) {
					GlobalNames.HBASE_PORT = prop.getProperty(key);
				}
				
				if ("HBASE_SERVER_IP".equals(key)) {
					GlobalNames.HBASE_SERVER_IP = prop.getProperty(key);
				}
				
				if ("DATABASE_NAME".equals(key)) {
					GlobalNames.DATABASE_NAME = prop.getProperty(key);
				}
				
				if ("DB_DAO_PATH".equals(key)) {
					GlobalNames.DB_DAO_PATH = prop.getProperty(key);
				}
				
				if ("CODE_CREATE_MODEL".equals(key)) {
					GlobalNames.CODE_CREATE_MODEL = prop.getProperty(key);
				}
				
				if ("CREATE_CONFIG_JS".equals(key)) {
					GlobalNames.CREATE_CONFIG_JS = prop.getProperty(key);
				}
				
				if ("CONFIG_JS_PATH".equals(key)) {
					GlobalNames.CONFIG_JS_PATH = prop.getProperty(key);
				}
				
				if ("REDIS_SESSION_TIME".equals(key)) {
					String value = prop.getProperty(key);
					if ((value != null) && (!"".equals(value))) {
						GlobalNames.REDIS_SESSION_TIME = Integer.parseInt(value);
					}
				}
				
				if ("REDIS_CLUSTER".equals(key)) {
					GlobalNames.REDIS_CLUSTER = prop.getProperty(key);
				}
				if ("REDIS_PASSWORD".equals(key)) {
					GlobalNames.REDIS_PASSWORD = prop.getProperty(key);
				}
				
				if ("FILE_UPLOAD_PATH".equals(key)) {
					String tem = prop.getProperty(key);
					if (tem.indexOf(":") > -1) {
						GlobalNames.FILE_UPLOAD_PATH = tem;
					} else {
						String webroot = GlobalFunction.getWebRootPath();
						GlobalNames.FILE_UPLOAD_PATH = webroot + tem;
					}
					this.log.info("GlobalNames.FILE_UPLOAD_PATH=" + GlobalNames.FILE_UPLOAD_PATH);
				}
				
			}
			in.close();
			
			in = null;
		} catch (Exception e) {
			System.out.println(e);
			ok = Boolean.valueOf(false);
		}
		prop = null;
		Properties obj = System.getProperties();
		
		String os = obj.getProperty("os.name").toLowerCase();
		if (os.indexOf("win") > 0) {
			GlobalNames.OS_TYPE = "win";
		}
		else {
			GlobalNames.OS_TYPE = "linux";
		}
		
		return ok;
	}
}