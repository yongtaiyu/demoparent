package com.internet.sdk.log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class LogManage
{
  protected Logger log;
  private static LogManage instance;

  public static LogManage getInstance()
  {
    if (instance == null) {
      instance = new LogManage();
    }

    return instance;
  }

  public LogManage()
  {
    this.log = Logger.getLogger("");
  }

  public Logger getLog(String ClassName)
  {
    this.log = Logger.getLogger(ClassName);

    return this.log;
  }

  public Logger getLog()
  {
    return this.log;
  }

  public void setLog(Logger log)
  {
    this.log = log;
  }
  class MyLogHander extends Formatter {
    MyLogHander() {
    }
    public String format(LogRecord record) { Date date = new Date();
      SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      String d = sd.format(date);
      return "[" + d + "]" + "[" + record.getLevel() + "]" + " :" + record.getMessage() + "\n";
    }
  }
}