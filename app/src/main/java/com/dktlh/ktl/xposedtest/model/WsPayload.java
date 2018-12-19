package com.dktlh.ktl.xposedtest.model;

public class WsPayload {
    /**
     * uuid
     */
    private String uuid;
    /**
     * 动作(create:生成二维码;check:检测设备;heartbeat:心跳检测;notice:支付通知)
     */
    private String action;

    private String json;

    private String signature;

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
