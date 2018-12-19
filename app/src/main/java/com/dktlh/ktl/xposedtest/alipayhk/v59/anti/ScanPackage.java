package com.dktlh.ktl.xposedtest.alipayhk.v59.anti;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;

public class ScanPackage extends XC_MethodHook {
    @Override
    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
        String pn=(String) param.args[1];
        XposedBridge.log("ScanPackage pn:"+pn);
        if(pn.contains("substrate"))
        {
            param.setResult(false);

        }

        if(pn.contains("xposed"))
        {
            param.setResult(false);

        }
    }
}
