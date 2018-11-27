package com.internet.sdk.redis;

import java.util.Map;
import java.util.logging.Logger;

import com.internet.sdk.BaseClass;
import com.internet.sdk.GlobalNames;

public class RedisBase extends BaseClass {

	public RedisBase() {

	}

	public RedisBase(String ClassName) {
		super(ClassName);
	}

	/*
	 * 向缓存中设置字符串内容 key 关键字 value 值 return true 成功 false 失败
	 */
	public boolean setRedis(String key, String value) throws Exception {
		try {

			RedisClusterUtil.getInstance().getRedisCluster().set(key, value);
			if (GlobalNames.SHOW_REDIS_LOG == true) {
				log.info("写缓存成功！  key==" + key + "  value=" + value);
			}
			return true;
		} catch (Exception e) {
			log.info("写 缓存 错误！ key==" + key + "  value=" + value + " " + e.toString());
			return false;
		}
	}

	/*
	 * 向缓存中设置字符串内容 (指定 存活时间) key 关键字 value 值 time 数据存活时间 单位 秒
	 */
	public boolean setRedis(String key, String value, int time) throws Exception {
		try {

			RedisClusterUtil.getInstance().getRedisCluster().setex(key, time, value);
			if (GlobalNames.SHOW_REDIS_LOG == true) {
				log.info("写字符缓存成功！  key==" + key + "  value=" + value + " time=" + time);
			}
			return true;
		} catch (Exception e) {
			log.info("写 字符缓存 错误！ key==" + key + "  value=" + value + " time=" + time + " " + e.toString());
			return false;
		}
	}

	/*
	 * 向缓存中设置hash内容 key 关键字 field hash key
	 */
	public boolean hsetRedis(String key, String field, String value, int time) {
		try {
			RedisClusterUtil.getInstance().getRedisCluster().hset(key, field, value);
			this.setExpire(key, time); // 设置 key的失效时间

			if (GlobalNames.SHOW_REDIS_LOG == true) {
				log.info("写hash缓存成功！  key==" + key + " filed=" + field + "  value=" + value);
			}
			return true;
		} catch (Exception e) {
			log.info("写hash缓存错误！  key==" + key + " filed=" + field + "  value=" + value + " " + e.toString());
			return false;
		}
	}

	/*
	 * 向缓存中设置hash内容 key 关键字 field hash key
	 */
	public boolean hsetRedis(String key, String field, String value) {
		try {
			RedisClusterUtil.getInstance().getRedisCluster().hset(key, field, value);
			if (GlobalNames.SHOW_REDIS_LOG == true) {
				log.info("写hash缓存成功！  key==" + key + " filed=" + field + "  value=" + value);
			}
			return true;
		} catch (Exception e) {
			log.info("写hash缓存错误！  key==" + key + " filed=" + field + "  value=" + value + " " + e.toString());
			return false;
		}
	}

	/*
	 * 取缓存中值 key 关键子 field hash中的key
	 */
	public String hgetRedis(String key, String field) throws Exception {
		String value = "";
		try {
			value = RedisClusterUtil.getInstance().getRedisCluster().hget(key, field);
			if (GlobalNames.SHOW_REDIS_LOG == true) {
				log.info("读hash缓存成功！  key==" + key + "  field=" + field + "  value=" + value);
			}
		} catch (Exception e) {
			log.info("取hash缓存错误！ key==" + key + "  field=" + field + "  " + e.toString());
		}
		return value;

	}

	/* 判断 key 是否存在 */
	public Boolean existsRedis(String key) throws Exception {
		Boolean ret = false;
		try {
			ret = RedisClusterUtil.getInstance().getRedisCluster().exists(key);
			if (GlobalNames.SHOW_REDIS_LOG == true) {
				log.info("缓存中 (" + key + ")是否存在=" + ret);
			}
		} catch (Exception e) {
			log.info("判断缓存中(" + key + ")是否存在错误! " + e.toString());
			ret = false;
		}
		return ret;
	}

	/* 设置key 的失效时间 */
	public Boolean setExpire(String key, int time) throws Exception {
		try {
			RedisClusterUtil.getInstance().getRedisCluster().expire(key, time);
			if (GlobalNames.SHOW_REDIS_LOG == true) {
				log.info("设置key的失效时间成功！  key==" + key + " time=" + time);
			}
			return true;
		} catch (Exception e) {
			log.info("设置key的失效时间失败！  key==" + key + " time=" + time + "  " + e.toString());
			return false;
		}
	}

	/*
	 * 取缓存中的 map 值 key 关键子
	 */
	public Map<String, String> hgetRedis(String key) throws Exception {
		Map<String, String> map = null;
		try {
			map = RedisClusterUtil.getInstance().getRedisCluster().hgetAll(key);
			if (GlobalNames.SHOW_REDIS_LOG == true) {
				log.info("读hash缓存成功！  key==" + key + "  map=" + map);
			}
		} catch (Exception e) {
			log.info("取hash缓存错误！ key==" + key + "  map=" + map + "  " + e.toString());
		}
		return map;
	}

	/*
	 * 取缓存中值 key 关键子
	 */
	public String getRedis(String key) throws Exception {
		String value = "";
		try {
			value = RedisClusterUtil.getInstance().getRedisCluster().get(key);
			if (GlobalNames.SHOW_REDIS_LOG == true) {
				log.info("读缓存成功！  key==" + key + "  value=" + value);
			}
		} catch (Exception e) {
			log.info("取缓存错误！ key==" + key + "  " + e.toString());
		}
		return value;
	}

	/*
	 * 删除 key 的值 key 关键字 return true 成功 false 失败
	 */
	public boolean delRedis(String key) throws Exception {
		try {
			RedisClusterUtil.getInstance().getRedisCluster().del(key);
			if (GlobalNames.SHOW_REDIS_LOG == true) {
				log.info("删除缓冲数据成功！  key==" + key);
			}
			return true;
		} catch (Exception e) {
			log.info("删除缓冲数据成功 错误！ key==" + key + " " + e.toString());
			return false;
		}
	}

}
