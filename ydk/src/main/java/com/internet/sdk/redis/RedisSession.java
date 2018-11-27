package com.internet.sdk.redis;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.internet.sdk.BaseClass;
import com.internet.sdk.GlobalNames;
//import com.zenith.gsxt.db.dao.htjbxxDao;

/*
 * redis-sesson  共享session 类   
 * @author 徐川 
 * @Date 2016-11-22 
 */
public class RedisSession extends RedisBase {

	private static RedisSession redisSession = new RedisSession();

	public RedisSession() {
		super(RedisSession.class.getName());
	}

	public static RedisSession getInstance() {
		return redisSession;
	}

	/*
	 * 设置 session 值 sessionid session id key 关键字 value 值
	 */
	public void setSession(String sessionid, String key, String value) throws Exception {
		this.hsetRedis(sessionid, key, value, GlobalNames.REDIS_SESSION_TIME);
	}

	/*
	 * 获取sesion 值 sessionid 会话id key session中的key
	 */
	public String getSession(String sessionid, String key) throws Exception {
		return this.hgetRedis(sessionid, key);
	}

	/*
	 * 获取sesion map 值 sessionid 会话id
	 */
	public Map<String, String> getSession(String sessionid) throws Exception {
		return this.hgetRedis(sessionid);
	}

	/*
	 *  设置session 
	 */
	public void setSession(String sessionid, HashMap<String, String> hash) throws Exception {
		Iterator iter = hash.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			String key = (String) entry.getKey();
			String value = (String) entry.getValue();
			this.hsetRedis(sessionid, key, value);
		}
		this.setExpire(sessionid, GlobalNames.REDIS_SESSION_TIME);
	}
}
