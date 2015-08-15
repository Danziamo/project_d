package com.mirsoft.easyfix.gcm;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.mirsoft.easyfix.R;
import com.mirsoft.easyfix.TabsActivity;

/**
 * Created by parviz on 8/15/15.
 */
public class GCM_GlobalIntentService extends IntentService {

    private static final String TAG = "GCNGlobalIntentService";
    private static final String MESSAGE_KEY = "message";
    public  static final int NOTIFICATION_ID = 1;

    NotificationManager notificationManager;
    NotificationCompat.Builder builder;


    public GCM_GlobalIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Bundle data = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);

        String messageType = gcm.getMessageType(intent);

        if(!data.isEmpty()){

            if(GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)){
                sendNotification("Send Error: " + data.toString());
            }
            else  if(GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)){
                sendNotification("Deleted messages on server:: " + data.toString());
            }
            else if(GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)){
                sendNotification(data.getString(MESSAGE_KEY));
            }
        }

        GCM_BroadcastReceiver.completeWakefulIntent(intent);
    }

    private void sendNotification(String message){

        notificationManager = (NotificationManager)this.getSystemService(Context.NOTIFICATION_SERVICE);

        if(message == null || message.length() == 0) return;

        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,new Intent(this, TabsActivity.class),0);

        builder = new NotificationCompat.Builder(this)
                .setDefaults(Notification.DEFAULT_ALL)
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.logo)
                .setContentTitle("EasyFix")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(message))
                .setContentText(message);

        builder.setContentIntent(pendingIntent);
        notificationManager.notify(NOTIFICATION_ID, builder.build());

    }
}
