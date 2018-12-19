package com.dktlh.ktl.xposedtest.alipayhk.v59;

import android.content.Intent;
import android.widget.Toast;

import java.io.Serializable;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

public class PayeeQRActivity_onActivityResult extends XC_MethodHook {
    @Override
    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
        Intent intent=(Intent) param.args[2];
        Serializable v0_1 = intent.getSerializableExtra("resultData");
       String amount= (String) XposedHelpers.getObjectField(v0_1,"amount");
        String perAmount= (String)XposedHelpers.getObjectField(v0_1,"perAmount");
        String peopleCount= (String)XposedHelpers.getObjectField(v0_1,"peopleCount");
        String memo= (String)XposedHelpers.getObjectField(v0_1,"memo");
        String qrCode= (String)XposedHelpers.getObjectField(v0_1,"qrCode");
        String qrCodeOffline= (String)XposedHelpers.getObjectField(v0_1,"qrCodeOffline");
        XposedBridge.log("PayeeQRActivity_onActivityResult amount:"+amount);
        XposedBridge.log("PayeeQRActivity_onActivityResult perAmount:"+perAmount);
        XposedBridge.log("PayeeQRActivity_onActivityResult peopleCount:"+peopleCount);
        XposedBridge.log("PayeeQRActivity_onActivityResult memo:"+memo);
        XposedBridge.log("PayeeQRActivity_onActivityResult qrCode:"+qrCode);
        XposedBridge.log("PayeeQRActivity_onActivityResult qrCodeOffline:"+qrCodeOffline);
    }
}
