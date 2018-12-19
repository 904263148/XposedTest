package com.dktlh.ktl.xposedtest;

import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.Toast;

import com.dktlh.ktl.xposedtest.utils.XmlToJson;

import org.json.JSONObject;

import java.lang.reflect.Method;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

public class WechatHook {
    public static String BILLRECEIVED_ACTION = "com.hamancom.hmpayhelp.billreceived";
    public static String QRCODERECEIVED_ACTION = "com.hamancom.hmpayhelp.qrcodereceived";

    protected void hook(final ClassLoader paramClassLoader, final Context paramContext) {

        XposedHelpers.findAndHookMethod("com.tencent.wcdb.database.SQLiteDatabase", paramClassLoader, "insert", new Object[]{String.class, String.class, ContentValues.class, new XC_MethodHook() {
            protected void afterHookedMethod(MethodHookParam paramAnonymousMethodHookParam)
                    throws Throwable {
            }

            protected void beforeHookedMethod(MethodHookParam paramAnonymousMethodHookParam)
                    throws Throwable {
                try {
                    Object localObject1 = (ContentValues) paramAnonymousMethodHookParam.args[2];
                    String s = (String) paramAnonymousMethodHookParam.args[0];
                    if (!TextUtils.isEmpty(s)) {
                        if (!s.equals("message")) {
                            return;
                        }
                        Integer type = ((ContentValues) localObject1).getAsInteger("type");
                        if (type == null) {
                            return;
                        }
                        if (type.intValue() == 318767153) {
                            XposedBridge.log("======start=========");
                            XposedBridge.log(((ContentValues) localObject1).getAsString("content"));
                          Object localObject2 = new XmlToJson.Builder(((ContentValues)localObject1).getAsString("content")).build().getJSONObject("msg");
                          XposedBridge.log(((JSONObject)localObject2).toString());
                          String s1 = ((JSONObject)localObject2).getJSONObject("appmsg").getJSONObject("mmreader").getJSONObject("template_detail").getJSONObject("line_content").getJSONObject("topline").getJSONObject("value").getString("word").replace("￥", "");
                          localObject1 = ((JSONObject)localObject2).getJSONObject("appmsg").getJSONObject("mmreader").getJSONObject("template_detail").getJSONObject("line_content").getJSONObject("lines").getJSONArray("line").getJSONObject(0).getJSONObject("value").getString("word");
                          localObject2 = ((JSONObject)localObject2).getJSONObject("appmsg").getString("template_id");
                          Object localObject3 = new StringBuilder();
                          ((StringBuilder)localObject3).append("收到微信支付订单：");
                          ((StringBuilder)localObject3).append((String)localObject2);
                          ((StringBuilder)localObject3).append("==");
                          ((StringBuilder)localObject3).append(s1);
                          ((StringBuilder)localObject3).append("==");
                          ((StringBuilder)localObject3).append((String)localObject1);
                          XposedBridge.log(((StringBuilder)localObject3).toString());
                          localObject3 = new Intent();
                          ((Intent)localObject3).putExtra("bill_no", (String)localObject2);
                          ((Intent)localObject3).putExtra("bill_money", s1);
                          ((Intent)localObject3).putExtra("bill_mark", (String)localObject1);
                          ((Intent)localObject3).putExtra("bill_type", "wechat");
                          ((Intent)localObject3).setAction(WechatHook.BILLRECEIVED_ACTION);
                          paramContext.sendBroadcast((Intent)localObject3);
                            XposedBridge.log("======end=========");
                        }
                        return;
                    }
                    return;
                } catch (Exception e) {
                    XposedBridge.log(e.getMessage());
                }
            }
        }});
        try {
            XposedBridge.hookAllMethods(XposedHelpers.findClass("com.tencent.mm.plugin.collect.b.s", paramClassLoader), "a", new XC_MethodHook() {
                protected void afterHookedMethod(MethodHookParam paramAnonymousMethodHookParam)
                        throws Throwable {
                    double d = ((Double) XposedHelpers.findField(paramAnonymousMethodHookParam.thisObject.getClass(), "hUL").get(paramAnonymousMethodHookParam.thisObject)).doubleValue();
                    String str = (String) XposedHelpers.findField(paramAnonymousMethodHookParam.thisObject.getClass(), "desc").get(paramAnonymousMethodHookParam.thisObject);
                    String s = (String) XposedHelpers.findField(paramAnonymousMethodHookParam.thisObject.getClass(), "hUK").get(paramAnonymousMethodHookParam.thisObject);
                    Object localObject = new StringBuilder();
                    ((StringBuilder) localObject).append(d);
                    ((StringBuilder) localObject).append("  ");
                    ((StringBuilder) localObject).append(str);
                    ((StringBuilder) localObject).append("  ");
                    ((StringBuilder) localObject).append(s);
                    XposedBridge.log("----" + ((StringBuilder) localObject).toString());
                    XposedBridge.log("调用增加数据方法==>微信");
                    localObject = new Intent();
                    StringBuilder localStringBuilder = new StringBuilder();
                    localStringBuilder.append(d);
                    localStringBuilder.append("");
                    ((Intent) localObject).putExtra("money", localStringBuilder.toString());
                    ((Intent) localObject).putExtra("mark", str);
                    ((Intent) localObject).putExtra("type", "wechat");
                    ((Intent) localObject).putExtra("payurl", s);
                    XposedBridge.log("str = " + str);
                    XposedBridge.log("payurl = " + s);
                    if (localStringBuilder.toString().equals("") || str.equals("")) {
                        return;
                    }
                    ((Intent) localObject).setAction(WechatHook.QRCODERECEIVED_ACTION);
                    paramContext.sendBroadcast((Intent) localObject);

//                    Activity activity = (Activity) paramAnonymousMethodHookParam.thisObject;
//                    Intent intent  = new Intent();
//                    intent.setClassName("com.dktlh.ktl.xposedtest","com.dktlh.ktl.xposedtest.MainActivity");
//                    activity.startActivity(intent);
                }

                protected void beforeHookedMethod(MethodHookParam paramAnonymousMethodHookParam)
                        throws Throwable {
                }
            });
        } catch (Exception e) {
        }
        try {
            XposedHelpers.findAndHookMethod("com.tencent.mm.plugin.collect.ui.CollectCreateQRCodeUI", paramClassLoader, "initView", new Object[]{new XC_MethodHook() {

                protected void afterHookedMethod(MethodHookParam paramAnonymousMethodHookParam)
                        throws Throwable {
                    XposedBridge.log("Hook微信开始......");
                    Object localObject1 = ((Activity) paramAnonymousMethodHookParam.thisObject).getIntent();
                    for (String key:
                            ((Intent) localObject1).getExtras().keySet()) {
                        XposedBridge.log("paramClassLoader = " + key + "///value = " + ((Intent) localObject1).getStringExtra(key));
                    }
                    String str = ((Intent) localObject1).getStringExtra("mark");
                    localObject1 = ((Intent) localObject1).getStringExtra("money");
                    if (str == null || str.equals("") || ((String) localObject1) == null || ((String) localObject1).equals("")) {
                        return;
                    }
//                    Method[] methods = paramAnonymousMethodHookParam.thisObject.getClass().getMethods();
//                    for (Method method: methods) {
//                        XposedBridge.log(paramAnonymousMethodHookParam.thisObject.getClass() + "---方法名---" + method.getName());
//                    }
                    Object localObject2 = XposedHelpers.findField(paramAnonymousMethodHookParam.thisObject.getClass(), "hXD").get(paramAnonymousMethodHookParam.thisObject);
                    XposedHelpers.callMethod(XposedHelpers.findField(XposedHelpers.findClass("com.tencent.mm.wallet_core.ui.formview.WalletFormView", paramClassLoader), "uZy").get(localObject2), "setText", new Object[]{localObject1});
                    localObject1 = XposedHelpers.findClass("com.tencent.mm.plugin.collect.ui.CollectCreateQRCodeUI", paramClassLoader);
                    XposedHelpers.callStaticMethod((Class) localObject1, "a", new Object[]{paramAnonymousMethodHookParam.thisObject, str});
                    XposedHelpers.callStaticMethod((Class) localObject1, "c", new Object[]{paramAnonymousMethodHookParam.thisObject});
                    ((Button) XposedHelpers.callMethod(paramAnonymousMethodHookParam.thisObject, "findViewById", new Object[]{Integer.valueOf(2131756838)})).performClick();

                }

                protected void beforeHookedMethod(MethodHookParam paramAnonymousMethodHookParam)
                        throws Throwable {
                }
            }});
            return;
        } catch (Exception e) {
        }
    }
}

