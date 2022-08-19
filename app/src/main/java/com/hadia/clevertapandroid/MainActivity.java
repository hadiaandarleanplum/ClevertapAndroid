package com.hadia.clevertapandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.clevertap.android.sdk.CleverTapAPI;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CleverTapAPI clevertapDefaultInstance = CleverTapAPI.getDefaultInstance(getApplicationContext());
        CleverTapAPI.setDebugLevel(CleverTapAPI.LogLevel.DEBUG);     //Set Log level to VERBOSE

        //======================================================================================================================================================================================
        //JACK MONTANA USER PROPERTIES
        // each of the below mentioned fields are optional
        HashMap<String, Object> jackMontanaProfileUpdate = new HashMap<String, Object>();
        jackMontanaProfileUpdate.put("Name", "Jack Montana");    // String
        jackMontanaProfileUpdate.put("Identity", 61026032);      // String or number
        jackMontanaProfileUpdate.put("Email", "jack@gmail.com"); // Email address of the user
        jackMontanaProfileUpdate.put("Phone", "+14155551234");   // Phone (with the country code, starting with +)
        jackMontanaProfileUpdate.put("Gender", "M");             // Can be either M or F
        //profileUpdate.put("DOB", new Date());         // Date of Birth. Set the Date object to the appropriate value first
// optional fields. controls whether the user will be sent email, push etc.
        jackMontanaProfileUpdate.put("MSG-email", false);        // Disable email notifications
        jackMontanaProfileUpdate.put("MSG-push", true);          // Enable push notifications
        jackMontanaProfileUpdate.put("MSG-sms", false);          // Disable SMS notifications
        jackMontanaProfileUpdate.put("MSG-whatsapp", true);      // Enable WhatsApp notifications

        ArrayList<String> jackStuff = new ArrayList<String>();
        jackStuff.add("bag");
        jackStuff.add("shoes");
        jackMontanaProfileUpdate.put("MyStuff", jackStuff);                        //ArrayList of Strings
        String[] otherStuff = {"Jeans","Perfume"};
        jackMontanaProfileUpdate.put("MyStuff", otherStuff);                   //String Array, update user properties

        //ROSE DOE USER PROPERTIES
        HashMap<String, Object> roseDoeProfileUpdate = new HashMap<String, Object>();
        roseDoeProfileUpdate.put("Name", "Rose Doe");    // String
        roseDoeProfileUpdate.put("Identity", 81026032);      // String or number
        roseDoeProfileUpdate.put("Email", "rose@gmail.com"); // Email address of the user
        roseDoeProfileUpdate.put("Phone", "+24155551234");   // Phone (with the country code, starting with +)
        roseDoeProfileUpdate.put("Gender", "F");             // Can be either M or F
        //profileUpdate.put("DOB", new Date());         // Date of Birth. Set the Date object to the appropriate value first
// optional fields. controls whether the user will be sent email, push etc.
        roseDoeProfileUpdate.put("MSG-email", false);        // Disable email notifications
        roseDoeProfileUpdate.put("MSG-push", true);          // Enable push notifications
        roseDoeProfileUpdate.put("MSG-sms", false);          // Disable SMS notifications
        roseDoeProfileUpdate.put("MSG-whatsapp", true);      // Enable WhatsApp notifications

        String[] roseStuff = {"Purse","Dress"};
        roseDoeProfileUpdate.put("MyStuff", roseStuff);

        Button btnLoginUser1 = findViewById(R.id.btnLoginUser1);
        btnLoginUser1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v){
                clevertapDefaultInstance.onUserLogin(jackMontanaProfileUpdate);
            }
        });

        Button btnLoginUser2 = findViewById(R.id.btnLoginUser2);
        btnLoginUser2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v){
                clevertapDefaultInstance.onUserLogin(roseDoeProfileUpdate);
            }
        });

        //======================================================================================================================================================================================
        //PUSH NOTIFICATIONS
        //CleverTapAPI.createNotificationChannelGroup(getApplicationContext(),"PushNotifsGroupID","PushNotifsGroupName");
        //CleverTapAPI.createNotificationChannel(getApplicationContext(),"YourChannelId","YourChannelName","YourChannelDescription", NotificationManager.IMPORTANCE_MAX,"PushNotifsGroupID",true);
        CleverTapAPI.createNotificationChannel(getApplicationContext(),"YourChannelId","Your Channel Name","Your Channel Description",NotificationManager.IMPORTANCE_MAX,true);

        //FCM CUSTOM PUSH
        String fcmRegId = FirebaseInstanceId.getInstance().getToken();
        clevertapDefaultInstance.pushFcmRegistrationId(fcmRegId,true);

        //EVENTS
        Button btnAddedtoCart = findViewById(R.id.btnAddedtoCart);
        btnAddedtoCart.setOnClickListener(new View.OnClickListener() {
            //@Override
            public void onClick (View v){
                clevertapDefaultInstance.pushEvent("Added to Cart");
            }
        });
    }


}