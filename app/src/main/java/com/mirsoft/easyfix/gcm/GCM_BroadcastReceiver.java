package com.mirsoft.easyfix.gcm;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

/**
 * Created by parviz on 8/15/15.
 */
public class GCM_BroadcastReceiver extends WakefulBroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        ComponentName component = new ComponentName(context.getPackageName(),GCM_GlobalIntentService.class.getName());
        startWakefulService(context,intent.setComponent(component));
    }
}
