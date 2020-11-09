package com.marte5.beautifulvino;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;

public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        final String payload = intent.getStringExtra("payload");
        createNotification(context, payload);

        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void createNotification(Context context, String payload) {
        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);

        Notification notification = new NotificationCompat.Builder(context, "0")
                .setSmallIcon(R.drawable.notif_icon)
                .setContentText(payload)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(payload))
                .build();

        notificationManager.notify(0, notification);

       /* Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("payload", payload);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                intent, PendingIntent.FLAG_CANCEL_CURRENT);
        notification.contentIntent = pendingIntent;*/


    }
}
