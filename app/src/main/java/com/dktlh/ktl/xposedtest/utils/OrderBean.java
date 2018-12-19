package com.dktlh.ktl.xposedtest.utils;

import java.io.Serializable;

public class OrderBean
  implements Serializable
{
  private static final long serialVersionUID = 8988815091574805671L;
  private String dt;
  private String mark;
  private String money;
  private String no;
  private String result;
  private int time;
  private String type;
  
  public OrderBean() {}
  
  public OrderBean(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, String paramString6, int paramInt)
  {
    this.money = paramString1;
    this.mark = paramString2;
    this.type = paramString3;
    this.no = paramString4;
    this.dt = paramString5;
    this.result = paramString6;
    this.time = paramInt;
  }
  
  public String getDt()
  {
    return this.dt;
  }
  
  public String getMark()
  {
    return this.mark;
  }
  
  public String getMoney()
  {
    return this.money;
  }
  
  public String getNo()
  {
    return this.no;
  }
  
  public String getResult()
  {
    return this.result;
  }
  
  public int getTime()
  {
    return this.time;
  }
  
  public String getType()
  {
    return this.type;
  }
  
  public void setDt(String paramString)
  {
    this.dt = paramString;
  }
  
  public void setMark(String paramString)
  {
    this.mark = paramString;
  }
  
  public void setMoney(String paramString)
  {
    this.money = paramString;
  }
  
  public void setNo(String paramString)
  {
    this.no = paramString;
  }
  
  public void setResult(String paramString)
  {
    this.result = paramString;
  }
  
  public void setTime(int paramInt)
  {
    this.time = paramInt;
  }
  
  public void setType(String paramString)
  {
    this.type = paramString;
  }
}
