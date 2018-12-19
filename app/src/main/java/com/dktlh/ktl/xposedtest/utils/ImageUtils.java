package com.dktlh.ktl.xposedtest.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.os.Build;
import android.os.Build.VERSION;
import android.renderscript.Allocation;
import android.renderscript.Allocation.MipmapControl;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.RenderScript.RSMessageHandler;
import android.renderscript.ScriptIntrinsicBlur;
import android.text.TextUtils;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;

public class ImageUtils
{
  private ImageUtils()
  {
    throw new UnsupportedOperationException("u can't fuck me...");
  }
  
  public static Bitmap addFrame(Bitmap paramBitmap, int paramInt1, int paramInt2)
  {
    if (isEmptyBitmap(paramBitmap)) {
      return null;
    }
    Bitmap localBitmap = Bitmap.createBitmap(paramBitmap.getWidth() + paramInt1, paramBitmap.getHeight() + paramInt1, Config.ARGB_8888);
    Canvas localCanvas = new Canvas(localBitmap);
    Rect localRect = localCanvas.getClipBounds();
    localRect.bottom -= 1;
    localRect.right -= 1;
    Paint localPaint = new Paint();
    localPaint.setColor(paramInt2);
    localPaint.setStyle(Style.STROKE);
    localPaint.setStrokeWidth(paramInt1);
    localCanvas.drawRect(localRect, localPaint);
    localCanvas.drawBitmap(paramBitmap, paramInt1 / 2, paramInt1 / 2, null);
    localCanvas.save();
    localCanvas.restore();
    if (!paramBitmap.isRecycled()) {
      paramBitmap.recycle();
    }
    return localBitmap;
  }
  
  public static Bitmap addReflection(Bitmap paramBitmap, int paramInt)
  {
    if (isEmptyBitmap(paramBitmap)) {
      return null;
    }
    int i = paramBitmap.getWidth();
    int j = paramBitmap.getHeight();
    if ((i != 0) && (j != 0))
    {
      Object localObject = new Matrix();
      ((Matrix)localObject).preScale(1.0F, -1.0F);
      localObject = Bitmap.createBitmap(paramBitmap, 0, j - paramInt, i, paramInt, (Matrix)localObject, false);
      if (localObject == null) {
        return null;
      }
      Bitmap localBitmap = Bitmap.createBitmap(i, j + paramInt, Config.ARGB_8888);
      if (localBitmap == null) {
        return null;
      }
      Canvas localCanvas = new Canvas(localBitmap);
      localCanvas.drawBitmap(paramBitmap, 0.0F, 0.0F, null);
      localCanvas.drawBitmap((Bitmap)localObject, 0.0F, j + 0, null);
      Paint localPaint = new Paint();
      localPaint.setAntiAlias(true);
      localPaint.setShader(new LinearGradient(0.0F, j, 0.0F, localBitmap.getHeight() + 0, 1895825407, 16777215, TileMode.MIRROR));
      localPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
      localCanvas.save();
      localCanvas.drawRect(0.0F, j, i, localBitmap.getHeight() + 0, localPaint);
      localCanvas.restore();
      if (!paramBitmap.isRecycled()) {
        paramBitmap.recycle();
      }
      if (!((Bitmap)localObject).isRecycled()) {
        ((Bitmap)localObject).recycle();
      }
      return localBitmap;
    }
    return null;
  }
  
  public static Bitmap addText(Bitmap paramBitmap, String paramString, int paramInt1, int paramInt2, float paramFloat1, float paramFloat2)
  {
    Object localObject2 = paramBitmap.getConfig();
    Object localObject1 = localObject2;
    if (localObject2 == null) {
      localObject1 = Config.ARGB_8888;
    }
    paramBitmap = paramBitmap.copy((Config)localObject1, true);
    localObject1 = new Canvas(paramBitmap);
    localObject2 = new Paint(1);
    ((Paint)localObject2).setColor(paramInt2);
    ((Paint)localObject2).setTextSize(paramInt1);
    ((Paint)localObject2).setShadowLayer(1.0F, 0.0F, 1.0F, -1);
    Rect localRect = new Rect();
    ((Paint)localObject2).getTextBounds(paramString, 0, paramString.length(), localRect);
    ((Canvas)localObject1).drawText(paramString, paramFloat1, paramFloat2, (Paint)localObject2);
    return paramBitmap;
  }
  
