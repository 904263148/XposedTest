package com.dktlh.ktl.xposedtest;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dktlh.ktl.xposedtest.alipayhk.ActivityOnCreate;
import com.dktlh.ktl.xposedtest.tools.AnyMethodHook;
import com.google.zxing.common.BitMatrix;

import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Created by dengkaitao on 2018/6/30 19:43.
 * Email：724279138@qq.com
 */
public class Main implements IXposedHookLoadPackage {
    private final String ALIPAY_PACKAGE = "com.eg.android.AlipayGphone";
    private boolean ALIPAY_PACKAGE_ISHOOK = false;
    private final String WECHAT_PACKAGE = "com.tencent.mm";
    private boolean WECHAT_PACKAGE_ISHOOK = false;
    private final String QQ_PACKAGE = "com.tencent.mobileqq";
    private boolean QQ_PACKAGE_ISHOOK = false;
    private final String ALIPAYHK_PACKAGE = "hk.alipay.wallet";
    private boolean ALIPAYHK_PACKAGE_ISHOOK = false;

    static String strClassName = "";

    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam paramLoadPackageParam)
            throws Throwable {
        XposedBridge.log("Loaded app: " + paramLoadPackageParam.packageName);
        if (paramLoadPackageParam.appInfo != null) {
            if ((paramLoadPackageParam.appInfo.flags & 0x81) != 0) {
                return;
            }
            final String str = paramLoadPackageParam.packageName;
            final String processName = paramLoadPackageParam.processName;
            StringBuilder localStringBuilder = new StringBuilder();
            localStringBuilder.append("lpparam.packageName=");
            localStringBuilder.append(str);
            XposedBridge.log(localStringBuilder.toString());
            localStringBuilder = new StringBuilder();
            localStringBuilder.append("lpparam.processName=");
            localStringBuilder.append(processName);
            XposedBridge.log(localStringBuilder.toString());

            if ("com.tencent.mm".equals(str)) {
                try {
                    XposedHelpers.findAndHookMethod(ContextWrapper.class, "attachBaseContext", new Object[]{Context.class, new XC_MethodHook() {

                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            super.beforeHookedMethod(param);
                            XposedBridge.log("beforeHookedMethod = " );
                        }

                        protected void afterHookedMethod(XC_MethodHook.MethodHookParam paramAnonymousMethodHookParam)
                                throws Throwable {
                            super.afterHookedMethod(paramAnonymousMethodHookParam);

                            Activity activity=(Activity) paramAnonymousMethodHookParam.thisObject;
                            PackageManager pm= activity.getPackageManager();
                            int versionCode=pm.getPackageInfo(activity.getPackageName(),PackageManager.GET_INSTRUMENTATION).versionCode;
                            XposedBridge.log("wechat_version-----" + versionCode);

                            XposedBridge.log("paramLoadPackageParam = " + str);
                            Context context = (Context) paramAnonymousMethodHookParam.args[0];
                            ClassLoader localClassLoader = context.getClassLoader();
                            if (("com.tencent.mm".equals(processName)) && (!Main.this.WECHAT_PACKAGE_ISHOOK)) {
                                Object localObject = new StartWechatReceived();
                                IntentFilter localIntentFilter = new IntentFilter();
                                localIntentFilter.addAction("com.payhelper.wechat.start");
                                context.registerReceiver((BroadcastReceiver) localObject, localIntentFilter);
                                localObject = new StringBuilder();
                                ((StringBuilder) localObject).append("handleLoadPackage: ");
                                ((StringBuilder) localObject).append(str);
                                XposedBridge.log("获取到wechat=>>classloader" + ((StringBuilder) localObject).toString());
                                Toast.makeText(context, "获取到wechat=>>classloader", Toast.LENGTH_LONG).show();
                                new WechatHook().hook(localClassLoader, context);
                                WECHAT_PACKAGE_ISHOOK = true;
                            }
                        }
                    }});
                } catch (Throwable e) {
                    XposedBridge.log(e);
                }
                return;
            }
            if ("com.eg.android.AlipayGphone".equals(str)) {
                try {
                    XposedHelpers.findAndHookMethod(Application.class, "attach", new Object[]{Context.class, new XC_MethodHook() {
                        protected void afterHookedMethod(XC_MethodHook.MethodHookParam paramAnonymousMethodHookParam)
                                throws Throwable {
                            super.afterHookedMethod(paramAnonymousMethodHookParam);

//                            Class<?> cls = (Class<?>) paramAnonymousMethodHookParam.getResult();
//                            String name = cls.getName();
//                            XposedBridge.log("cls.getName() = " + name);

                            Context context = (Context) paramAnonymousMethodHookParam.args[0];
                            ClassLoader localClassLoader = context.getClassLoader();
                            if (("com.eg.android.AlipayGphone".equals(processName)) && (!Main.this.ALIPAY_PACKAGE_ISHOOK)) {
                                Object localObject = new StartAlipayReceived();
                                IntentFilter localIntentFilter = new IntentFilter();
                                localIntentFilter.addAction("com.payhelper.alipay.start");
                                context.registerReceiver((BroadcastReceiver) localObject, localIntentFilter);
                                localObject = new StringBuilder();
                                ((StringBuilder) localObject).append("handleLoadPackage: ");
                                ((StringBuilder) localObject).append(str);
                                XposedBridge.log(((StringBuilder) localObject).toString());
                                Toast.makeText(context, "获取到alipay=>>classloader", Toast.LENGTH_LONG).show();
                                new AliPayHook().hook(localClassLoader, context);
                                ALIPAY_PACKAGE_ISHOOK = true;
                            }
                        }
                    }});
                    return;
                } catch (Throwable e) {
                    XposedBridge.log(e);
                }
            }
        }
    }

    void hookAlipayHK(XC_LoadPackage.LoadPackageParam lp)
    {

    }

}
