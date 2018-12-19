package com.dktlh.ktl.xposedtest.event;

public class WebsocketEvent {
    private int state;
    private String msg;

    public WebsocketEvent(int state, String msg) {
        this.state = state;
        this.msg = msg;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
