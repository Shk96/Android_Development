package com.example.saiclevertapdemoandroidapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.clevertap.android.sdk.CleverTapAPI;

import java.util.Date;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {


    // 1. Creating Variables for the text field and signup button and CleverTap API

    EditText email, phone, name, username, address;
    Button SignUp, skip;
    CleverTapAPI clevertapDefaultInstance;


    //4. As the data we enter in the fields will be a String, we create String Variables
    String textEmail, textPhone, textName, textUsername, textAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //12. This below code will help with the logs for debugging, these can be viewed below in logcat
        CleverTapAPI.setDebugLevel(CleverTapAPI.LogLevel.DEBUG);   //Set Log level to DEBUG log warnings or other important messages


        // 0. Below we are Initializing the CleverTap SDK
        // CleverTapAPI clevertapDefaultInstance = CleverTapAPI.getDefaultInstance(getApplicationContext());
        // This is the API that we get from CleverTap
        clevertapDefaultInstance = CleverTapAPI.getDefaultInstance(getApplicationContext());


        //28. CleverTap Profile Customization, this enables the personalised experience tp the app users
        // We will create a personalized experience demo [Profile push] on the other button in the HomeActivity file
        clevertapDefaultInstance.enablePersonalization();




        //25. Action Buttons in the push notifications, the below code will give us the information on which button was clicked by giving us the [action id]
        // As the below code is in OnCreateMethod, this gets executed with it and when we click on the notification button when the app is running we dont get the button id toast message
        // But if App is in close mode or is not opened, and then when we click on the notification, we get the action id as toast message.

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String actionId = extras.getString("actionId");
            if (actionId != null) {
                Log.d("ACTION_ID", actionId);

                //This below code vanishes/closes the notificaion once the button is clicked
                boolean autoCancel = extras.getBoolean("autoCancel", true);
                int notificationId = extras.getInt("notificationId", -1);
                if (autoCancel && notificationId > -1) {
                    NotificationManager notifyMgr =
                            (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
                    notifyMgr.cancel(notificationId);  // the bit that cancels the notification
                }

                Toast.makeText(getBaseContext(),"Action ID is: "+actionId,
                        Toast.LENGTH_SHORT).show();
            }
        }



        //23. PushNotification Channel Service, from Android 8.0 We need to create a notification channel through which the priority of the notifications can be set.
        // Creating a Notification Channel With Sound Support. here we have channel id [important], channelName and Channel Description [Keep something relevant]
        //While sending the push from CleverTap dashboard, we need to put the channelid = "st" , here so tha notification is sent
        CleverTapAPI.createNotificationChannel(getApplicationContext(), "st",
                "Stranger Things", "Stranger Things", NotificationManager.IMPORTANCE_MAX, true, "strangerthings_theme.mp3");


        // 2. Now initializing the above variables, we use findviewbyid to find a particular view and we will get all the views
        email = findViewById(R.id.text_email_id);
        phone = findViewById(R.id.text_phone);
        name = findViewById(R.id.text_name);
        username = findViewById(R.id.text_user_name);
        address = findViewById(R.id.text_address);
        SignUp = findViewById(R.id.button_signup);
        skip = findViewById(R.id.button_skip);


        // 3. Now we put a Setoncliclistener on the signup button, when the button is clicked, then the
        // OnClick Method is called and the data is passed to CleverTap

        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //5. We use the string variables, here to read the data and send it to the CleverTap API [Post Request]

                textEmail = email.getText().toString();
                textPhone = phone.getText().toString();
                textName = name.getText().toString();
                textUsername = username.getText().toString();
                textAddress = address.getText().toString();


                // 6. We can add information to userProfile [OnUserLogin] Data
                // We use a hashmap to store the data

                // each of the below mentioned fields are optional if set, these populate demographic information in the Dashboard

                HashMap<String, Object> profileUpdate = new HashMap<String, Object>();

                profileUpdate.put("Name", textName);                  // String
                profileUpdate.put("Identity", textUsername);                    // String or number
                profileUpdate.put("Email", textEmail);               // Email address of the user
                profileUpdate.put("Phone", textPhone);                 // Phone (with the country code, starting with +)
                profileUpdate.put("Sign Up date", new Date());                       // Date of Birth. Set the Date object to the appropriate value first
                profileUpdate.put("address", textAddress);                    //an abbreviation such as "PST", a full name such as "America/Los_Angeles",
                //or a custom ID such as "GMT-8:00"
                profileUpdate.put("Photo", "https://www.mediavillage.com/media/articles/blackpanther.jpg.550x380_q85_box-0%2C23%2C640%2C468_crop.jpg");    // URL to the Image

                // optional fields. controls whether the user will be sent email, push etc.

                profileUpdate.put("MSG-email", false);                      // Disable email notifications
                profileUpdate.put("MSG-push", true);                        // Enable push notifications
                profileUpdate.put("MSG-sms", false);                        // Disable SMS notifications
                profileUpdate.put("MSG-dndPhone", true);                  // Opt out phone                                                                    number from SMS                                                                  notifications
                profileUpdate.put("MSG-dndEmail", true);                  // Opt out phone                                                                    number from SMS                                                                  notifications


                // 7. We use the CleverTap Default Instance to push the profile
                clevertapDefaultInstance.pushProfile(profileUpdate);

                //8. Giving the User a Message after clicking the button, via a Toast Method
                Toast.makeText(MainActivity.this, "SignUp Successful", Toast.LENGTH_LONG).show();


                //12. After the user is signed up the goToHome function is called and the user is taken to the new activity [HomePage]

                goToHome();


            }
        });


        // 9. Now we put a Setoncliclistener on the skip button, when the skip button is clicked, OnClick Method is called
        // then the goToHome Function is executed and the user is taken to the sign in / home page
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //11. GoToHome function is used for taking the user to new activity [Home Page]
                goToHome();
            }
        });


    }

    private void goToHome() {
        //10. Intent is used for inter activity communication / navigating from one activity to another
        // Below [MainActivity.this] is the exiting activity and [HomeActivity.class] is the target activity
        // startActivity(intent) will start this activity and go to the target activity
        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
        startActivity(intent);
    }


//     26. So, when the app is running even in the background [which means the OnCreateMethod is already called], we can still get the Button type [Action id]
//     within the app, with the help of OnNewIntent method
//     if the App is closed and we click on the button, we dont get the toast message, we have click on the notification button again to get the action id
//     So we need to have the below code Bundle in both OnCreate and onNewIntent method to get the action id, everytime.

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        Bundle extras = intent.getExtras();
        if (extras != null) {
            String actionId = extras.getString("actionId");
            if (actionId != null) {
                Log.d("ACTION_ID", actionId);

                //This below code vanishes/closes the notificaion once the button is clicked
                boolean autoCancel = extras.getBoolean("autoCancel", true);
                int notificationId = extras.getInt("notificationId", -1);
                if (autoCancel && notificationId > -1) {
                    NotificationManager notifyMgr =
                            (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
                    notifyMgr.cancel(notificationId);  // the bit that cancels the notification
                }

                //The below code gives us the toast message
                Toast.makeText(getBaseContext(), "Action ID is: " + actionId,
                        Toast.LENGTH_SHORT).show();
            }
        }

    }


    //27. We can enable Push Impressions also known as Notification viewed in the analytics report
    //by going to settings < schema < events < push impressions < click on 3 dots < enable push impressions
    // this way we can see how many times our notification is viewed

}