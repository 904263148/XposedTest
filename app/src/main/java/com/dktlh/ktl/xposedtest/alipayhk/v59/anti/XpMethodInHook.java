package com.dktlh.ktl.xposedtest.alipayhk.v59.anti;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;

public class XpMethodInHook extends XC_MethodHook {
    @Override
    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
        if(param.getResult()==null)
        {
            return;
        }
        String m=param.getResult().toString();
        XposedBridge.log("XpMethodInHook m:"+m);
        param.setResult(null);
    }
}
