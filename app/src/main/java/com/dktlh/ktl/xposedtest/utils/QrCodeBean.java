package com.dktlh.ktl.xposedtest.utils;

import java.io.Serializable;

public class QrCodeBean
  implements Serializable
{
  private static final long serialVersionUID = 8988815091574805671L;
  private String dt;
  private String mark;
  private String money;
  private String payurl;
  private String type;
  
  public QrCodeBean() {}
  
  public QrCodeBean(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5)
  {
    this.money = paramString1;
    this.mark = paramString2;
    this.type = paramString3;
    this.payurl = paramString4;
    this.dt = paramString5;
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
  
  public String getPayurl()
  {
    return this.payurl;
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
  
  public void setPayurl(String paramString)
  {
    this.payurl = paramString;
  }
  
  public void setType(String paramString)
  {
    this.type = paramString;
  }
}
