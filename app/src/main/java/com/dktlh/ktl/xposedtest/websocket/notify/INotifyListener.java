package com.dktlh.ktl.xposedtest.websocket.notify;

/**
 * Created by zly on 2017/2/22.
 */

public interface INotifyListener<T> {
    void fire(T t);
}
