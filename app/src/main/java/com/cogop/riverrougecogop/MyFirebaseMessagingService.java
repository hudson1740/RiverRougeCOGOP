package com.cogop.riverrougecogop;
import android.util.Log;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMessaging";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Notification Title: " + remoteMessage.getNotification().getTitle());
            Log.d(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());
            // Handle the notification here (e.g., show it in the notification tray)
        }

        // Handle data payload if present
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Data Payload: " + remoteMessage.getData());
            // Process the data payload as required
        }
    }
}
