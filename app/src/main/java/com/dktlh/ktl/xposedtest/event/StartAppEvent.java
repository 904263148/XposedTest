package com.dktlh.ktl.xposedtest.event;

public class StartAppEvent {
    /**
     * 0：微信
     * 1：支付宝
     * 2: qq
     */
    private int type;
    /**
     * 0：未启动
     * 1：已启动
     */
    private int state;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
