package com.dktlh.ktl.xposedtest.utils;

import java.util.Arrays;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

public class SignatureUtil {

    public static String hmacSha1(String token, String... array) {
        try {
            StringBuffer sb = new StringBuffer();
            // 字符串排序
            Arrays.sort(array);
            for (int i = 0; i < array.length; i++) {
                sb.append(array[i]);
            }
            SecretKeySpec signingKey = new SecretKeySpec(token.getBytes("UTF-8"), "HmacSHA1");
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(signingKey);
            byte[] rawHmac = mac.doFinal(sb.toString().getBytes("utf-8"));
            return Hex.encodeHexString(rawHmac);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param signature
     *            签名
     * @param array
     *            字符串数组
     */
    public static boolean verify(String token, String signature, String... array) {
        String _signature = hmacSha1(token, array);
        XposedBridge.log("------------------------_signature" + _signature);
        XposedBridge.log("------------------------signature" + signature);
        if (!_signature.equals(signature)) {
            return false;
        }
        return true;
    }

}
