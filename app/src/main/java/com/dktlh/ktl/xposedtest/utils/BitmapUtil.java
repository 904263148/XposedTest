package com.dktlh.ktl.xposedtest.utils;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class BitmapUtil {
    private static Bitmap addLogo(Bitmap paramBitmap1, Bitmap paramBitmap2) {
        if (paramBitmap1 == null) {
            return null;
        }
        if (paramBitmap2 == null) {
            return paramBitmap1;
        }
        int i = paramBitmap1.getWidth();
        int j = paramBitmap1.getHeight();
        int k = paramBitmap2.getWidth();
        int m = paramBitmap2.getHeight();
        if (i != 0) {
            if (j == 0) {
                return null;
            }
            if (k != 0) {
                if (m == 0) {
                    return paramBitmap1;
                }
                float f = i * 1.0F / 5.0F / k;
                Bitmap localBitmap = Bitmap.createBitmap(i, j, Config.ARGB_8888);
                try {
                    Canvas localCanvas = new Canvas(localBitmap);
                    localCanvas.drawBitmap(paramBitmap1, 0.0F, 0.0F, null);
                    localCanvas.scale(f, f, i / 2, j / 2);
                    localCanvas.drawBitmap(paramBitmap2, (i - k) / 2, (j - m) / 2, null);
                    localCanvas.save();
                    localCanvas.restore();
                    return localBitmap;
                } catch (Exception e) {
                    e.getStackTrace();
                    return null;
                }
            }
            return paramBitmap1;
        }
        return null;
    }/* Error */
    @android.annotation.SuppressLint({"NewApi"})
    public static String bitmapToBase64(Bitmap paramBitmap)
    {
        // Byte code:
        //   0: aconst_null
        //   1: astore 5
        //   3: aconst_null
        //   4: astore 4
        //   6: aconst_null
        //   7: astore_2
        //   8: aconst_null
        //   9: astore_1
        //   10: aload_0
        //   11: ifnull +126 -> 137
        //   14: new 65	java/io/ByteArrayOutputStream
        //   17: dup
        //   18: invokespecial 66	java/io/ByteArrayOutputStream:<init>	()V
        //   21: astore_3
        //   22: aload_3
        //   23: astore_1
        //   24: aload_3
        //   25: astore_2
        //   26: aload_0
        //   27: getstatic 72	android/graphics/Bitmap$CompressFormat:JPEG	Landroid/graphics/Bitmap$CompressFormat;
        //   30: bipush 30
        //   32: aload_3
        //   33: invokevirtual 76	android/graphics/Bitmap:compress	(Landroid/graphics/Bitmap$CompressFormat;ILjava/io/OutputStream;)Z
        //   36: pop
        //   37: aload_3
        //   38: astore_1
        //   39: aload_3
        //   40: astore_2
        //   41: aload_3
        //   42: invokevirtual 79	java/io/ByteArrayOutputStream:flush	()V
        //   45: aload_3
        //   46: astore_1
        //   47: aload_3
        //   48: astore_2
        //   49: aload_3
        //   50: invokevirtual 82	java/io/ByteArrayOutputStream:close	()V
        //   53: aload_3
        //   54: astore_1
        //   55: aload_3
        //   56: astore_2
        //   57: aload_3
        //   58: invokevirtual 86	java/io/ByteArrayOutputStream:toByteArray	()[B
        //   61: iconst_0
        //   62: invokestatic 92	android/util/Base64:encodeToString	([BI)Ljava/lang/String;
        //   65: astore_0
        //   66: aload_0
        //   67: astore_1
        //   68: aload_3
        //   69: ifnull +21 -> 90
        //   72: aload_0
        //   73: astore_1
        //   74: aload_3
        //   75: invokevirtual 82	java/io/ByteArrayOutputStream:close	()V
        //   78: aload_0
        //   79: astore_1
        //   80: goto +10 -> 90
        //   83: astore_0
        //   84: aload_0
        //   85: invokevirtual 95	java/io/IOException:printStackTrace	()V
        //   88: aload_1
        //   89: areturn
        //   90: aload_1
        //   91: areturn
        //   92: astore_0
        //   93: goto +26 -> 119
        //   96: astore_0
        //   97: aload_2
        //   98: astore_1
        //   99: aload_0
        //   100: invokevirtual 95	java/io/IOException:printStackTrace	()V
        //   103: aload 5
        //   105: astore_1
        //   106: aload_2
        //   107: ifnull -17 -> 90
        //   110: aload 4
        //   112: astore_1
        //   113: aload_2
        //   114: invokevirtual 82	java/io/ByteArrayOutputStream:close	()V
        //   117: aconst_null
        //   118: areturn
        //   119: aload_1
        //   120: ifnull +15 -> 135
        //   123: aload_1
        //   124: invokevirtual 82	java/io/ByteArrayOutputStream:close	()V
        //   127: goto +8 -> 135
        //   130: astore_1
        //   131: aload_1
        //   132: invokevirtual 95	java/io/IOException:printStackTrace	()V
        //   135: aload_0
        //   136: athrow
        //   137: aload_1
        //   138: ifnull +17 -> 155
        //   141: aload_1
        //   142: invokevirtual 82	java/io/ByteArrayOutputStream:close	()V
        //   145: goto +10 -> 155
        //   148: astore_0
        //   149: aload_0
        //   150: invokevirtual 95	java/io/IOException:printStackTrace	()V
        //   153: aconst_null
        //   154: areturn
        //   155: aconst_null
        //   156: areturn
        // Local variable table:
        //   start	length	slot	name	signature
        //   0	157	0	paramBitmap	Bitmap
        //   9	115	1	localObject1	Object
        //   130	12	1	localIOException	java.io.IOException
        //   7	107	2	localObject2	Object
        //   21	54	3	localByteArrayOutputStream	java.io.ByteArrayOutputStream
        //   4	107	4	localObject3	Object
        //   1	103	5	localObject4	Object
        // Exception table:
        //   from	to	target	type
        //   74	78	83	java/io/IOException
        //   113	117	83	java/io/IOException
        //   14	22	92	finally
        //   26	37	92	finally
        //   41	45	92	finally
        //   49	53	92	finally
        //   57	66	92	finally
        //   99	103	92	finally
        //   14	22	96	java/io/IOException
        //   26	37	96	java/io/IOException
        //   41	45	96	java/io/IOException
        //   49	53	96	java/io/IOException
        //   57	66	96	java/io/IOException
        //   123	127	130	java/io/IOException
        //   141	145	148	java/io/IOException
        return "";
    }

    public static Bitmap createQRCode(String paramString, int paramInt) {
        new Hashtable().put(EncodeHintType.CHARACTER_SET, "utf-8");
        Object localObject = null;
        try {
            localObject = new MultiFormatWriter().encode(paramString, BarcodeFormat.QR_CODE, paramInt, paramInt);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        int j = ((BitMatrix) localObject).getWidth();
        int k = ((BitMatrix) localObject).getHeight();
        int[] param = new int[j * k];
        paramInt = 0;
        while (paramInt < k) {
            int i = 0;
            while (i < j) {
                if (((BitMatrix) localObject).get(i, paramInt)) {
                    param[(paramInt * j + i)] = -16777216;
                }
                i += 1;
            }
            paramInt += 1;
        }
        localObject = Bitmap.createBitmap(j, k, Config.ARGB_8888);
        ((Bitmap) localObject).setPixels(param, 0, j, 0, 0, j, k);
        return (Bitmap) localObject;
    }

    public static Bitmap createQRImage(String paramString, int paramInt, Bitmap paramBitmap) {
        BitMatrix bitMatrix;
        Map<Object, Object> localObject;
        Bitmap bitmap;
        int[] arrayOfInt;
        int i;
        int j = 0;
        try {
            localObject = new HashMap();
            ((Map) localObject).put(EncodeHintType.CHARACTER_SET, "utf-8");
            ((Map) localObject).put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            bitMatrix = new QRCodeWriter().encode(paramString, BarcodeFormat.QR_CODE, paramInt, paramInt, (Map) localObject);
            arrayOfInt = new int[paramInt * paramInt];
            i = 0;
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
        if (j < paramInt) {
            if (bitMatrix.get(j, i)) {
                arrayOfInt[(i * paramInt + j)] = -16777216;
            }
        }
        label182:
        for (; ; ) {
            bitmap = Bitmap.createBitmap(paramInt, paramInt, Config.ARGB_8888);
            bitmap.setPixels(arrayOfInt, 0, paramInt, 0, 0, paramInt, paramInt);
            if (paramBitmap != null) {
            }
            return addLogo(bitmap, paramBitmap);
        }
    }

    public static Bitmap createQRImage2(String str, int paramInt) {
        Bitmap bitmap = null;
        BitMatrix result;
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            result = multiFormatWriter.encode(str, BarcodeFormat.QR_CODE, paramInt, paramInt);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            bitmap = barcodeEncoder.createBitmap(result);
        } catch (WriterException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException iae) { // ?
            return null;
        }
        return bitmap;
    }
}