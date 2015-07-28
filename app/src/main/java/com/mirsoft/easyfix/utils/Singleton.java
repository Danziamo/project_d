package com.mirsoft.easyfix.utils;

import android.content.Context;
import android.widget.LinearLayout;

/**
 * Created by parviz on 7/27/15.
 */
public class Singleton {
    Context context;
    private static Singleton instance;


    public static Singleton getInstance(Context context) {
        if(instance == null) instance = new Singleton(context);
        if(instance.context == null) instance.context = context;
        return instance;
    }

    private Singleton(Context context) {
        this.context = context;
    }
}
