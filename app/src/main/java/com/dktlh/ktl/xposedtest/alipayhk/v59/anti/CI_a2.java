package com.dktlh.ktl.xposedtest.alipayhk.v59.anti;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;

public class CI_a2 extends XC_MethodHook {
    @Override
    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
        XposedBridge.log("CI_a2 "+param.args[1]);
    }
}