  public static Drawable bitmap2Drawable(Resources paramResources, Bitmap paramBitmap)
  {
    if (paramBitmap == null) {
      return null;
    }
    return new BitmapDrawable(paramResources, paramBitmap);
  }
  
  public static Bitmap bytes2Bitmap(byte[] paramArrayOfByte)
  {
    if ((paramArrayOfByte != null) && (paramArrayOfByte.length != 0)) {
      return BitmapFactory.decodeByteArray(paramArrayOfByte, 0, paramArrayOfByte.length);
    }
    return null;
  }
  
  public static Drawable bytes2Drawable(Resources paramResources, byte[] paramArrayOfByte)
  {
    return bitmap2Drawable(paramResources, bytes2Bitmap(paramArrayOfByte));
  }
  
  public static int calculateInSampleSize(Options paramOptions, int paramInt1, int paramInt2)
  {
    int k = paramOptions.outHeight;
    int m = paramOptions.outWidth;
    int j = 1;
    int i = 1;
    if ((k > paramInt2) || (m > paramInt1)) {
      for (;;)
      {
        j = i;
        if ((k >> 1) / i <= paramInt2) {
          break;
        }
        j = i;
        if ((m >> 1) / i <= paramInt1) {
          break;
        }
        i <<= 1;
      }
    }
    return j;
  }
  
  public static Bitmap drawable2Bitmap(Drawable paramDrawable)
  {
    if (paramDrawable == null) {
      return null;
    }
    return ((BitmapDrawable)paramDrawable).getBitmap();
  }
  
  public static Bitmap fastBlur(Context paramContext, Bitmap paramBitmap, int paramInt, float paramFloat)
  {
    if (isEmptyBitmap(paramBitmap)) {
      return null;
    }
    int i = paramBitmap.getWidth();
    int j = paramBitmap.getHeight();
    int k = i / paramInt;
    int m = j / paramInt;
    if (k != 0)
    {
      if (m == 0) {
        return null;
      }
      Bitmap localBitmap = Bitmap.createBitmap(k, m, Config.ARGB_8888);
      Canvas localCanvas = new Canvas(localBitmap);
      localCanvas.scale(1.0F / paramInt, 1.0F / paramInt);
      Paint localPaint = new Paint();
      localPaint.setFlags(3);
      localPaint.setColorFilter(new PorterDuffColorFilter(0, PorterDuff.Mode.SRC_ATOP));
      localCanvas.drawBitmap(paramBitmap, 0.0F, 0.0F, localPaint);
      if (!paramBitmap.isRecycled()) {
        paramBitmap.recycle();
      }
      if (Build.VERSION.SDK_INT >= 17) {
        paramBitmap = renderScriptBlur(paramContext, localBitmap, paramFloat);
      } else {
        paramBitmap = stackBlur(localBitmap, (int)paramFloat, true);
      }
      if (paramInt == 1) {
        return paramBitmap;
      }
      paramBitmap = Bitmap.createScaledBitmap(paramBitmap, i, j, true);
      if ((paramContext != null) && (!paramBitmap.isRecycled())) {
        paramBitmap.recycle();
      }
      return paramBitmap;
    }
    return null;
  }
  
  public static Bitmap getBitmapByFile(File paramFile)
  {
    if (paramFile == null) {
      return null;
    }
    return getBitmapByFile(paramFile.getPath());
  }
  
  public static Bitmap getBitmapByFile(String paramString)
  {
    return BitmapFactory.decodeFile(paramString);
  }
  
  public static Bitmap getBitmapByFile(String paramString, int paramInt1, int paramInt2)
  {
    if (TextUtils.isEmpty(paramString)) {
      return null;
    }
    Options localOptions = new Options();
    localOptions.inJustDecodeBounds = true;
    BitmapFactory.decodeFile(paramString, localOptions);
    localOptions.inSampleSize = calculateInSampleSize(localOptions, paramInt1, paramInt2);
    localOptions.inJustDecodeBounds = false;
    return BitmapFactory.decodeFile(paramString, localOptions);
  }
  
  public static Bitmap getBitmapByResource(Resources paramResources, int paramInt)
  {
    return BitmapFactory.decodeResource(paramResources, paramInt);
  }
  
