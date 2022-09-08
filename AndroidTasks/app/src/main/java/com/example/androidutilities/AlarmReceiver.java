package com.example.androidutilities;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.TimePicker;

import androidx.core.app.NotificationCompat;


public class AlarmReceiver extends BroadcastReceiver {
    private NotificationManager mNotificationManager;
    private static final int NOTIFICATION_ID = 0;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("testing","onreceivebroadcast");
        mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        sendNotification(context);
    }

    public void sendNotification(Context context) {
        Log.i("testing","sendnotification");
        Intent tapAction = new Intent(context,MainActivity.class);
        PendingIntent pendingTapAction = PendingIntent.getActivity(context,NOTIFICATION_ID,tapAction,PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, MainActivity.PRIMARY_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("Android Utilities Notification")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentText("Much longer text that cannot fit one line...")
                .setContentIntent(pendingTapAction)
                .setAutoCancel(true)
                .setStyle(new NotificationCompat.BigTextStyle().bigText("Much longer text that cannot fit one line..."));
        mNotificationManager.notify(NOTIFICATION_ID,builder.build());
    }

}
