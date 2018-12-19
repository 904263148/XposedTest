package com.dktlh.ktl.xposedtest.websocket.notify;

import com.dktlh.ktl.xposedtest.websocket.response.Response;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.orhanobut.logger.Logger;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zly on 2017/2/22.
 */

public class NotifyListenerManager {
    private final String TAG = this.getClass().getSimpleName();
    private volatile static NotifyListenerManager manager;
    private Map<String, INotifyListener> map = new HashMap<>();

    private NotifyListenerManager() {
        regist();
    }

    public static NotifyListenerManager getInstance() {
        if (manager == null) {
            synchronized (NotifyListenerManager.class) {
                if (manager == null) {
                    manager = new NotifyListenerManager();
                }
            }
        }
        return manager;
    }

    private void regist() {
        map.put("notifyAnnounceMsg", new AnnounceMsgListener());
    }

    public void fire(Response response) {
        String action = response.getAction();
        String resp = response.getResp();
        INotifyListener listener = map.get(action);
        if (listener == null) {
            Logger.t(TAG).d("no found notify listener");
            return;
        }

        NotifyClass notifyClass = listener.getClass().getAnnotation(NotifyClass.class);
        Class<?> clazz = notifyClass.value();
        Object result = null;
        try {
            result = new Gson().fromJson(resp, clazz);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        Logger.t(TAG).d(result);
        listener.fire(result);
    }


}
