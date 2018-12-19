package com.dktlh.ktl.xposedtest.websocket.common;


import com.dktlh.ktl.xposedtest.websocket.request.Action;
import com.dktlh.ktl.xposedtest.websocket.request.Request;

/**
 * Created by zly on 2017/7/23.
 */

public interface IWsCallback<T> {
    void onSuccess(T t);
    void onError(String msg, Request request, Action action);
    void onTimeout(Request request, Action action);
}
