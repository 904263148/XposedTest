package com.dktlh.ktl.xposedtest;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import com.dktlh.ktl.xposedtest.event.StartAppEvent;

import org.greenrobot.eventbus.EventBus;

import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

/**
* Created by dengkaitao on 2018/6/30 20:29.
* Email：724279138@qq.com
*/
class StartWechatReceived extends BroadcastReceiver {
    StartWechatReceived() {}

    public void onReceive(final Context paramContext, final Intent paramIntent)
    {
        XposedBridge.log("启动微信Activity");
        try
        {
            Intent localIntent = new Intent(paramContext, XposedHelpers.findClass("com.tencent.mm.plugin.collect.ui.CollectCreateQRCodeUI", paramContext.getClassLoader()));
            localIntent.putExtra("mark", paramIntent.getStringExtra("mark"));
            localIntent.putExtra("money", paramIntent.getStringExtra("money"));
            localIntent.addFlags(268435456);
            paramContext.startActivity(localIntent);
            XposedBridge.log("启动微信成功");
            return;
        }
        catch (Exception e)
        {
            StringBuilder sb = new StringBuilder();
            sb.append("启动微信失败：");
            sb.append(e.getMessage());
            XposedBridge.log(paramIntent.toString());
        }
    }
}
