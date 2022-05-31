package com.iotait.schoolapp.notification.chatnotif;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.iotait.schoolapp.R;
import com.iotait.schoolapp.application.AppController;
import com.iotait.schoolapp.database.tables.ArticleNotification;
import com.iotait.schoolapp.ui.homepage.HomeNavigationActivity;
import com.iotait.schoolapp.ui.homepage.ui.chat.ChatWindowActivity;
import com.iotait.schoolapp.ui.homepage.ui.chat.ChatWindowGroupActivity;

import java.util.Map;

public class ReceivedNotifications extends FirebaseMessagingService {

    private FirebaseFirestore fs = FirebaseFirestore.getInstance();
    Intent actionActivityIntent;

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        if (remoteMessage.getData().get("type") != null && remoteMessage.getData().get("type").equals("SMS")) {
            getImage(remoteMessage);
        }
        if (remoteMessage.getData().get("type") != null && remoteMessage.getData().get("type").equals("user_payment_request")) {
            getApprove(remoteMessage);
        }
        if (remoteMessage.getData().get("type") != null && remoteMessage.getData().get("type").equals("article")) {
            getArticleNotification(remoteMessage);
        }
    }

    private void getApprove(RemoteMessage remoteMessage) {
        Map<String, String> data = remoteMessage.getData();

        String sender = data.get("sender");
        String receiver = data.get("receiver");
        String icon = data.get("icon");
        String title = data.get("title");
        String body = data.get("body");
        String type = data.get("type");


        Config.content = body;
        Config.title = "Premium Request Accepted";


        actionActivityIntent = new Intent(getApplicationContext(), HomeNavigationActivity.class);
        sendNotification();
    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        //PublicFunctions.updateToken(this, s, fs);
    }

    private void getImage(final RemoteMessage remoteMessage) {


        Map<String, String> data = remoteMessage.getData();


        String sender = data.get("sender");
        String receiver = data.get("receiver");
        String icon = data.get("icon");
        String title = data.get("title");
        String body = data.get("body");
        String type = data.get("type");
        String messageType = data.get("messageType");

        Config.content = body;
        Config.title = data.get("senderName");
        if (messageType != null && messageType.equalsIgnoreCase("individual")){
            actionActivityIntent = new Intent(getApplicationContext(), ChatWindowActivity.class);
            actionActivityIntent.putExtra("senderId", sender);
            actionActivityIntent.putExtra("receiver", receiver);
            actionActivityIntent.putExtra("senderName", data.get("senderName"));
            actionActivityIntent.putExtra("senderProfile", data.get("senderProfile"));
            sendNotification();
        } else if(messageType != null && messageType.equalsIgnoreCase("group")) {
            actionActivityIntent = new Intent(getApplicationContext(), ChatWindowGroupActivity.class);
            actionActivityIntent.putExtra("senderId", sender);
            actionActivityIntent.putExtra("receiver", receiver);
            actionActivityIntent.putExtra("senderName", data.get("senderName"));
            actionActivityIntent.putExtra("senderProfile", data.get("senderProfile"));

            sendNotification();
        }

    }

    private void sendNotification() {

        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        actionActivityIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, actionActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "101";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            @SuppressLint("WrongConstant") NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "Notification", NotificationManager.IMPORTANCE_MAX);

            //Configure Notification Channel
            notificationChannel.setDescription("School Notifications");
            notificationChannel.enableLights(true);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(Config.title)
                .setAutoCancel(true)
                .setSound(defaultSound)
                .setContentText(Config.content)
                .setContentIntent(pendingIntent)
                .setWhen(System.currentTimeMillis())
                .setPriority(Notification.PRIORITY_HIGH);
        notificationManager.notify(1, notificationBuilder.build());
    }

    private void getArticleNotification(RemoteMessage remoteMessage) {
        Map<String, String> data = remoteMessage.getData();

        String sender = data.get("sender");
        String receiver = data.get("receiver");
        String id = data.get("id");
        String title = data.get("title");
        String body = data.get("body");
        String type = data.get("type");

        Config.content = body;
        Config.title = "New article release";
        Log.d("ARTICLE_ID", "getArticleNotification: "+id);

        ArticleNotification articleNotification=new ArticleNotification();
        articleNotification.setArticleId(id);
        articleNotification.setArticleTitle(body);

        AppController.getDatabase().myDao().addNotification(articleNotification);

        actionActivityIntent = new Intent(getApplicationContext(), HomeNavigationActivity.class);
        sendNotification();
    }

}
