package com.dktlh.ktl.xposedtest.model.req;

import java.math.BigDecimal;

public class CreateWsPayloadRequest {
    /**
     * 订单号
     */
    private String orderCode;
    /**
     * 金额
     */
    private BigDecimal amount;
    /**
     * 二维码类型(wechat:微信;alipay:支付宝;qq:QQ)
     */
    private String type;
    /**
     * 二维码链接
     */
    private String qrcode;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getQrcode() {
        return qrcode;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }
}
