package com.dktlh.ktl.xposedtest.websocket.notify;

import com.google.gson.annotations.SerializedName;

/**
 * Created by zly on 2017/2/22.
 */

public class AnnounceMsgNotify {
    @SerializedName("msg_version")
    private String msgVersion;

    public String getMsgVersion() {
        return msgVersion;
    }

    public void setMsgVersion(String msgVersion) {
        this.msgVersion = msgVersion;
    }

}
