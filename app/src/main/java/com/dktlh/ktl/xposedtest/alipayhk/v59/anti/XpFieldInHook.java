package com.dktlh.ktl.xposedtest.alipayhk.v59.anti;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;

public class XpFieldInHook extends XC_MethodHook {
    @Override
    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
        String m=param.getResult().toString();
        XposedBridge.log("XpFieldInHook m:"+m);
        param.setResult("");
    }
}
