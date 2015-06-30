package com.mirsoft.easyfixmaster.bus.facebook;

import android.os.Handler;
import android.os.Looper;

import com.squareup.otto.Bus;

public class FacebookActivityResultBus extends Bus {
    private static FacebookActivityResultBus instance;

    public static FacebookActivityResultBus getInstance() {
        if (instance == null)
            instance = new FacebookActivityResultBus();
        return instance;
    }

    private Handler mHandler = new Handler(Looper.getMainLooper());

    public void postQueue(final Object obj) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                FacebookActivityResultBus.getInstance().post(obj);
            }
        });
    }
}
