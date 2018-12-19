package com.dktlh.ktl.xposedtest;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.dktlh.ktl.xposedtest.model.req.PayStateWsPayloadRequest;
import com.dktlh.ktl.xposedtest.utils.PrefUtil;
import com.dktlh.ktl.xposedtest.utils.StringUtils;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.List;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

public class AliPayHook {
    public static String BILLRECEIVED_ACTION = "com.hamancom.hmpayhelp.billreceived";
    public static String QRCODERECEIVED_ACTION = "com.hamancom.hmpayhelp.qrcodereceived";
    public static String PAYSTATERECEIVED_ACTION = "com.hamancom.hmpayhelp.paystatereceived";

    private String mark;

    private int count = 0;
//  private void securityCheckHook(ClassLoader paramClassLoader)
//  {
//    try
//    {
//      Class aClass = XposedHelpers.findClass("com.alipay.mobile.base.security.CI", paramClassLoader);
//      XposedHelpers.findAndHookMethod(aClass, "a", new Object[] { String.class, String.class, String.class, new XC_MethodHook()
//      {
//        protected void afterHookedMethod(MethodHookParam paramAnonymousMethodHookParam)
//          throws Throwable
//        {
//          Object localObject = paramAnonymousMethodHookParam.getResult();
//          XposedHelpers.setBooleanField(localObject, "a", false);
//          paramAnonymousMethodHookParam.setResult(localObject);
//          super.afterHookedMethod(paramAnonymousMethodHookParam);
//        }
//      } });
//      XposedHelpers.findAndHookMethod(paramClassLoader, "a", new Object[] { Class.class, String.class, String.class, new XC_MethodReplacement()
//      {
//        protected Object replaceHookedMethod(MethodHookParam paramAnonymousMethodHookParam)
//          throws Throwable
//        {
//          return Byte.valueOf();
//        }
//      } });
//      XposedHelpers.findAndHookMethod(paramClassLoader, "a", new Object[] { ClassLoader.class, String.class, new XC_MethodReplacement()
//      {
//        protected Object replaceHookedMethod(MethodHookParam paramAnonymousMethodHookParam)
//          throws Throwable
//        {
//          return Byte.valueOf();
//        }
//      } });
//      XposedHelpers.findAndHookMethod(paramClassLoader, "a", new Object[] { new XC_MethodReplacement()
//      {
//        protected Object replaceHookedMethod(MethodHookParam paramAnonymousMethodHookParam)
//          throws Throwable
//        {
//          return Boolean.valueOf(false);
//        }
//      } });
//      return;
//    }
//    catch (Error|Exception e)
//    {
//      e.printStackTrace();
//    }
//  }

    public void hook(final ClassLoader paramClassLoader, final Context paramContext) {
//    securityCheckHook(paramClassLoader);
        try {

            XposedHelpers.findAndHookMethod("com.alipay.mobile.security.personcenter.PersonCenterActivity", paramClassLoader
                    , "onCreate", Bundle.class, new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            super.afterHookedMethod(param);
                            XposedBridge.log("用户信息--");

                            Object AuthService = XposedHelpers.findField(param.thisObject.getClass(), "e").get(param.thisObject);
                            Object UserInfo = XposedHelpers.callMethod(AuthService, "getUserInfo");
                            String LogonId = (String) XposedHelpers.callMethod(UserInfo, "getLogonId");
                            String RealName = (String) XposedHelpers.callMethod(UserInfo, "getRealName");
                            String UserId = (String) XposedHelpers.callMethod(UserInfo, "getUserId");
                            String UserAvatar = (String) XposedHelpers.callMethod(UserInfo, "getUserAvatar");

                            if (TextUtils.isEmpty(LogonId)) {
                                XposedBridge.log("LogonId=");
                            } else {
                                XposedBridge.log("LogonId=" + LogonId);
                            }
                            if (TextUtils.isEmpty(RealName)) {
                                XposedBridge.log("RealName=");
                            } else {
                                XposedBridge.log("RealName=" + RealName);
                            }
                            if (TextUtils.isEmpty(UserId)) {
                                XposedBridge.log("UserId=");
                            } else {
                                XposedBridge.log("UserId=" + UserId);
                            }
                            if (TextUtils.isEmpty(UserAvatar)) {
                                XposedBridge.log("UserAvatar=");
                            } else {
                                XposedBridge.log("UserAvatar=" + UserAvatar);
                            }
                        }
                    });

