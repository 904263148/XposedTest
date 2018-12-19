package com.dktlh.ktl.xposedtest;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.dktlh.ktl.xposedtest.utils.Tools;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

/**
* Created by dengkaitao on 2018/6/30 20:29.
* Email：724279138@qq.com
*/
class StartQQReceived
        extends BroadcastReceiver
{
    StartQQReceived() {}

    @SuppressLint("WrongConstant")
    public void onReceive(Context paramContext, Intent paramIntent)
    {
        XposedBridge.log("启动QQActivity");
        try {

            SharedPreferences sharedPreferences = paramContext.getSharedPreferences("qrcode", Context.MODE_PRIVATE); //私有数据
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("money" , paramIntent.getStringExtra("money"));
            editor.putString("mark" , paramIntent.getStringExtra("mark"));
            editor.putBoolean("intentParams" , true);
            editor.apply();

            //   com.tenpay.sdk.activity.QrcodeSettingActivity
            Intent localIntent = new Intent(paramContext, XposedHelpers.findClass("cooperation.qwallet.plugin.QWalletPluginProxyActivity", paramContext.getClassLoader()));

            localIntent.putExtra("mark", paramIntent.getStringExtra("mark"));
            localIntent.putExtra("money", paramIntent.getStringExtra("money"));
            localIntent.addFlags(268435456);
            paramContext.startActivity(localIntent);

            XposedBridge.log("启动QQ成功");
            return;
        } catch (Exception e) {
            StringBuilder sb = new StringBuilder();
            sb.append("启动QQ失败：");
            sb.append(e.getMessage());
            XposedBridge.log(sb.toString());
        }
    }

}
