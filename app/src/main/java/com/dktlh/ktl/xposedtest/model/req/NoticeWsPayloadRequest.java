package com.dktlh.ktl.xposedtest.model.req;

import java.math.BigDecimal;

public class NoticeWsPayloadRequest {
    /**
     * 应用类型(wechat:微信;alipay:支付宝;qq:QQ)
     */
    private String type;
    /**
     * 订单号
     */
    private String orderCode;
    /**
     * 金额
     */
    private BigDecimal amount;
    /**
     * 额外信息
     */
    private String additional;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getAdditional() {
        return additional;
    }

    public void setAdditional(String additional) {
        this.additional = additional;
    }
}
