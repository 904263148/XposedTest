package com.dktlh.ktl.xposedtest.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.telephony.TelephonyManager;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class StringUtils
{
  public static byte[] getBytes(String paramString)
  {
    try
    {
      FileInputStream inputStream = new FileInputStream(new File(paramString));
      ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream(1000);
      byte[] arrayOfByte = new byte['Ϩ'];
      for (;;)
      {
        int i = inputStream.read(arrayOfByte);
        if (i == -1) {
          break;
        }
        localByteArrayOutputStream.write(arrayOfByte, 0, i);
      }
      inputStream.close();
      localByteArrayOutputStream.close();
      byte[] bytes = localByteArrayOutputStream.toByteArray();
      return bytes;
    }
    catch (IOException e)
    {
      e.printStackTrace();
      return null;
    }
  }

  /* Error */
  public static void getFile(byte[] paramArrayOfByte, String paramString1, String paramString2)
  {
    // Byte code:
    //   0: aconst_null
    //   1: astore 8
    //   3: aconst_null
    //   4: astore 7
    //   6: aconst_null
    //   7: astore 10
    //   9: aconst_null
    //   10: astore 9
    //   12: aload 7
    //   14: astore 4
    //   16: aload 9
    //   18: astore_3
    //   19: aload 8
    //   21: astore 6
    //   23: aload 10
    //   25: astore 5
    //   27: new 19	java/io/File
    //   30: dup
    //   31: aload_1
    //   32: invokespecial 22	java/io/File:<init>	(Ljava/lang/String;)V
    //   35: astore 11
    //   37: aload 7
    //   39: astore 4
    //   41: aload 9
    //   43: astore_3
    //   44: aload 8
    //   46: astore 6
    //   48: aload 10
    //   50: astore 5
    //   52: aload 11
    //   54: invokevirtual 58	java/io/File:exists	()Z
    //   57: ifne +47 -> 104
    //   60: aload 7
    //   62: astore 4
    //   64: aload 9
    //   66: astore_3
    //   67: aload 8
    //   69: astore 6
    //   71: aload 10
    //   73: astore 5
    //   75: aload 11
    //   77: invokevirtual 61	java/io/File:isDirectory	()Z
    //   80: ifne +24 -> 104
    //   83: aload 7
    //   85: astore 4
    //   87: aload 9
    //   89: astore_3
    //   90: aload 8
    //   92: astore 6
    //   94: aload 10
    //   96: astore 5
    //   98: aload 11
    //   100: invokevirtual 64	java/io/File:mkdirs	()Z
    //   103: pop
    //   104: aload 7
    //   106: astore 4
    //   108: aload 9
    //   110: astore_3
    //   111: aload 8
    //   113: astore 6
    //   115: aload 10
    //   117: astore 5
    //   119: new 66	java/lang/StringBuilder
    //   122: dup
    //   123: invokespecial 67	java/lang/StringBuilder:<init>	()V
    //   126: astore 11
    //   128: aload 7
    //   130: astore 4
    //   132: aload 9
    //   134: astore_3
    //   135: aload 8
    //   137: astore 6
    //   139: aload 10
    //   141: astore 5
    //   143: aload 11
    //   145: aload_1
    //   146: invokevirtual 71	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   149: pop
    //   150: aload 7
    //   152: astore 4
    //   154: aload 9
    //   156: astore_3
    //   157: aload 8
    //   159: astore 6
    //   161: aload 10
    //   163: astore 5
    //   165: aload 11
    //   167: ldc 73
    //   169: invokevirtual 71	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   172: pop
    //   173: aload 7
    //   175: astore 4
    //   177: aload 9
    //   179: astore_3
    //   180: aload 8
    //   182: astore 6
    //   184: aload 10
    //   186: astore 5
    //   188: aload 11
    //   190: aload_2
    //   191: invokevirtual 71	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   194: pop
    //   195: aload 7
    //   197: astore 4
    //   199: aload 9
    //   201: astore_3
    //   202: aload 8
    //   204: astore 6
    //   206: aload 10
    //   208: astore 5
    //   210: new 75	java/io/FileOutputStream
    //   213: dup
    //   214: new 19	java/io/File
    //   217: dup
    //   218: aload 11
    //   220: invokevirtual 79	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   223: invokespecial 22	java/io/File:<init>	(Ljava/lang/String;)V
    //   226: invokespecial 80	java/io/FileOutputStream:<init>	(Ljava/io/File;)V
    //   229: astore_1
    //   230: aload 7
    //   232: astore 4
    //   234: aload_1
    //   235: astore_3
    //   236: aload 8
    //   238: astore 6
    //   240: aload_1
    //   241: astore 5
    //   243: new 82	java/io/BufferedOutputStream
    //   246: dup
    //   247: aload_1
    //   248: invokespecial 85	java/io/BufferedOutputStream:<init>	(Ljava/io/OutputStream;)V
    //   251: astore_2
    //   252: aload_2
    //   253: astore 4
    //   255: aload_1
    //   256: astore_3
    //   257: aload_2
    //   258: astore 6
    //   260: aload_1
    //   261: astore 5
    //   263: aload_2
    //   264: aload_0
    //   265: invokevirtual 88	java/io/BufferedOutputStream:write	([B)V
    //   268: aload_2
    //   269: ifnull +15 -> 284
    //   272: aload_2
    //   273: invokevirtual 89	java/io/BufferedOutputStream:close	()V
    //   276: goto +8 -> 284
    //   279: astore_0
    //   280: aload_0
    //   281: invokevirtual 49	java/io/IOException:printStackTrace	()V
    //   284: aload_1
    //   285: ifnull +59 -> 344
    //   288: aload_1
    //   289: invokevirtual 90	java/io/FileOutputStream:close	()V
    //   292: return
    //   293: astore_0
    //   294: aload_0
    //   295: invokevirtual 49	java/io/IOException:printStackTrace	()V
    //   298: return
    //   299: astore_0
    //   300: goto +45 -> 345
    //   303: astore_0
    //   304: aload 6
    //   306: astore 4
    //   308: aload 5
    //   310: astore_3
    //   311: aload_0
    //   312: invokevirtual 91	java/lang/Exception:printStackTrace	()V
    //   315: aload 6
    //   317: ifnull +16 -> 333
    //   320: aload 6
    //   322: invokevirtual 89	java/io/BufferedOutputStream:close	()V
    //   325: goto +8 -> 333
    //   328: astore_0
    //   329: aload_0
    //   330: invokevirtual 49	java/io/IOException:printStackTrace	()V
    //   333: aload 5
    //   335: ifnull +9 -> 344
    //   338: aload 5
    //   340: invokevirtual 90	java/io/FileOutputStream:close	()V
    //   343: return
    //   344: return
    //   345: aload 4
    //   347: ifnull +16 -> 363
    //   350: aload 4
    //   352: invokevirtual 89	java/io/BufferedOutputStream:close	()V
    //   355: goto +8 -> 363
    //   358: astore_1
    //   359: aload_1
    //   360: invokevirtual 49	java/io/IOException:printStackTrace	()V
    //   363: aload_3
    //   364: ifnull +15 -> 379
    //   367: aload_3
    //   368: invokevirtual 90	java/io/FileOutputStream:close	()V
    //   371: goto +8 -> 379
    //   374: astore_1
    //   375: aload_1
    //   376: invokevirtual 49	java/io/IOException:printStackTrace	()V
    //   379: aload_0
    //   380: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	381	0	paramArrayOfByte	byte[]
    //   0	381	1	paramString1	String
    //   0	381	2	paramString2	String
    //   18	350	3	localObject1	Object
    //   14	337	4	localObject2	Object
    //   25	314	5	localObject3	Object
    //   21	300	6	localObject4	Object
    //   4	227	7	localObject5	Object
    //   1	236	8	localObject6	Object
    //   10	190	9	localObject7	Object
    //   7	200	10	localObject8	Object
    //   35	184	11	localObject9	Object
    // Exception table:
    //   from	to	target	type
    //   272	276	279	java/io/IOException
    //   288	292	293	java/io/IOException
    //   338	343	293	java/io/IOException
    //   27	37	299	finally
    //   52	60	299	finally
    //   75	83	299	finally
    //   98	104	299	finally
    //   119	128	299	finally
    //   143	150	299	finally
    //   165	173	299	finally
    //   188	195	299	finally
    //   210	230	299	finally
    //   243	252	299	finally
    //   263	268	299	finally
    //   311	315	299	finally
    //   27	37	303	java/lang/Exception
    //   52	60	303	java/lang/Exception
    //   75	83	303	java/lang/Exception
    //   98	104	303	java/lang/Exception
    //   119	128	303	java/lang/Exception
    //   143	150	303	java/lang/Exception
    //   165	173	303	java/lang/Exception
    //   188	195	303	java/lang/Exception
    //   210	230	303	java/lang/Exception
    //   243	252	303	java/lang/Exception
    //   263	268	303	java/lang/Exception
    //   320	325	328	java/io/IOException
    //   350	355	358	java/io/IOException
    //   367	371	374	java/io/IOException
  }

  public static String getTextCenter(String paramString1, String paramString2, String paramString3)
  {
    try
    {
      int i = paramString1.indexOf(paramString2) + paramString2.length();
      paramString1 = paramString1.substring(i, paramString1.indexOf(paramString3, i));
      return paramString1;
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return "error";
  }

  @SuppressLint("MissingPermission")
  public static String getimei(Context paramContext)
  {
    @SuppressLint("WrongConstant") TelephonyManager telephonyManager = (TelephonyManager)paramContext.getSystemService("phone");
    if (telephonyManager.getDeviceId() == null) {
      return "00000000000000000";
    }
    return telephonyManager.getDeviceId();
  }
  
  public static void main(String[] paramArrayOfString) {}
}

