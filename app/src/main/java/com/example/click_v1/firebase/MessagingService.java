package com.example.click_v1.firebase;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MessagingService extends FirebaseMessagingService {

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        Log.d("FCM", "Token: " +token);
        Log.d("ANy", "your token");
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);
        Log.d("FCM", "From: ${remoteMessage.from}");
        Log.d("FCM", "Message: "+ message.getNotification().getBody());
    }

}
