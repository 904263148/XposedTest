package com.dktlh.ktl.xposedtest.alipayhk.v59;

import android.app.Activity;
import android.content.Context;

import com.dktlh.ktl.xposedtest.alipayhk.v59.anti.CI_a;
import com.dktlh.ktl.xposedtest.alipayhk.v59.anti.CI_a2;
import com.dktlh.ktl.xposedtest.alipayhk.v59.anti.ScanPackage;
import com.dktlh.ktl.xposedtest.alipayhk.v59.anti.XpExceptionCatch;
import com.dktlh.ktl.xposedtest.alipayhk.v59.anti.XpFieldInHook;
import com.dktlh.ktl.xposedtest.alipayhk.v59.anti.XpMethodInHook;
import com.dktlh.ktl.xposedtest.tools.Tool;

import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

public class Hook {
    public static void mutilDexHook(Activity activity)
    {
        try {
            ClassLoader classLoader=activity.getClassLoader();
            Class PayeeQRActivity=classLoader.loadClass("hk.alipay.wallet.payee.ui.PayeeQRActivity");
            XposedBridge.hookAllMethods(PayeeQRActivity,"onActivityResult",new PayeeQRActivity_onActivityResult());
            XposedBridge.log("alipayHK 59 is hook");

//            new Thread(new TestThread(classLoader)).start();
            antiAliScan(classLoader);
        }catch (Exception e)
        {
            Tool.printException(e);
        }
    }

    static  void antiAliScan(ClassLoader classLoader)
    {
        try {
            Class ScanAttack=classLoader.loadClass("com.alipay.apmobilesecuritysdk.scanattack.common.ScanAttack");
            XposedHelpers.findAndHookMethod(ScanAttack,"scanPackage",Context.class,String.class,new ScanPackage());
            XposedHelpers.findAndHookMethod(ScanAttack,"xpExceptionCatch",Context.class,new XpExceptionCatch());
            XposedHelpers.findAndHookMethod(ScanAttack,"xpFieldInHook",Context.class,new XpFieldInHook());
            XposedHelpers.findAndHookMethod(ScanAttack,"xpMethodInHook",Context.class,new XpMethodInHook());
            Class CI=classLoader.loadClass("com.alipay.mobile.base.security.CheckInject");
            XposedHelpers.findAndHookMethod(CI,"a",new CI_a());//检测是否ROOT
            XposedHelpers.findAndHookMethod(CI,"a",ClassLoader.class,String.class,new CI_a2());//检测是否被XPOSED HOOK



        }catch (Exception e)
        {
            Tool.printException(e);
        }
    }
}
