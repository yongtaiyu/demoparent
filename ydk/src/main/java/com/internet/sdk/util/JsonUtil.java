package com.zenith.client.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zenith.client.config.ClientConfig;
import com.zenith.client.config.ClientConfig.RocketMQConfig;
import com.zenith.client.util.todotask.pojo.DataSource;
import com.zenith.client.util.todotask.pojo.NextStepTaskins;
import com.zenith.client.util.todotask.pojo.OverStepTaskins;

/**
 * 待办任务转JSON工具类
 * @author HW
 *
 */
public class JsonUtil
{
  private HashMap hashData;

  public JsonUtil()
  {
    this.hashData = new HashMap();
  }
  public void addNode(HashMap hash) {
    this.hashData = hash;
  }

  public void addNode(String key, String value)
  {
    this.hashData.put(key, value);
  }

  public void addNode(String key, HashMap hash)
  {
    this.hashData.put(key, hash);
  }

  public void addNode(String key, List list)
  {
    this.hashData.put(key, list);
  }
  
  public void addNode(String key, Object obj )
  {
    this.hashData.put(key, obj);
  }

  public String getJson()
  {
    StringBuffer buffer = new StringBuffer();
    Iterator iter = this.hashData.entrySet().iterator();
    int i = 0;
    buffer.append("[{");
    while (iter.hasNext()) {
      Map.Entry entry = (Map.Entry)iter.next();
      Object key = entry.getKey();
      Object val = entry.getValue();

      if (i != 0) {
        buffer.append(",");
      }

      buffer.append("\"");
      buffer.append(key);
      buffer.append("\":");
      if (val==null)
      {
          buffer.append("\"\"");
      }
      if (val instanceof String)
      {
        if (((String)val).substring(0, 1).equals("[")) {
          buffer.append(val);
        } else {
          buffer.append("\"");
          buffer.append(val);
          buffer.append("\"");
        }
      }else if (val instanceof HashMap) {
        String json = HashMapToJson((HashMap)val);
        buffer.append(json);
      }else
      if (val instanceof List) {
        String json = ListToJson((List)val);
        buffer.append(json);
      }else  if (val instanceof Object) {
          String json = ObjectToJson((Object)val);
          buffer.append(json);
        }
     

      ++i;
    }
    buffer.append("}]");
    return buffer.toString();
  }


  
  public static String HashMapToJson(HashMap  hash){
		String json=""; 
		ObjectMapper mapper = new ObjectMapper();
		   try {
		         json=mapper.writeValueAsString(hash);
			// json="{\"return\":+mapper.writeValueAsString(list)+"}";
			// json="{\"Rows\":"+json+",\"Total\":"+Total+"}";
			} catch (Exception e) {
				e.printStackTrace();
			}
		   return json;	
	}
  
  public static String ObjectToJson(Object  obj){
		String json=""; 
		ObjectMapper mapper = new ObjectMapper();
		   try {
		         json=mapper.writeValueAsString(obj);
		         // json="{\"return\":+mapper.writeValueAsString(list)+"}";
		          // json="{\"Rows\":"+json+",\"Total\":"+Total+"}";
			} catch (Exception e) {
				e.printStackTrace();
			}
		   return json;	
	}
  
  public static String ListToJson(List  list){
		String json=""; 
		ObjectMapper mapper = new ObjectMapper();
		   try {
		         json=mapper.writeValueAsString(list);
		         // json="{\"return\":+mapper.writeValueAsString(list)+"}";
		          // json="{\"Rows\":"+json+",\"Total\":"+Total+"}";
			} catch (Exception e) {
				e.printStackTrace();
			}
		   return json;	
   }
  
