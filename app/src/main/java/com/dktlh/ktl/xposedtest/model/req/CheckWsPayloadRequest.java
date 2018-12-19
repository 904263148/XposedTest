package com.dktlh.ktl.xposedtest.model.req;

public class CheckWsPayloadRequest {
    /**
     * 应用类型(wechat:微信;alipay:支付宝;qq:QQ)
     */
    private String type;
    /**
     * 状态(0:正常;1:失败)
     */
    private Integer status;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