            XposedBridge.hookAllMethods(XposedHelpers.findClass("com.alipay.android.phone.messageboxstatic.biz.dao.TradeDao", paramClassLoader), "insertMessageInfo", new XC_MethodHook() {
                protected void beforeHookedMethod(MethodHookParam paramAnonymousMethodHookParam)
                        throws Throwable {
                    try {
                        XposedBridge.log("======start=========");
                        String str2 = (String) XposedHelpers.callMethod(paramAnonymousMethodHookParam.args[0], "toString", new Object[0]);
                        XposedBridge.log(str2);
                        String str1 = StringUtils.getTextCenter(str2, "content='", "'");
                        if ((str1.contains("二维码收款")) || (str1.contains("收到一笔转账"))) {
                            Object localObject1 = new JSONObject(str1);
                            str1 = ((JSONObject) localObject1).getString("content").replace("￥", "");
                            localObject1 = ((JSONObject) localObject1).getString("assistMsg2");
                            str2 = StringUtils.getTextCenter(str2, "tradeNO=", "&");
                            Object localObject2 = new StringBuilder();
                            ((StringBuilder) localObject2).append("收到支付宝支付订单：");
                            ((StringBuilder) localObject2).append(str2);
                            ((StringBuilder) localObject2).append("==");
                            ((StringBuilder) localObject2).append(str1);
                            ((StringBuilder) localObject2).append("==");
                            ((StringBuilder) localObject2).append((String) localObject1);
                            XposedBridge.log(((StringBuilder) localObject2).toString());
                            localObject2 = new Intent();
                            ((Intent) localObject2).putExtra("bill_no", str2);
                            ((Intent) localObject2).putExtra("bill_money", str1);
                            ((Intent) localObject2).putExtra("bill_mark", (String) localObject1);
                            ((Intent) localObject2).putExtra("bill_type", "alipay");
                            ((Intent) localObject2).setAction(AliPayHook.BILLRECEIVED_ACTION);
                            paramContext.sendBroadcast((Intent) localObject2);
                        }
                        XposedBridge.log("======end=========");
                    } catch (Exception localException) {
                        XposedBridge.log(localException.getMessage());
                    }
                    super.beforeHookedMethod(paramAnonymousMethodHookParam);
                }
            });
            XposedHelpers.findAndHookMethod("com.alipay.mobile.payee.ui.PayeeQRSetMoneyActivity", paramClassLoader, "onCreate", new Object[]{Bundle.class, new XC_MethodHook() {
                protected void afterHookedMethod(MethodHookParam paramAnonymousMethodHookParam)
                        throws Throwable {
                    Object localObject1 = XposedHelpers.findField(paramAnonymousMethodHookParam.thisObject.getClass(), "b").get(paramAnonymousMethodHookParam.thisObject);
                    Object localObject2 = XposedHelpers.findField(paramAnonymousMethodHookParam.thisObject.getClass(), "c").get(paramAnonymousMethodHookParam.thisObject);
                    Intent localIntent = ((Activity) paramAnonymousMethodHookParam.thisObject).getIntent();
                    String str = localIntent.getStringExtra("mark");
                    if (str == null || str.equals("")) {
                        return;
                    }
                    XposedHelpers.callMethod(localObject1, "setText", new Object[]{localIntent.getStringExtra("money")});
                    XposedHelpers.callMethod(localObject2, "setText", new Object[]{str});
                    ((Button) XposedHelpers.findField(paramAnonymousMethodHookParam.thisObject.getClass(), "e").get(paramAnonymousMethodHookParam.thisObject)).performClick();
                }
            }});
            XposedHelpers.findAndHookMethod("com.alipay.mobile.payee.ui.PayeeQRSetMoneyActivity", paramClassLoader, "a", new Object[]{XposedHelpers.findClass("com.alipay.transferprod.rpc.result.ConsultSetAmountRes", paramClassLoader), new XC_MethodHook() {
                protected void afterHookedMethod(MethodHookParam paramAnonymousMethodHookParam)
                        throws Throwable {
                    String str1 = (String) XposedHelpers.findField(paramAnonymousMethodHookParam.thisObject.getClass(), "g").get(paramAnonymousMethodHookParam.thisObject);
                    String str2 = (String) XposedHelpers.callMethod(XposedHelpers.findField(paramAnonymousMethodHookParam.thisObject.getClass(), "c").get(paramAnonymousMethodHookParam.thisObject), "getUbbStr", new Object[0]);
                    Object object = paramAnonymousMethodHookParam.args[0];
                    String payurl = (String) XposedHelpers.findField(object.getClass(), "qrCodeUrl").get(object);
                    Object localObject = new StringBuilder();
                    ((StringBuilder) localObject).append(str1);
                    ((StringBuilder) localObject).append("  ");
                    ((StringBuilder) localObject).append(str2);
                    ((StringBuilder) localObject).append("  ");
                    ((StringBuilder) localObject).append(paramAnonymousMethodHookParam);
                    XposedBridge.log(((StringBuilder) localObject).toString());
                    XposedBridge.log("调用增加数据方法==>支付宝");
                    localObject = new Intent();
                    ((Intent) localObject).putExtra("money", str1);
                    ((Intent) localObject).putExtra("mark", str2);
                    ((Intent) localObject).putExtra("type", "alipay");
                    ((Intent) localObject).putExtra("payurl", payurl);
                    XposedBridge.log("payurl----------" + payurl);
                    if (str1.equals("") || str2.equals("")) {
                        return;
                    }
                    mark = str2;
                    ((Intent) localObject).setAction(AliPayHook.QRCODERECEIVED_ACTION);
                    paramContext.sendBroadcast((Intent) localObject);
                }
            }});

