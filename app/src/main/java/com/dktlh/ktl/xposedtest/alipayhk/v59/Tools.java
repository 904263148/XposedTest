package com.dktlh.ktl.xposedtest.alipayhk.v59;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.dktlh.ktl.xposedtest.AliPayHook;
import com.dktlh.ktl.xposedtest.tools.Tool;

import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

public class Tools {

    public static String BILLRECEIVED_ACTION = "com.hamancom.hmpayhelp.billreceived";
    public static String QRCODERECEIVED_ACTION = "com.hamancom.hmpayhelp.qrcodereceived";

    private static int count = 0;

    public static Object getMicroApplicationContext(ClassLoader classLoader)
    {
        try {
         Class LauncherApplicationAgent= classLoader.loadClass("com.alipay.mobile.framework.LauncherApplicationAgent");
           Object  LauncherApplicationAgentObj=XposedHelpers.callStaticMethod(LauncherApplicationAgent,"getInstance");
          return XposedHelpers.callMethod(LauncherApplicationAgentObj,"getMicroApplicationContext");
        }catch (Exception e)
        {
            Tool.printException(e);
        }
        return null;
    }

    public static Object findServiceByInterface(Object MicroApplicationContext,String cl)
    {
        try {

            return XposedHelpers.callMethod(MicroApplicationContext,"findServiceByInterface",cl);
        }catch (Exception e)
        {
            Tool.printException(e);
        }
        return null;
    }

  public static Object   getRpcProxy(Class cl)
  {

      try {

          ClassLoader classLoader=cl.getClassLoader();
          Object MicroApplicationContext= getMicroApplicationContext(classLoader);
          Object RpcService=findServiceByInterface(MicroApplicationContext,
                  "com.alipay.mobile.framework.service.common.RpcService");
        return   XposedHelpers.callMethod(RpcService,"getRpcProxy",cl);
      }catch (Exception e)
      {
          Tool.printException(e);
      }
      return null;
  }

  public static String getQRCode(String money, String mark, ClassLoader classLoader , Context context)
  {
      try {

          XposedBridge.log("传过来的金额------------------------"+money);
          XposedBridge.log("传过来的备注------------------------"+mark);
          XposedBridge.log("传过来的classLoader------------------------"+classLoader.toString());
          XposedBridge.log("传过来的context------------------------"+context.toString());

         Class SetCollectAmountRequest= classLoader.loadClass(
                 "com.alipay.imobilewallet.common.facade.request.SetCollectAmountRequest");
         Object SetCollectAmountRequestObj=XposedHelpers.newInstance(SetCollectAmountRequest);
         XposedHelpers.setObjectField(SetCollectAmountRequestObj,"amount",money);
          XposedHelpers.setObjectField(SetCollectAmountRequestObj,"desc",mark);
          XposedHelpers.setObjectField(SetCollectAmountRequestObj,"collectMoneyType","FIXED");//或者AA
          Class CollectMoneyFacade= classLoader.loadClass(
                  "com.alipay.imobilewallet.common.facade.transfer.CollectMoneyFacade");
         Object CollectMoneyFacadeObj= getRpcProxy(CollectMoneyFacade);
         Object resp=  XposedHelpers.callMethod(CollectMoneyFacadeObj,"setCollectAmount",SetCollectAmountRequestObj);
          XposedBridge.log("resp cn:"+resp.getClass().getName());
          String qrCodeContent=(String) XposedHelpers.getObjectField(resp,"qrCodeContent");
          XposedBridge.log("qrCodeContent:"+qrCodeContent);

          count++;
          if(count == 1){
              return null;
          }else {
              Intent localObject = new Intent();
              ((Intent)localObject).putExtra("money", money);
              ((Intent)localObject).putExtra("mark", mark);
              ((Intent)localObject).putExtra("type", "alipayhk");
              ((Intent)localObject).putExtra("payurl", qrCodeContent);
              if (money.equals("") || mark.equals("")) {
                  return null;
              }
              ((Intent)localObject).setAction(Tools.QRCODERECEIVED_ACTION);
              context.sendBroadcast((Intent)localObject);
              count = 0;
          }
          return qrCodeContent;
      }catch (Exception e)
      {
          Tool.printException(e);
      }
      return null;
  }

    public static String getQRCode(String money, String mark, ClassLoader classLoader)
    {
        try {

            Class SetCollectAmountRequest= classLoader.loadClass(
                    "com.alipay.imobilewallet.common.facade.request.SetCollectAmountRequest");
            Object SetCollectAmountRequestObj=XposedHelpers.newInstance(SetCollectAmountRequest);
            XposedHelpers.setObjectField(SetCollectAmountRequestObj,"amount",money);
            XposedHelpers.setObjectField(SetCollectAmountRequestObj,"desc",mark);
            XposedHelpers.setObjectField(SetCollectAmountRequestObj,"collectMoneyType","FIXED");//或者AA
            Class CollectMoneyFacade= classLoader.loadClass(
                    "com.alipay.imobilewallet.common.facade.transfer.CollectMoneyFacade");
            Object CollectMoneyFacadeObj= getRpcProxy(CollectMoneyFacade);
            Object resp=  XposedHelpers.callMethod(CollectMoneyFacadeObj,"setCollectAmount",SetCollectAmountRequestObj);
            XposedBridge.log("resp cn:"+resp.getClass().getName());
            String qrCodeContent=(String) XposedHelpers.getObjectField(resp,"qrCodeContent");
            XposedBridge.log("qrCodeContent:"+qrCodeContent);

            return qrCodeContent;
        }catch (Exception e)
        {
            Tool.printException(e);
        }
        return null;
    }
}
