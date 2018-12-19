package com.dktlh.ktl.xposedtest.model.resp;

public class LoginResponse {

    private int code;

    private String token;

    private String msg;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isOK() {
        return this.code == 200;
    }
}
