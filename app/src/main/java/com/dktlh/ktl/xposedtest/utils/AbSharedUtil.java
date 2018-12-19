package com.dktlh.ktl.xposedtest.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class AbSharedUtil
{
  private static final String SHARED_PATH = "bbplayer";
  
  public static boolean getBoolean(Context paramContext, String paramString, boolean paramBoolean)
  {
    return getDefaultSharedPreferences(paramContext).getBoolean(paramString, paramBoolean);
  }
  
  public static SharedPreferences getDefaultSharedPreferences(Context paramContext)
  {
    return paramContext.getSharedPreferences("bbplayer", 0);
  }
  
  public static int getInt(Context paramContext, String paramString)
  {
    return getDefaultSharedPreferences(paramContext).getInt(paramString, 0);
  }
  
  public static String getString(Context paramContext, String paramString)
  {
    return getDefaultSharedPreferences(paramContext).getString(paramString, null);
  }
  
  public static void putBoolean(Context paramContext, String paramString, boolean paramBoolean)
  {
    SharedPreferences.Editor edit = getDefaultSharedPreferences(paramContext).edit();
    edit.putBoolean(paramString, paramBoolean);
    edit.commit();
  }
  
  public static void putInt(Context paramContext, String paramString, int paramInt)
  {
    SharedPreferences.Editor edit = getDefaultSharedPreferences(paramContext).edit();
    edit.putInt(paramString, paramInt);
    edit.commit();
  }
  
  public static void putString(Context paramContext, String paramString1, String paramString2)
  {
    SharedPreferences.Editor edit = getDefaultSharedPreferences(paramContext).edit();
    edit.putString(paramString1, paramString2);
    edit.commit();
  }
}