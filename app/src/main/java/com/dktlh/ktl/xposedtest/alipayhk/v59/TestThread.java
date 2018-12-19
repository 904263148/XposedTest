package com.dktlh.ktl.xposedtest.alipayhk.v59;

import android.content.Context;

import com.dktlh.ktl.xposedtest.tools.Tool;

public class TestThread implements Runnable {
    ClassLoader classLoader;
    String money;
    String mark;
    Context context;

    public  TestThread(ClassLoader classLoader)
    {
        this.classLoader=classLoader;
    }

    public  TestThread(ClassLoader classLoader , String money , String mark , Context context)
    {
        this.classLoader=classLoader;
        this.money =money;
        this.mark = mark;
        this.context = context;
    }
    @Override
    public void run() {
        try {
            Thread.sleep(1000);
            Tools.getQRCode(money,mark,classLoader , context);
        }catch (Exception e)
        {
            Tool.printException(e);
        }
    }
}
