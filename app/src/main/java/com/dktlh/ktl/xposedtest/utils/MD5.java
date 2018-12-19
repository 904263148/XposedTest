package com.dktlh.ktl.xposedtest.utils;

import java.security.MessageDigest;

public class MD5 {
    public static void main(String[] paramArrayOfString) {
        System.out.println(md5("31119@qq.com123456"));
        System.out.println(md5("mj1"));
    }

    public static String md5(String paramString) {
        try {
            Object localObject = MessageDigest.getInstance("MD5");
            ((MessageDigest) localObject).update(paramString.getBytes());
            byte[] bytes = ((MessageDigest) localObject).digest();
            StringBuffer localStringBuffer = new StringBuffer("");
            int i = 0;
            while (i < bytes.length) {
                int k = bytes[i];
                int j = k;
                if (k < 0) {
                    j = k + 256;
                }
                if (j < 16) {
                    localStringBuffer.append("0");
                }
                localStringBuffer.append(Integer.toHexString(j));
                i += 1;
            }
            localObject = localStringBuffer.toString();
            return (String) localObject;
        } catch (Exception localException) {
            localException.printStackTrace();
        }
        return paramString;
    }

    public static String md5byte(byte[] paramArrayOfByte) {
        try {
            Object localObject = MessageDigest.getInstance("MD5");
            ((MessageDigest) localObject).update(paramArrayOfByte);
            paramArrayOfByte = ((MessageDigest) localObject).digest();
            localObject = new StringBuffer("");
            int i = 0;
            while (i < paramArrayOfByte.length) {
                int k = paramArrayOfByte[i];
                int j = k;
                if (k < 0) {
                    j = k + 256;
                }
                if (j < 16) {
                    ((StringBuffer) localObject).append("0");
                }
                ((StringBuffer) localObject).append(Integer.toHexString(j));
                i += 1;
            }
            String s = ((StringBuffer) localObject).toString();
            return s;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
