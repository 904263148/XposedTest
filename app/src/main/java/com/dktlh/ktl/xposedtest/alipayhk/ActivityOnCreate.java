package com.dktlh.ktl.xposedtest.alipayhk;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.util.Log;

import com.dktlh.ktl.xposedtest.alipayhk.v59.Hook;
import com.dktlh.ktl.xposedtest.tools.Tool;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;

public class ActivityOnCreate extends XC_MethodHook {
    @Override
    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
        Activity activity=(Activity) param.thisObject;
        String scn=activity.getClass().getSimpleName();
        if(scn.equals("AlipayLogin"))
        {
            hookAlipayLogin(activity);
            XposedBridge.log("进来了-----------------");
        }
    }

    void hookAlipayLogin(Activity activity)
    {
        try {
            PackageManager pm= activity.getPackageManager();
            int versionCode=pm.getPackageInfo(activity.getPackageName(),PackageManager.GET_INSTRUMENTATION).versionCode;
            switch (versionCode)
            {
                case 59:
                    Hook.mutilDexHook(activity);
                    break;
            }
        }catch (Exception e)
        {
            Tool.printException(e);
        }


    }
}
