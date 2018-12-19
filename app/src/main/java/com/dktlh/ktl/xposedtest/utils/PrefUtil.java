package com.dktlh.ktl.xposedtest.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PrefUtil {

    private static final String NAME = "name";
    private static final String PWD = "pwd";
    private static final String ALIPAY_USERID = "alipayUserId";

    public static void setName(Context context, String name) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putString(NAME, name).apply();
    }

    public static String getName(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getString(NAME, "");
    }

    public static void setPwd(Context context, String pwd) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putString(PWD, pwd).apply();
    }

    public static String getPwd(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getString(PWD, "");
    }

    public static void setAlipayUserid(Context context, String alipayUserId) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putString(ALIPAY_USERID, alipayUserId).apply();
    }

    public static String getAlipayUserid(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getString(ALIPAY_USERID, "");
    }

}