  /**
   * 
   * @param from 数据来源，类型为DataFrom对象
   * @param overs 流程状态改变集合，对象为OverStepTaskins
   * @param nexts 流程新增集合，对象为NextStepTaskins
   * @return 返回值为HashMap<String,String>
   * Hash里面有3个内容
   * 1：result 返回状态
   * 2：message 状态消息
   * 3：data 若成功的状态下，返回需要发送到待办流程JSON
   */
  public static HashMap<String,String> GetTaskJson(DataSource source,List<OverStepTaskins> overs,List<NextStepTaskins> nexts){
	  JsonUtil util = new JsonUtil();
	  
	  JSONObject jsonObject = new JSONObject();
	  JSONObject sendObject = new JSONObject();
	  
	  HashMap<String, String> taskResult = new HashMap<String, String>();
	  String result = "0";//转换结果  0:成功，1：错误
	  String message = "";//消息
	  String data = "";//内容
	  /**
	   * 首先验证参数的完整性
	   */
	  message = IsLegal(source,overs,nexts);
	  /**参数验证失败的情况*/
	  if(!"success".equals(message)){
		  taskResult.put("result", "1");
		  taskResult.put("message", message);
		  taskResult.put("data", data);
	  }else{
		  util.addNode("source", source);
		  util.addNode("overstep", overs);
		  util.addNode("nextstep", nexts);
		  //组装data信息
		  jsonObject.put("ProducerGroupName",RocketMQConfig.MQ_PRODUCTOR_GROUPNAME.toUpperCase());
		  jsonObject.put("MessageBody", util.getJson());
		  sendObject.put("json", jsonObject);
		  data = sendObject.toJSONString();
		  //组装返回结果
		  taskResult.put("result", result);
		  taskResult.put("message", message);
		  taskResult.put("data",data);
	  }
	  return taskResult;
  }
 
  public static String IsLegal(DataSource source,List<OverStepTaskins> overs,List<NextStepTaskins> nexts){
	  String result = "";
	  /** 首先验证from参数是否完整*/
	  if("".equals(source.getSystemid()) || source.getSystemid() == null || "".equals(source.getCodetype()) || source.getCodetype() == null){
		  return result+":DataFrom中缺失必要参数";
	  }
	  /** 验证overs参数是否完整*/
	  for(OverStepTaskins over:overs){
		  if(over.getBusiid()==null || "".equals(over.getBusiid()) || over.getFlowstatus()==null || "".equals(over.getFlowstatus())){
			  return result+":OverStepTaskins中缺失必要参数";
		  }
		  if(over.getReceiverid()==null || "".equals(over.getReceiverid()) || over.getTaskinsid()==null || "".equals(over.getTaskinsid())){
			  return result+":OverStepTaskins中缺失必要参数";
		  }
	  }
	  /** 验证nexts参数是否完整*/
	  for(NextStepTaskins next:nexts){
		  if(next.getBusiid()==null || "".equals(next.getBusiid()) || next.getBusiname()==null || "".equals(next.getBusiname())){
			  return result+":NextStepTaskins中缺失必要参数";
		  }
		  if(next.getBusitype()==null || "".equals(next.getBusitype()) || next.getCategory()==null || "".equals(next.getCategory())){
			  return result+":NextStepTaskins中缺失必要参数";
		  }
		  if(next.getFlowstatus()==null || "".equals(next.getFlowstatus()) || next.getJumpurl()==null || "".equals(next.getJumpurl())){
			  return result+":NextStepTaskins中缺失必要参数";
		  }
		  if(next.getReceiver()==null || "".equals(next.getReceiver()) || next.getReceiverid()==null || "".equals(next.getReceiverid())){
			  return result+":NextStepTaskins中缺失必要参数";
		  }
		  if(next.getStarttime()==null || "".equals(next.getStarttime()) || next.getTaskinsid()==null || "".equals(next.getTaskinsid())){
			  return result+":NextStepTaskins中缺失必要参数";
		  }
		  //若无事项过程，则默认给空字符串占位
		  if(next.getProcess()==null || "".equals(next.getProcess())){
			  next.setProcess(" ");
		  }
	  }
	  result = "success";
	  return result;
  }
  
}