  public static String getImageType(InputStream paramInputStream)
  {
    String str = null;
    if (paramInputStream == null) {
      return null;
    }
    try
    {
      byte[] arrayOfByte = new byte[8];
      if (paramInputStream.read(arrayOfByte, 0, 8) != -1) {
        str = getImageType(arrayOfByte);
      }
      return str;
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    return null;
  }
  
  public static String getImageType(byte[] paramArrayOfByte)
  {
    if (isJPEG(paramArrayOfByte)) {
      return "JPEG";
    }
    if (isGIF(paramArrayOfByte)) {
      return "GIF";
    }
    if (isPNG(paramArrayOfByte)) {
      return "PNG";
    }
    if (isBMP(paramArrayOfByte)) {
      return "BMP";
    }
    return null;
  }
  
  public static int getRotateDegree(String paramString)
  {
    try
    {
      int i = new ExifInterface(paramString).getAttributeInt("Orientation", 1);
      if (i != 3)
      {
        if (i != 8) {
          i = 90;
        } else {
          i = 270;
        }
      }
      else {
        i = 180;
      }
      return i;
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    return 0;
  }
  
  private static boolean isBMP(byte[] paramArrayOfByte)
  {
    return (paramArrayOfByte.length >= 2) && (paramArrayOfByte[0] == 66) && (paramArrayOfByte[1] == 77);
  }
  
  private static boolean isEmptyBitmap(Bitmap paramBitmap)
  {
    return (paramBitmap == null) || (paramBitmap.getWidth() == 0) || (paramBitmap.getHeight() == 0);
  }
  
  private static boolean isGIF(byte[] paramArrayOfByte)
  {
    return (paramArrayOfByte.length >= 6) && (paramArrayOfByte[0] == 71) && (paramArrayOfByte[1] == 73) && (paramArrayOfByte[2] == 70) && (paramArrayOfByte[3] == 56) && ((paramArrayOfByte[4] == 55) || (paramArrayOfByte[4] == 57)) && (paramArrayOfByte[5] == 97);
  }
  
  public static boolean isImage(File paramFile)
  {
    return (paramFile != null) && (isImage(paramFile.getPath()));
  }
  
  public static boolean isImage(String paramString)
  {
    paramString = paramString.toUpperCase();
    return (paramString.endsWith(".PNG")) || (paramString.endsWith(".JPG")) || (paramString.endsWith(".JPEG")) || (paramString.endsWith(".BMP")) || (paramString.endsWith(".GIF"));
  }
  
  private static boolean isJPEG(byte[] paramArrayOfByte)
  {
    return (paramArrayOfByte.length >= 2) && (paramArrayOfByte[0] == -1) && (paramArrayOfByte[1] == -40);
  }
  
  private static boolean isPNG(byte[] paramArrayOfByte)
  {
    return (paramArrayOfByte.length >= 8) && (paramArrayOfByte[0] == -119) && (paramArrayOfByte[1] == 80) && (paramArrayOfByte[2] == 78) && (paramArrayOfByte[3] == 71) && (paramArrayOfByte[4] == 13) && (paramArrayOfByte[5] == 10) && (paramArrayOfByte[6] == 26) && (paramArrayOfByte[7] == 10);
  }
  
  @TargetApi(17)
  public static Bitmap renderScriptBlur(Context paramContext, Bitmap paramBitmap, float paramFloat)
  {
    if (isEmptyBitmap(paramBitmap)) {
      return null;
    }
    Context localContext = null;
    RenderScript  renderScript = null;
    for (;;)
    {
      try
      {
        renderScript = RenderScript.create(paramContext);
        localContext = paramContext;
        renderScript.setMessageHandler(new RSMessageHandler());
        localContext = paramContext;
        Allocation localAllocation1 = Allocation.createFromBitmap(renderScript, paramBitmap, MipmapControl.MIPMAP_NONE, 1);
        localContext = paramContext;
        Allocation localAllocation2 = Allocation.createTyped(renderScript, localAllocation1.getType());
        localContext = paramContext;
        ScriptIntrinsicBlur localScriptIntrinsicBlur = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript));
        if (paramFloat > 25.0F)
        {
//          f = 25.0F;
          localContext = paramContext;
          localScriptIntrinsicBlur.setInput(localAllocation1);
          localContext = paramContext;
          localScriptIntrinsicBlur.setRadius(25.0F);
          localContext = paramContext;
          localScriptIntrinsicBlur.forEach(localAllocation2);
          localContext = paramContext;
          localAllocation2.copyTo(paramBitmap);
          return paramBitmap;
        }
      }
      finally
      {
        if (localContext != null) {
          renderScript.destroy();
        }
      }
      float f = paramFloat;
      if (paramFloat <= 0.0F) {
        f = 1.0F;
      }
    }
  }
  
