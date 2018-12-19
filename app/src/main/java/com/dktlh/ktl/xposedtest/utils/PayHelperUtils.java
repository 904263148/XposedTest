package com.dktlh.ktl.xposedtest.utils;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;

import com.dktlh.ktl.xposedtest.CustomApplcation;
import com.dktlh.ktl.xposedtest.Main2Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dengkaitao on 2018/6/30 18:48.
 * Email：724279138@qq.com
 */
public class PayHelperUtils {
    public static String ALIPAYSTART_ACTION;
    public static String MSGRECEIVED_ACTION;
    public static String WECHATSTART_ACTION = "com.payhelper.wechat.start";
    public static List<OrderBean> orderBeans = new ArrayList();
    public static List<QrCodeBean> qrCodeBeans;

    public static String QQSTART_ACTION = "com.payhelper.qq.start";

    public static String ALIPAYHK_ACTION = "com.payhelper.alipayhk.start";

    static
    {
        ALIPAYSTART_ACTION = "com.payhelper.alipay.start";
        MSGRECEIVED_ACTION = "com.hamancom.hmpayhelp.msgreceived";
        qrCodeBeans = new ArrayList();
    }

    public static boolean isAppRunning(Context paramContext, String paramString)
    {
        @SuppressLint("WrongConstant")
        List<ActivityManager.RunningTaskInfo> runningTaskInfoList = ((ActivityManager)paramContext.getSystemService("activity")).getRunningTasks(100);
        if (runningTaskInfoList.size() <= 0) {
            return false;
        }
        while (runningTaskInfoList.iterator().hasNext()) {
            if (((ActivityManager.RunningTaskInfo)runningTaskInfoList.iterator().next()).baseActivity.getPackageName().equals(paramString)) {
                return true;
            }
        }
        return false;
    }

    public static void sendAppMsg(String paramString1, String paramString2, String paramString3, Context paramContext)
    {
        Intent localIntent = new Intent();
        if (paramString3.equals("al")) {
            localIntent.setAction(ALIPAYSTART_ACTION);
        } else if (paramString3.equals("wx")) {
            localIntent.setAction(WECHATSTART_ACTION);
        }else if (paramString3.equals("qq")){
            localIntent.setAction(QQSTART_ACTION);
        }else if (paramString3.equals("alipayhk")){
            localIntent.setAction(ALIPAYHK_ACTION);
        }
        localIntent.putExtra("mark", paramString2);
        localIntent.putExtra("money", paramString1);
        paramContext.sendBroadcast(localIntent);
    }

    @SuppressLint("WrongConstant")
    public static void startAPP()
    {
        try
        {
            Intent localIntent = new Intent(CustomApplcation.getInstance().getApplicationContext(), Main2Activity.class);
            localIntent.addFlags(128);
            CustomApplcation.getInstance().getApplicationContext().startActivity(localIntent);
            return;
        }
        catch (Exception localException) {}
    }

    public static void startAPP(Context paramContext, String paramString)
    {
        try
        {
            paramContext.startActivity(paramContext.getPackageManager().getLaunchIntentForPackage(paramString));
            return;
        }
        catch (Exception e) {}
    }
}
