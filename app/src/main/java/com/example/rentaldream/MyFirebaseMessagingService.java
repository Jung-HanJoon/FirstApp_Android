package com.example.rentaldream;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.example.rentaldream.ui.LoginActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        if (remoteMessage.getData().size() > 0) {
            try {
                String title = remoteMessage.getData().get("title").toString();
                String body = remoteMessage.getNotification().getBody().toString();
                Map<String, String> extraData = remoteMessage.getData();

                String data = extraData.get("data").toString();
                String gogo = extraData.get("gogo").toString();
                String into = extraData.get("into").toString();
                String chatroomid = extraData.get("chatroomid").toString();
                String sellerID = extraData.get("sellerID").toString();
                String saln = extraData.get("saln").toString();
                String buyer =extraData.get("buyer").toString();
                String buyerName =extraData.get("buyerName").toString();
                sendNotification(title, body, data, gogo, into, chatroomid, sellerID, saln, buyer, buyerName);
            }catch (Exception e){
                e.printStackTrace();
            }



        }
    }

    private void sendNotification(String title, String messageBody, String data, String gogo, String into, String chatroomid, String sellerID, String saln, String buyer, String buyerName) {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra("data",data);
        intent.putExtra("gogo", gogo);
        intent.putExtra("into", into);
        intent.putExtra("chatroomid", chatroomid);
        intent.putExtra("sellerID", sellerID);
        intent.putExtra("saln", saln);
        intent.putExtra("buyer", buyer);
        intent.putExtra("buyerName", buyerName);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        String channelId = "fcm_default_channel";//getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.icon)
                        .setContentTitle(title)
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setPriority(Notification.PRIORITY_HIGH)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}