            XposedHelpers.findAndHookMethod(paramClassLoader.loadClass("com.alipay.mobile.rome.syncservice.b.c"), "b"
                    , new Object[]{byte[].class, new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            super.afterHookedMethod(param);
                            List list = (List) XposedHelpers.getObjectField(param.getResult(), "biz_sync_data");
                            Object localObject1 = new StringBuilder();
                            ((StringBuilder) localObject1).append("D_b payload biz_sync_data size:");
                            ((StringBuilder) localObject1).append(list.size());
                            XposedBridge.log(((StringBuilder) localObject1).toString());
                            localObject1 = list.get(0);
                            int str = (Integer) XposedHelpers.getObjectField(localObject1, "biz_type");
                            Object localObject2 = new StringBuilder();
                            ((StringBuilder) localObject2).append("D_b biz_type:");
                            ((StringBuilder) localObject2).append(str);
                            XposedBridge.log(((StringBuilder) localObject2).toString());
                            localObject1 = (List) XposedHelpers.getObjectField(localObject1, "oplog");
                            localObject2 = ((List) localObject1).get(0);
                            StringBuilder localStringBuilder = new StringBuilder();
                            localStringBuilder.append("D_b payload oplog size:");
                            localStringBuilder.append(((List) localObject1).size());
                            XposedBridge.log(localStringBuilder.toString());
                            getShowMsg((String) XposedHelpers.getObjectField(localObject2, "payload"), str, paramContext);
                            localObject1 = new StringBuilder();
                            ((StringBuilder) localObject1).append("D_b payload:\n");
                            ((StringBuilder) localObject1).append(XposedHelpers.getObjectField(localObject2, "payload"));
                            XposedBridge.log(((StringBuilder) localObject1).toString());
                        }
                    }});

            return;
        } catch (Error | Exception e) {
            e.printStackTrace();
        }
    }

    private String amount;
    private String userId;
    private String payerUserId;
    private String transferNo;
    private String state;

    public void getShowMsg(String paramString, int paramInt, final Context paramContext) {
        if (paramInt != 46) {
            if (paramInt == 180) {
//                XposedBridge.log("到这来了111");
//
//                Object localObject = new Intent();
//                ((Intent) localObject).putExtra("amount", "0.01");
//
//                if(amount != null){
//                    XposedBridge.log("amount:" + amount);
//                    XposedBridge.log("userId:" + userId);
//                    XposedBridge.log("transferNo:" + transferNo);
//                    XposedBridge.log("payerUserId:" + payerUserId);
//                }
//
//                ((Intent) localObject).putExtra("userId", userId);
//                ((Intent) localObject).putExtra("payerUserId", payerUserId);
//                ((Intent) localObject).putExtra("transferNo", transferNo);
//                ((Intent) localObject).putExtra("state", "1");
//                ((Intent) localObject).setAction(AliPayHook.PAYSTATERECEIVED_ACTION);
//                paramContext.sendBroadcast((Intent) localObject);
            }
        } else {
            try {

                Object localObject = new JSONObject(paramString);
                String str1 = (String) ((JSONObject) localObject).optString("state");
                String str2 = (String) ((JSONObject) localObject).optString("amount");
                String str3 = (String) ((JSONObject) localObject).optString("payerUserId");
                String str7 = (String) ((JSONObject) localObject).optString("userId");
                String str8 = (String) ((JSONObject) localObject).optString("transferNo");

                if (str1.equals("0")) {
                    localObject = new StringBuilder();
                    ((StringBuilder) localObject).append("支付中:");
                    ((StringBuilder) localObject).append(paramString);
                }
                //
                if (str1.equals("2") || str1.equals("1") ) {
                    XposedBridge.log("支付通知：---------" + str2 + "++" +str7 + "++" +str3 +"++" + "++" + str1);

                    localObject = new Intent();
                    ((Intent) localObject).putExtra("amount", str2);
                    ((Intent) localObject).putExtra("userId", str7);
                    ((Intent) localObject).putExtra("payerUserId", str3);
                    ((Intent) localObject).putExtra("transferNo", str8);
                    ((Intent) localObject).putExtra("state", str1);
                    ((Intent) localObject).setAction(AliPayHook.PAYSTATERECEIVED_ACTION);
                    paramContext.sendBroadcast((Intent) localObject);
                }

            } catch (Exception e) {
            }
        }
    }

}

