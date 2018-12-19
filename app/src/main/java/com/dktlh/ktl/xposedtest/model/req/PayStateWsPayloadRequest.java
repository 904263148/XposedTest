package com.dktlh.ktl.xposedtest.model.req;

import java.math.BigDecimal;

public class PayStateWsPayloadRequest {
    /**
     * 应用类型(wechat:微信;alipay:支付宝;qq:QQ)
     */
    private String type;
    /**
     * 金额
     */
    private BigDecimal amount;
    /**
     * 收款人ID
     */
    private String userId;
    /**
     * 付款人ID
     */
    private String payerUserId;
    /**
     * 交易号
     */
    private String transferNo;
    /**
     * 支付状态
     */
    private String state;
    /**
     * 额外信息
     */
    private String additional;

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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPayerUserId() {
        return payerUserId;
    }

    public void setPayerUserId(String payerUserId) {
        this.payerUserId = payerUserId;
    }

    public String getTransferNo() {
        return transferNo;
    }

    public void setTransferNo(String transferNo) {
        this.transferNo = transferNo;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "金额" + amount +" ,收款人ID " + userId +" , 付款人ID" + payerUserId +" , 交易号" + transferNo + " ,支付状态" +state;
    }
}
