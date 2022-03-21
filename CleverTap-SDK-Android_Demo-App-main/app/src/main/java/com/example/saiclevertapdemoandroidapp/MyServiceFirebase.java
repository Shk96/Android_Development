
package com.example.saiclevertapdemoandroidapp;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.clevertap.android.sdk.CleverTapAPI;
import com.clevertap.android.sdk.pushnotification.NotificationInfo;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

//22. This service class is used to send and get data from firebase

public class MyServiceFirebase extends FirebaseMessagingService {

    //So When a user sends a notification from the CT dashboard, the daata is send to fire base and
// then firebase sends it to the device and then device assigns the
// push notification to the concerned app
    @Override
    public void onMessageReceived(RemoteMessage message) {
        try {
            if (message.getData().size() > 0) {
                // Here we create a bundle called extras , get the key value data
                //  of the push data and store it in a map and run a for loop to store in the map
                Bundle extras = new Bundle();
                for (Map.Entry<String, String> entry : message.getData().entrySet()) {
                    extras.putString(entry.getKey(), entry.getValue());
                }
                Log.e("TAG", "onReceived Mesaage Called"); // This shows the message
                //Now we pass on the push data to CleverTap API and it checks
                // if the information / data is generated from CleverTap or not
                // if yes, the notification is passed on to firebase and then device
                NotificationInfo info = CleverTapAPI.getNotificationInfo(extras);
                if (info.fromCleverTap) {
                    CleverTapAPI.createNotification(getApplicationContext(), extras);
                }

            }
        } catch (Throwable t) {
            Log.d("MYFCMLIST", "Error parsing FCM message", t);
        }
    }

    //In the method onNewToken, the firebase generates a new token for the device
    // according to its rules, and when that happens, the below code
    // will send the new token to CleverTap

    @Override
    public void onNewToken(String token) {
        CleverTapAPI.getDefaultInstance(this).pushFcmRegistrationId(token, true);
        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        //sendRegistrationToServer(token);
    }


}