  public static Bitmap rotateBitmap(Bitmap paramBitmap, int paramInt)
  {
    if (paramBitmap != null)
    {
      if (paramInt == 0) {
        return paramBitmap;
      }
      Object localObject = new Matrix();
      ((Matrix)localObject).setRotate(paramInt, paramBitmap.getWidth() / 2, paramBitmap.getHeight() / 2);
      localObject = Bitmap.createBitmap(paramBitmap, 0, 0, paramBitmap.getWidth(), paramBitmap.getHeight(), (Matrix)localObject, true);
      if (!paramBitmap.isRecycled()) {
        paramBitmap.recycle();
      }
      return (Bitmap)localObject;
    }
    return paramBitmap;
  }
  
  public static Bitmap scaleImage(Bitmap paramBitmap, float paramFloat1, float paramFloat2)
  {
    if (paramBitmap == null) {
      return null;
    }
    Object localObject = new Matrix();
    ((Matrix)localObject).postScale(paramFloat1, paramFloat2);
    localObject = Bitmap.createBitmap(paramBitmap, 0, 0, paramBitmap.getWidth(), paramBitmap.getHeight(), (Matrix)localObject, true);
    if (!paramBitmap.isRecycled()) {
      paramBitmap.recycle();
    }
    return (Bitmap)localObject;
  }
  
  public static Bitmap scaleImage(Bitmap paramBitmap, int paramInt1, int paramInt2)
  {
    return Bitmap.createScaledBitmap(paramBitmap, paramInt1, paramInt2, true);
  }
  
