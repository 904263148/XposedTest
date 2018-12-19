package com.dktlh.ktl.xposedtest.websocket.request;

import com.google.gson.annotations.SerializedName;

import java.lang.ref.SoftReference;

/**
 * Created by zly on 2017/6/12.
 */

public class Request<T> {
    @SerializedName("action")
    private String action;

    @SerializedName("uuid")
    private String seqId;

    @SerializedName("json")
    private T req;

    @SerializedName("signature")
    private String signature;

    private transient int reqCount;

    public Request() {
    }


    public Request(String action, String seqId, T req, int reqCount , String signature) {
        this.action = action;
        this.seqId = seqId;
        this.req = req;
        this.reqCount = reqCount;
        this.signature = signature;
    }


    public String getAction() {
        return action;
    }


    public void setAction(String action) {
        this.action = action;
    }


    public String getSeqId() {
        return seqId;
    }


    public void setSeqId(String seqId) {
        this.seqId = seqId;
    }


    public T getReq() {
        return req;
    }


    public void setReq(T req) {
        this.req = req;
    }


    public int getReqCount() {
        return reqCount;
    }


    public void setReqCount(int reqCount) {
        this.reqCount = reqCount;
    }


    public static class Builder<T> {
        private String action;
        private String seqId;
        private T req;
        private int reqCount;
        private String signature;

        public Builder action(String action) {
            this.action = action;
            return this;
        }


        public Builder seqId(String seqId) {
            this.seqId = seqId;
            return this;
        }

        public Builder req(T req) {
            this.req = req;
            return this;
        }


        public Builder reqCount(int reqCount) {
            this.reqCount = reqCount;
            return this;
        }

        public Builder signature(String signature) {
            this.signature = signature;
            return this;
        }

        public Request build() {
            return new Request<T>(action, seqId, req, reqCount ,signature);
        }

    }
}
