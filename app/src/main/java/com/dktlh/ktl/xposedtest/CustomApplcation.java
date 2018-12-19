package com.dktlh.ktl.xposedtest;

import android.app.Application;
import android.content.Context;

/**
 * Created by dengkaitao on 2018/6/30 19:08.
 * Email：724279138@qq.com
 */
public class CustomApplcation extends Application {
    private static Context context;
    public static CustomApplcation mInstance;

    public static Context getContext()
    {
        return context;
    }

    public static CustomApplcation getInstance()
    {
        return mInstance;
    }

    public void onCreate()
    {
        super.onCreate();
        context = getApplicationContext();
        mInstance = this;
    }
}
