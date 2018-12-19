package com.dktlh.ktl.xposedtest.websocket.request;

/**
 * Created by zly on 2017/6/12.
 */

public enum Action {
    LOGIN("login", 1, null),
    HEARTBEAT("heartbeat", 1, null),
    SYNC("sync", 1, null),
    QRCODE("qrcode", 1, null),
    CREATE("create", 1, null),
    NOTICE("notice", 1, null),
    PAYSTATE("paystate", 1, null);


    private String action;
    private int reqEvent;
    private Class respClazz;


    Action(String action, int reqEvent, Class respClazz) {
        this.action = action;
        this.reqEvent = reqEvent;
        this.respClazz = respClazz;
    }


    public String getAction() {
        return action;
    }


    public int getReqEvent() {
        return reqEvent;
    }


    public Class getRespClazz() {
        return respClazz;
    }

}
