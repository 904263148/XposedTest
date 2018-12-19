package com.dktlh.ktl.xposedtest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import android.content.BroadcastReceiver;

import com.dktlh.ktl.xposedtest.event.StartAppEvent;
import com.dktlh.ktl.xposedtest.utils.Tools;

import org.greenrobot.eventbus.EventBus;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        StartAppEvent startAppEvent = new StartAppEvent();
        int uid = Tools.getPackageUid(context, "com.eg.android.AlipayGphone");
        if(uid > 0){
            boolean rstA = Tools.isAppRunning(context, "com.eg.android.AlipayGphone");
            boolean rstB = Tools.isProcessRunning(context, uid);
            if(rstA||rstB){
                //指定包名的程序正在运行中
                startAppEvent.setType(1);
                startAppEvent.setState(1);
            }else{
                //指定包名的程序未在运行中
                startAppEvent.setType(1);
                startAppEvent.setState(0);
            }
            EventBus.getDefault().post(startAppEvent);
        } else{
            //应用未安装
        }

        StartAppEvent startAppEvent2 = new StartAppEvent();
        int uid2 = Tools.getPackageUid(context, "com.tencent.mm");
        if(uid2 > 0){
            boolean rstA = Tools.isAppRunning(context, "com.tencent.mm");
            boolean rstB = Tools.isProcessRunning(context, uid2);
            if(rstA||rstB){
                //指定包名的程序正在运行中

                startAppEvent2.setType(0);
                startAppEvent2.setState(1);
            }else{
                //指定包名的程序未在运行中
                startAppEvent2.setType(0);
                startAppEvent2.setState(0);
            }
            EventBus.getDefault().post(startAppEvent2);
        } else{
            //应用未安装
        }
    }
}