  public static Bitmap stackBlur(Bitmap paramBitmap, int paramInt, boolean paramBoolean)
  {
    int i9 = paramInt;
    if (!paramBoolean) {
      paramBitmap = paramBitmap.copy(paramBitmap.getConfig(), true);
    }
    if (i9 < 1) {
      return null;
    }
    int i18 = paramBitmap.getWidth();
    int j = paramBitmap.getHeight();
    int[] arrayOfInt1 = new int[i18 * j];
    paramBitmap.getPixels(arrayOfInt1, 0, i18, 0, 0, i18, j);
    int i12 = i18 - 1;
    int m = j - 1;
    int i = i18 * j;
    int i19 = i9 + i9 + 1;
    int[] arrayOfInt2 = new int[i];
    int[] arrayOfInt3 = new int[i];
    int[] arrayOfInt4 = new int[i];
    int[] arrayOfInt5 = new int[Math.max(i18, j)];
    i = i19 + 1 >> 1;
    int i11 = i * i;
    int[] arrayOfInt6 = new int[i11 * 256];
    i = 0;
    while (i < i11 * 256)
    {
      arrayOfInt6[i] = (i / i11);
      i += 1;
    }
    int[][] arrayOfInt = (int[][])Array.newInstance(Integer.TYPE, new int[] { i19, 3 });
    int i20 = i9 + 1;
    int i10 = 0;
    int i13 = 0;
    int k = 0;
    i = m;
    int i6;
    int i7;
    int n;
    int i1;
    int i2;
    int i3;
    int i5;
    int i8;
    int i4;
    int i14;
    int[] arrayOfInt7;
    int i15;
    while (k < j)
    {
      i6 = 0;
      i7 = 0;
      n = 0;
      i1 = 0;
      i2 = 0;
      i3 = 0;
      i5 = 0;
      i8 = 0;
      m = -i9;
      i4 = 0;
      while (m <= i9)
      {
        i14 = arrayOfInt1[(i13 + Math.min(i12, Math.max(m, 0)))];
        arrayOfInt7 = arrayOfInt[(m + i9)];
        arrayOfInt7[0] = ((i14 & 0xFF0000) >> 16);
        arrayOfInt7[1] = ((i14 & 0xFF00) >> 8);
        arrayOfInt7[2] = (i14 & 0xFF);
        i14 = i20 - Math.abs(m);
        i7 += arrayOfInt7[0] * i14;
        i6 += arrayOfInt7[1] * i14;
        i4 += arrayOfInt7[2] * i14;
        if (m > 0)
        {
          i8 += arrayOfInt7[0];
          i5 += arrayOfInt7[1];
          i3 += arrayOfInt7[2];
        }
        else
        {
          i2 += arrayOfInt7[0];
          i1 += arrayOfInt7[1];
          n += arrayOfInt7[2];
        }
        m += 1;
      }
      int i16 = i9;
      int i17 = 0;
      i15 = i8;
      i14 = i5;
      i8 = i3;
      i5 = i4;
      i4 = i16;
      i3 = i17;
      while (i3 < i18)
      {
        arrayOfInt2[i13] = arrayOfInt6[i7];
        arrayOfInt3[i13] = arrayOfInt6[i6];
        arrayOfInt4[i13] = arrayOfInt6[i5];
        arrayOfInt7 = arrayOfInt[((i4 - i9 + i19) % i19)];
        int i21 = arrayOfInt7[0];
        i17 = arrayOfInt7[1];
        i16 = arrayOfInt7[2];
        if (k == 0) {
          arrayOfInt5[i3] = Math.min(i3 + i9 + 1, i12);
        }
        int i22 = arrayOfInt1[(i10 + arrayOfInt5[i3])];
        arrayOfInt7[0] = ((i22 & 0xFF0000) >> 16);
        arrayOfInt7[1] = ((i22 & 0xFF00) >> 8);
        arrayOfInt7[2] = (i22 & 0xFF);
        i15 += arrayOfInt7[0];
        i14 += arrayOfInt7[1];
        i8 += arrayOfInt7[2];
        i7 = i7 - i2 + i15;
        i6 = i6 - i1 + i14;
        i5 = i5 - n + i8;
        i4 = (i4 + 1) % i19;
        arrayOfInt7 = arrayOfInt[(i4 % i19)];
        i2 = i2 - i21 + arrayOfInt7[0];
        i1 = i1 - i17 + arrayOfInt7[1];
        n = n - i16 + arrayOfInt7[2];
        i15 -= arrayOfInt7[0];
        i14 -= arrayOfInt7[1];
        i8 -= arrayOfInt7[2];
        i13 += 1;
        i3 += 1;
      }
      i10 += i18;
      k += 1;
    }
    m = j;
    n = k;
    i1 = 0;
    j = i;
    k = m;
    m = n;
    i = i1;
    for (;;)
    {
      i9 = paramInt;
      if (i >= i18) {
        break;
      }
      i1 = 0;
      i2 = 0;
      i7 = 0;
      i3 = 0;
      i6 = 0;
      i10 = -i9;
      i12 = -i9;
      i4 = 0;
      i8 = 0;
      n = 0;
      i5 = 0;
      i11 = i10 * i18;
      i10 = m;
      m = i12;
      while (m <= i9)
      {
        i15 = Math.max(0, i11) + i;
        arrayOfInt7 = arrayOfInt[(m + i9)];
        arrayOfInt7[0] = arrayOfInt2[i15];
        arrayOfInt7[1] = arrayOfInt3[i15];
        arrayOfInt7[2] = arrayOfInt4[i15];
        i13 = i20 - Math.abs(m);
        i14 = arrayOfInt2[i15];
        i12 = i4 + arrayOfInt3[i15] * i13;
        i5 += arrayOfInt4[i15] * i13;
        if (m > 0)
        {
          i6 += arrayOfInt7[0];
          i3 += arrayOfInt7[1];
          i7 += arrayOfInt7[2];
        }
        else
        {
          i2 += arrayOfInt7[0];
          i1 += arrayOfInt7[1];
          n += arrayOfInt7[2];
        }
        i4 = i11;
        if (m < j) {
          i4 = i11 + i18;
        }
        m += 1;
        i8 += i14 * i13;
        i11 = i4;
        i4 = i12;
      }
      i12 = i;
      i14 = 0;
      i13 = i9;
      i9 = i7;
      i7 = n;
      i11 = i6;
      i10 = i3;
      i3 = i13;
      i6 = i4;
      i4 = i12;
      n = i14;
      while (n < k)
      {
        arrayOfInt1[i4] = (arrayOfInt1[i4] & 0xFF000000 | arrayOfInt6[i8] << 16 | arrayOfInt6[i6] << 8 | arrayOfInt6[i5]);
        arrayOfInt7 = arrayOfInt[((i3 - paramInt + i19) % i19)];
        i14 = arrayOfInt7[0];
        i13 = arrayOfInt7[1];
        i12 = arrayOfInt7[2];
        if (i == 0) {
          arrayOfInt5[n] = (Math.min(n + i20, j) * i18);
        }
        i15 = arrayOfInt5[n] + i;
        arrayOfInt7[0] = arrayOfInt2[i15];
        arrayOfInt7[1] = arrayOfInt3[i15];
        arrayOfInt7[2] = arrayOfInt4[i15];
        i11 += arrayOfInt7[0];
        i10 += arrayOfInt7[1];
        i9 += arrayOfInt7[2];
        i8 = i8 - i2 + i11;
        i6 = i6 - i1 + i10;
        i5 = i5 - i7 + i9;
        i3 = (i3 + 1) % i19;
        arrayOfInt7 = arrayOfInt[i3];
        i2 = i2 - i14 + arrayOfInt7[0];
        i1 = i1 - i13 + arrayOfInt7[1];
        i7 = i7 - i12 + arrayOfInt7[2];
        i11 -= arrayOfInt7[0];
        i10 -= arrayOfInt7[1];
        i9 -= arrayOfInt7[2];
        i4 += i18;
        n += 1;
      }
      i += 1;
      m = n;
    }
    paramBitmap.setPixels(arrayOfInt1, 0, i18, 0, 0, i18, k);
    return paramBitmap;
  }
  
