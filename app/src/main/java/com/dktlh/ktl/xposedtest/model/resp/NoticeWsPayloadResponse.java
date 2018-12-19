package com.dktlh.ktl.xposedtest.model.resp;

public class NoticeWsPayloadResponse {
    /**
     * 应用类型(wechat:微信;alipay:支付宝;qq:QQ)
     */
    private String type;
    /**
     * 状态
     */
    private Integer status;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
