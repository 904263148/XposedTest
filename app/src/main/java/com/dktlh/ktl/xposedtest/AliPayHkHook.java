package com.dktlh.ktl.xposedtest;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.dktlh.ktl.xposedtest.alipayhk.ActivityOnCreate;
import com.dktlh.ktl.xposedtest.alipayhk.v59.TestThread;
import com.dktlh.ktl.xposedtest.utils.StringUtils;

import org.json.JSONObject;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

public class AliPayHkHook
{
  public static String BILLRECEIVED_ACTION = "com.hamancom.hmpayhelp.billreceived";
  public static String QRCODERECEIVED_ACTION = "com.hamancom.hmpayhelp.qrcodereceived";

  public void hook(final ClassLoader paramClassLoader, final Context paramContext)
  {

    XposedHelpers.findAndHookMethod(Activity.class,
            "onCreate",Bundle.class,new ActivityOnCreate());
    XposedBridge.log("alipayHK is hook");

    XposedHelpers.findAndHookMethod("hk.alipay.wallet.payee.ui.PayeeQRActivity", paramClassLoader, "onCreate", new Object[] { Bundle.class, new XC_MethodHook()
    {
      protected void afterHookedMethod(MethodHookParam paramAnonymousMethodHookParam)
              throws Throwable
      {
        Activity activity=(Activity) paramAnonymousMethodHookParam.thisObject;
        XposedBridge.log("Hook支付宝港版开始......");
        Intent localIntent = ((Activity)paramAnonymousMethodHookParam.thisObject).getIntent();
        String str = localIntent.getStringExtra("mark");
        String money = localIntent.getStringExtra("money");
        if(money == null || "".equals(money)){
          return;
        }
        XposedBridge.log("money--------------------"+money);
        new Thread(new TestThread(activity.getClassLoader() ,money , str ,paramContext)).start();

      }
    } });

  }
}

