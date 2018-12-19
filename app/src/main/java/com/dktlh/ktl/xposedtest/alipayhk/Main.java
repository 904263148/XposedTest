package com.dktlh.ktl.xposedtest.alipayhk;

import android.app.Activity;
import android.os.Bundle;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class Main implements IXposedHookLoadPackage {


    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lp) throws Throwable {
        if(lp.packageName.equals("hk.alipay.wallet"))
        {
            hookAlipayHK(lp);
        }
    }

//    void hookWeChat(XC_LoadPackage.LoadPackageParam lp)
//    {
//        XposedHelpers.findAndHookMethod(Activity.class,"onCreate",Bundle.class,new com.hook.wechat.ActivityOnCreate());
//        XposedBridge.log("wechat is hook");
//    }
//    void hookAlipay(XC_LoadPackage.LoadPackageParam lp)
//    {
//        XposedHelpers.findAndHookMethod(Activity.class,"onCreate",Bundle.class,new ActivityOnCreate());
//        XposedBridge.log("alipay is hook");
//    }

    void hookAlipayHK(XC_LoadPackage.LoadPackageParam lp)
    {
        XposedHelpers.findAndHookMethod(Activity.class,
                "onCreate",Bundle.class,new ActivityOnCreate());
        XposedBridge.log("alipayHK is hook");
    }
}
