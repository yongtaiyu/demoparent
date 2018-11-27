package com.internet.sdk;

//import com.internet.sdk.log.LogManage;
//import java.util.logging.Logger;
import org.apache.log4j.Logger;
public class BaseClass
{
  protected Logger log;

  public BaseClass(String ClassName)
  {
    //this.log = LogManage.getInstance().getLog(ClassName);
    this.log = Logger.getLogger(ClassName);
  }

  public BaseClass()
  {
  }
}