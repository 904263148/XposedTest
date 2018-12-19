package com.dktlh.ktl.xposedtest.tools;

import java.io.PrintWriter;
import java.io.StringWriter;

import de.robv.android.xposed.XposedBridge;

public class Tool {

    public static  void  printException(Exception e)
    {
        StringWriter sw=new StringWriter();
        PrintWriter pw=new PrintWriter(sw);
        e.printStackTrace(pw);
        XposedBridge.log(sw.toString());
    }
}
