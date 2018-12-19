package com.dktlh.ktl.xposedtest.alipayhk.v59.anti;

import de.robv.android.xposed.XC_MethodHook;

public class XpExceptionCatch extends XC_MethodHook {
    @Override
    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
        param.setResult(false);
    }
}