  public static Bitmap toGray(Bitmap paramBitmap)
  {
    return toGray(paramBitmap, false);
  }
  
  public static Bitmap toGray(Bitmap paramBitmap, boolean paramBoolean)
  {
    if (isEmptyBitmap(paramBitmap)) {
      return null;
    }
    Bitmap localBitmap = Bitmap.createBitmap(paramBitmap.getWidth(), paramBitmap.getHeight(), Config.RGB_565);
    Canvas localCanvas = new Canvas(localBitmap);
    Paint localPaint = new Paint();
    ColorMatrix localColorMatrix = new ColorMatrix();
    localColorMatrix.setSaturation(0.0F);
    localPaint.setColorFilter(new ColorMatrixColorFilter(localColorMatrix));
    localCanvas.drawBitmap(paramBitmap, 0.0F, 0.0F, localPaint);
    if ((paramBoolean) && (!paramBitmap.isRecycled())) {
      paramBitmap.recycle();
    }
    return localBitmap;
  }
  
  public static Bitmap toRound(Bitmap paramBitmap)
  {
    if (paramBitmap == null) {
      return null;
    }
    int i = paramBitmap.getWidth();
    int j = paramBitmap.getHeight();
    Bitmap localBitmap = Bitmap.createBitmap(i, j, Config.ARGB_8888);
    Canvas localCanvas = new Canvas(localBitmap);
    Paint localPaint = new Paint();
    Rect localRect = new Rect(0, 0, i, j);
    localPaint.setAntiAlias(true);
    localCanvas.drawARGB(0, 0, 0, 0);
    localPaint.setColor(0);
    localCanvas.drawCircle(i / 2, j / 2, i / 2, localPaint);
    localPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
    localCanvas.drawBitmap(paramBitmap, localRect, localRect, localPaint);
    if (!paramBitmap.isRecycled()) {
      paramBitmap.recycle();
    }
    return localBitmap;
  }
  
  public static Bitmap toRoundCorner(Bitmap paramBitmap, float paramFloat)
  {
    if (paramBitmap == null) {
      return null;
    }
    int i = paramBitmap.getWidth();
    int j = paramBitmap.getHeight();
    Bitmap localBitmap = Bitmap.createBitmap(i, j, Config.ARGB_8888);
    Object localObject = new BitmapShader(paramBitmap, TileMode.CLAMP, TileMode.CLAMP);
    Paint localPaint = new Paint();
    localPaint.setAntiAlias(true);
    localPaint.setShader((Shader)localObject);
    localObject = new RectF(0.0F, 0.0F, i, j);
    Canvas localCanvas = new Canvas(localBitmap);
    localCanvas.drawRoundRect((RectF)localObject, paramFloat, paramFloat, localPaint);
    localCanvas.save();
    localCanvas.restore();
    if (!paramBitmap.isRecycled()) {
      paramBitmap.recycle();
    }
    return localBitmap;
  }
}

