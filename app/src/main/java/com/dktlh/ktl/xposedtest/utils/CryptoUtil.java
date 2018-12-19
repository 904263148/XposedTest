package com.dktlh.ktl.xposedtest.utils;

import java.util.UUID;

public class CryptoUtil {

    /**
     * 签名
     *
     *            签名
     * @param json
     * @return
     */
    public static String getSHA1(String username, String json) {
        return MD5.md5(username + json);
    }

    /**
     * 验证签名
     *
     * @param signature
     *            签名
     * @param json
     * @return
     */
    public static boolean verify(String signature, String username, String json) {
        String _signature = getSHA1(username, json);
        if (!_signature.equals(signature)) {
            return false;
        }
        return true;
    }

    public static String uuid() {
        return UUID.randomUUID().toString();
    }
}
