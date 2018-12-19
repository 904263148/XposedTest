package com.dktlh.ktl.xposedtest.websocket.common;

/**
 * Created by zly on 2017/2/16.
 */

public enum ErrorCode {
    BUSINESS_EXCEPTION("业务异常"),
    PARSE_EXCEPTION("数据格式异常"),
    DISCONNECT_EXCEPTION("连接断开");

    private String msg;

    ErrorCode(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
