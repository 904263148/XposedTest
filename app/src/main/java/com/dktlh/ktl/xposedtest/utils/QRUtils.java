package com.dktlh.ktl.xposedtest.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.DecodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;

import java.util.Hashtable;

public class QRUtils {
    public static Result scanningImage(String paramString)
    {
        if (TextUtils.isEmpty(paramString)) {
            return null;
        }
        Hashtable localHashtable = new Hashtable();
        localHashtable.put(DecodeHintType.CHARACTER_SET, "UTF-8");
        Object localObject = new BitmapFactory.Options();
        ((BitmapFactory.Options)localObject).inJustDecodeBounds = true;
        ImageUtils.getBitmapByFile(paramString);
        ((BitmapFactory.Options)localObject).inJustDecodeBounds = false;
        int j = (int)(((BitmapFactory.Options)localObject).outHeight / 200.0F);
        int i = j;
        if (j <= 0) {
            i = 1;
        }
        ((BitmapFactory.Options)localObject).inSampleSize = i;
        Bitmap bitmap = BitmapFactory.decodeFile(paramString, (BitmapFactory.Options)localObject);
        localObject = new int[bitmap.getWidth() * bitmap.getHeight()];
        bitmap.getPixels((int[])localObject, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
        BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(new RGBLuminanceSource(bitmap.getWidth(), bitmap.getHeight(), (int[])localObject)));
        localObject = new QRCodeReader();
        try
        {
            Result result = ((QRCodeReader)localObject).decode(binaryBitmap, localHashtable);
            return result;
        }
        catch (FormatException e)
        {
            e.printStackTrace();
            return null;
        }
        catch (ChecksumException e)
        {
            Log.e("hxy", "ChecksumException");
            return null;
        }
        catch (NotFoundException e)
        {
            Log.e("hxy", "NotFoundException");
        }
        return null;
    }
}
