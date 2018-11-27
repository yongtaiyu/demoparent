package com.internet.sdk.redis;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Logger;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import com.internet.sdk.BaseClass;
import com.internet.sdk.GlobalNames;
import com.internet.sdk.log.LogManage;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster; 


/*
 * redis-cluster java客户端工具类(单例)   
 * @author 徐川 
 * @Date 2016-11-22 
 */  
public class RedisClusterUtil {
	protected Logger log; // 日志对象  
	private static RedisClusterUtil redisClusterComponent = new RedisClusterUtil(); 
	private JedisCluster redisCluster; // redisCluster客户端    
   // private final static int TIME_OUT = 15;  
   
   
    private RedisClusterUtil() {  
    	log = LogManage.getInstance().getLog(RedisClusterUtil.class.getName()); 
    	String config=GlobalNames.REDIS_CLUSTER;//得到集群配置信息
    	if(config==null || config!=null && "".equals(config)){
    		log.info("没有配置集群,请在 config.properties 文件中配置 REDIS_CLUSTER属性！");
    	  return ;
    	}
    	String [] Aconfig=config.split(",");
        // common-pool配置  
        GenericObjectPoolConfig poolConfig = getCommonPoolConfig();  
        try {  
            // redis节点信息  
            Set<HostAndPort> nodeList = new HashSet<HostAndPort>();  
            for(int i=0;i<Aconfig.length;i++){
            	 String item=Aconfig[i];
            	 String[] Atem=item.split(":");
            	 int port=Integer.parseInt( Atem[1]);
            	 nodeList.add(new HostAndPort(Atem[0],port));  
                log.info("初始化 redis 集群节点 ip="+Atem[0]+" port="+Atem[1]); 	 
            }
           
            redisCluster = new JedisCluster(nodeList,poolConfig); 
         
             redisCluster.hset("auth","1","1");
            // redisCluster = new JedisCluster()
           // redisCluster.auth("xuc751119");
        } catch (Exception e) {  
        	log.info(e.getMessage());  
        }  
    }  
    /** 
     * 生成默认的common-pool配置 
     *  
     * @return 
     */  
    public static GenericObjectPoolConfig getCommonPoolConfig() {  
        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();  
        poolConfig.setMaxTotal(GenericObjectPoolConfig.DEFAULT_MAX_TOTAL * 10);  
        poolConfig.setMaxIdle(GenericObjectPoolConfig.DEFAULT_MAX_IDLE * 5);  
        poolConfig.setMinIdle(GenericObjectPoolConfig.DEFAULT_MAX_IDLE * 2);  
        // JedisPool.borrowObject最大等待时间  
        poolConfig.setMaxWaitMillis(1000L);  
        // 开启jmx  
        poolConfig.setJmxEnabled(true);  
        return poolConfig;  
    }
    
    public static RedisClusterUtil getInstance() {  
        return redisClusterComponent;  
    }  
    public void destroy() throws IOException {  
        if (redisCluster != null) {  
            redisCluster.close();  
        }  
    }  
    public JedisCluster getRedisCluster() {  
    	
    	
    	return redisCluster;  
    } 
    
    


    public static void main(String[] args) { 

    	
    	long startTime;
    	//RedisClusterUtil.getInstance().getRedisCluster().flushDB();
    	
    	//RedisClusterUtil.getInstance().getRedisCluster().set("xf", "a2");
  	  //
    	  startTime=System.currentTimeMillis();   //获取开始时间  
         for(int i=0;i<1000000;i++){
           RedisClusterUtil.getInstance().getRedisCluster().set("abcdefghix-"+i, i+"-dadffdsfdsfdsfsfsdfdsfedffsfwesxzcwqe");
     	    if(i % 1000 ==0){
     	    	long endTime=System.currentTimeMillis()-startTime; //获取结束时间  
     	       System.out.println("写 "+i +" 条数据耗时"+ Long.toString(endTime)+"ms(毫秒)");
     	    }
        }
        long endTime=System.currentTimeMillis()-startTime; //获取结束时间  
        	 
        System.out.println("写 1000000 条数据耗时"+ Long.toString(endTime)+"ms(毫秒)");
       
     }  
}
