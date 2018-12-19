package com.dktlh.ktl.xposedtest.model.resp;

public class CheckWsPayloadResponse {
    /**
     * 应用类型(wechat:微信;alipay:支付宝;qq:QQ)
     */
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
