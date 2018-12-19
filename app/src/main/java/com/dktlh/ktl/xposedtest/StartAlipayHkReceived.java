package com.dktlh.ktl.xposedtest;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.dktlh.ktl.xposedtest.alipayhk.v59.TestThread;
import com.dktlh.ktl.xposedtest.alipayhk.v59.Tools;
import com.dktlh.ktl.xposedtest.tools.Tool;

import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

/**
* Created by dengkaitao on 2018/6/30 20:29.
* Email：724279138@qq.com
*/
class StartAlipayHkReceived
        extends BroadcastReceiver
{
    StartAlipayHkReceived() {}

    @SuppressLint("WrongConstant")
    public void onReceive(final Context paramContext, final Intent paramIntent)
    {
        XposedBridge.log("启动支付宝HKActivity");
        try {
            Intent localIntent = new Intent(paramContext, XposedHelpers.findClass("hk.alipay.wallet.payee.ui.PayeeQRActivity", paramContext.getClassLoader()));
            localIntent.putExtra("mark", paramIntent.getStringExtra("mark"));
            localIntent.putExtra("money", paramIntent.getStringExtra("money"));
            localIntent.addFlags(268435456);
            paramContext.startActivity(localIntent);
            XposedBridge.log("启动支付宝HK成功");

            new Thread(new TestThread(paramContext.getClassLoader() ,paramIntent.getStringExtra("money") , paramIntent.getStringExtra("mark") ,paramContext)).start();

//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    Tools.getQRCode(paramIntent.getStringExtra("money"),paramIntent.getStringExtra("mark"),paramContext.getClassLoader() , paramContext);
//                }
//            });

        } catch (Exception e) {
            StringBuilder sb = new StringBuilder();
            sb.append("启动支付宝HK失败：");
            sb.append(e.getMessage());
            XposedBridge.log(sb.toString());
        }
    }
}
