//Here also we will put the cleverTap Varibale and initialize the SDK as we did in the MainActivty.java

package com.example.saiclevertapdemoandroidapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.clevertap.android.sdk.CTInboxListener;
import com.clevertap.android.sdk.CTInboxStyleConfig;
import com.clevertap.android.sdk.CleverTapAPI;

import java.util.ArrayList;
import java.util.HashMap;

public class HomeActivity extends AppCompatActivity implements CTInboxListener {

    //13. Creating Buttons for the HomeActivity Page
    Button UpdateProfile,AddToCart,ProductView,Charge,AppInbox,Other;
    CleverTapAPI clevertapDefaultInstance;
    TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Initialize the CleverTap SDK for this Page
        clevertapDefaultInstance = CleverTapAPI.getDefaultInstance(getApplicationContext());


        //21.Initializing the In app notification Listner
        //Set the Notification Inbox Listener
        clevertapDefaultInstance.setCTNotificationInboxListener(this);
        //Initialize the inbox and wait for callbacks on overridden methods
        clevertapDefaultInstance.initializeInbox();


        //14. First Here we find the view for all the buttons in the HomePage [HomeActivity.java]
        UpdateProfile = findViewById(R.id.update_profile);
        AddToCart = findViewById(R.id.add_to_cart);
        ProductView = findViewById(R.id.product_view);
        Charge = findViewById(R.id.charged);
        AppInbox = findViewById(R.id.App_Inbox);
        Other = findViewById(R.id.Other);
        textView = findViewById(R.id.textView);

        //30.This will give us the Customer_type, here which is given as gold in the other button onclick method.
        //We get the customer_type and then pass it on to the textView in the home activity xml file / home activity app page
        String customerType = (String) clevertapDefaultInstance.getProperty("Customer_type");
        textView.setText(customerType);




        //15. Setting OnClickListner events for all the buttons

        UpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //16. Now we are updating the profile via a HashMap , here we will update the address and add a new property Language
                HashMap<String, Object> profileUpdate = new HashMap<String, Object>();
                profileUpdate.put("Address", "Chennai");
                profileUpdate.put("Language", "Tamil");
                //As we have initialized the clevertap sdk above, we can use CleverTap Methods
                // Here We push the ProfileUpdate into CleverTap and these properties will be added to our profile.
                clevertapDefaultInstance.pushProfile(profileUpdate);



            }
        });



        ProductView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 17. We can raise a event with properties,and we push the event product viewed

                HashMap<String, Object> prodViewedAction = new HashMap<String, Object>();
                prodViewedAction.put("Product Name", "Casio Chronograph Watch");
                prodViewedAction.put("Category", "Mens Accessories");
                prodViewedAction.put("Price", 59.99);
                prodViewedAction.put("Date", new java.util.Date());

                clevertapDefaultInstance.pushEvent("Product viewed", prodViewedAction);



            }
        });


        //Here the product that is viewed that is added to the cart
        AddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                HashMap<String, Object> prodViewedAction = new HashMap<String, Object>();
                prodViewedAction.put("Product Name", "Casio Chronograph Watch");
                prodViewedAction.put("Category", "Mens Accessories");
                prodViewedAction.put("Price", 59.99);
                prodViewedAction.put("Date", new java.util.Date());

                clevertapDefaultInstance.pushEvent("Add To Cart", prodViewedAction);

            }
        });


        // 18. Charged is a special event, where there can be more than 1 product in the Cart
        // we store the Charged Details in a hashmap, which includes ChargeDetails such as amount, payment mode, charged id [Transaction id]
        // We also store individual product information the hashmap and then we add them in a Arraylist
        // then push the event to clevertap

        Charge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                HashMap<String, Object> chargeDetails = new HashMap<String, Object>();
                chargeDetails.put("Amount", 300);
                chargeDetails.put("Payment Mode", "Credit card");
                chargeDetails.put("Charged ID", 24052013);

                HashMap<String, Object> item1 = new HashMap<String, Object>();
                item1.put("Product category", "books");
                item1.put("Book name", "The Millionaire next door");
                item1.put("Quantity", 1);

                HashMap<String, Object> item2 = new HashMap<String, Object>();
                item2.put("Product category", "books");
                item2.put("Book name", "Achieving inner zen");
                item2.put("Quantity", 1);

                HashMap<String, Object> item3 = new HashMap<String, Object>();
                item3.put("Product category", "books");
                item3.put("Book name", "Chuck it, let's do it");
                item3.put("Quantity", 5);

                ArrayList<HashMap<String, Object>> items = new ArrayList<HashMap<String, Object>>();
                items.add(item1);
                items.add(item2);
                items.add(item3);

                clevertapDefaultInstance.pushChargedEvent(chargeDetails, items);



            }
        });



        //29. Here we will do a personalised profile push
        Other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, Object> profileUpdate = new HashMap<String, Object>();
                profileUpdate.put("Customer_type", "Gold");
                clevertapDefaultInstance.pushProfile(profileUpdate);

            }
        });


    }



    //19.This is used for the AppInbox Messages Callback Functions
    // We implemented the CTInboxListener on the top [HomeActivityClass]
    // We define the Inbox tabs and its style configuration

    @Override
    public void inboxDidInitialize() {


  //20. We define the AppInnbox onlicklistner inside the InboxDidInilialize, because when the inbox initializes then the AppInbox Button gets activated
        // and when we click it will take us to the inbox.
        AppInbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //We can only show 2 tabs in the inbox
                ArrayList<String> tabs = new ArrayList<>();
                tabs.add("promotions");
                tabs.add("offers");

                CTInboxStyleConfig styleConfig = new CTInboxStyleConfig();
                styleConfig.setTabs(tabs); //Do not use this if you don't want to use tabs

                styleConfig.setTabBackgroundColor("#FF0000");//provide Hex code in string ONLY
                styleConfig.setSelectedTabIndicatorColor("#0000FF");
                styleConfig.setSelectedTabColor("#000000");
                styleConfig.setUnselectedTabColor("#FFFFFF");
                styleConfig.setBackButtonColor("#FF0000");
                styleConfig.setNavBarTitleColor("#FF0000");
                styleConfig.setNavBarTitle("MY INBOX");
                styleConfig.setNavBarColor("#FFFFFF");
                styleConfig.setInboxBackgroundColor("#00FF00");
                clevertapDefaultInstance.showAppInbox(styleConfig); //Opens activity tith Tabs


            }
        });



    }




    @Override
    public void inboxMessagesDidUpdate() {

    }
}

