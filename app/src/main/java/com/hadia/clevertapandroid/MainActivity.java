package com.hadia.clevertapandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.clevertap.android.geofence.CTGeofenceAPI;
import com.clevertap.android.geofence.CTGeofenceSettings;
import com.clevertap.android.sdk.CTInboxListener;
import com.clevertap.android.sdk.CTInboxStyleConfig;
import com.clevertap.android.sdk.CleverTapAPI;
import com.clevertap.android.sdk.InAppNotificationButtonListener;
import com.clevertap.android.sdk.pushnotification.CTPushNotificationListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

import com.mixpanel.android.mpmetrics.MixpanelAPI;

public class MainActivity extends AppCompatActivity implements CTInboxListener, CTPushNotificationListener {
    Context ctx;
    public CleverTapAPI clevertapDefaultInstance;
    private final int REQUEST_LOCATION_PERMISSION = 1;
    Button btnAppInbox = findViewById(R.id.btnAppInbox);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //mixpanel
        boolean trackAutomaticEvents = true;
        MixpanelAPI mixpanel = MixpanelAPI.getInstance(this, "9c0dd2d3df892c38df240c28f47a445d", trackAutomaticEvents);

        requestLocationPermission(); //request location permission at runtime

        ctx = getApplicationContext();
        clevertapDefaultInstance = CleverTapAPI.getDefaultInstance(ctx); //init clevertap sdk
        CleverTapAPI.setDebugLevel(CleverTapAPI.LogLevel.DEBUG);     //Set Log level to VERBOSE
        clevertapDefaultInstance.setCTPushNotificationListener(this);
        clevertapDefaultInstance.setCTNotificationInboxListener(this);

        //CleverTapAPI clevertapDefaultInstance = CleverTapAPI.getDefaultInstance(getApplicationContext()); //init clevertap sdk

        //get and set location
        clevertapDefaultInstance.enableDeviceNetworkInfoReporting(true);
        //requires Location Permission in AndroidManifest e.g. "android.permission.ACCESS_COARSE_LOCATION"
        Location location = clevertapDefaultInstance.getLocation();
        //do something with location, optionally set on CleverTap for use in segmentation etc
        clevertapDefaultInstance.setLocation(location);

        //CTGeofenceAPI.getInstance(getApplicationContext()).init(ctGeofenceSettings,CleverTapAPI);

        //init app inbox - this is broken lol
        if (clevertapDefaultInstance != null) {
            //Set the Notification Inbox Listener
            clevertapDefaultInstance.setCTNotificationInboxListener(this);
            //Initialize the inbox and wait for callbacks on overridden methods
            clevertapDefaultInstance.initializeInbox();
        }

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
        String[] otherStuff = {"Jeans", "Perfume"};
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

        String[] roseStuff = {"Purse", "Dress"};
        roseDoeProfileUpdate.put("MyStuff", roseStuff);

        Button btnLoginUser1 = findViewById(R.id.btnLoginUser1);
        btnLoginUser1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clevertapDefaultInstance.onUserLogin(jackMontanaProfileUpdate);
                clevertapDefaultInstance.pushEvent("userLoggedIn");
            }
        });

        Button btnLoginUser2 = findViewById(R.id.btnLoginUser2);
        btnLoginUser2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clevertapDefaultInstance.onUserLogin(roseDoeProfileUpdate);
                clevertapDefaultInstance.pushEvent("userLoggedIn");
            }
        });

        //======================================================================================================================================================================================
        //PUSH NOTIFICATIONS
        //CleverTapAPI.createNotificationChannelGroup(getApplicationContext(),"PushNotifsGroupID","PushNotifsGroupName");
        //CleverTapAPI.createNotificationChannel(getApplicationContext(),"YourChannelId","YourChannelName","YourChannelDescription", NotificationManager.IMPORTANCE_MAX,"PushNotifsGroupID",true);
        CleverTapAPI.createNotificationChannel(getApplicationContext(), "YourChannelId", "Your Channel Name", "Your Channel Description", NotificationManager.IMPORTANCE_MAX, true);

        //EVENTS
        // event with properties
        HashMap<String, Object> prodViewedAction = new HashMap<String, Object>();
        prodViewedAction.put("Product Name", "Casio Chronograph Watch");
        prodViewedAction.put("Category", "Mens Accessories");
        prodViewedAction.put("Price", 65.99);
        prodViewedAction.put("Date", new java.util.Date());

        clevertapDefaultInstance.pushEvent("Product viewed - Test CleverTap", prodViewedAction);

/*      //Geofencing settings
        CTGeofenceSettings ctGeofenceSettings = new CTGeofenceSettings.Builder()
                .enableBackgroundLocationUpdates(bgLocation)//boolean to enable background location updates
                .setLogLevel(logLevel)//Log Level
                .setLocationAccuracy(accuracy)//byte value for Location Accuracy
                .setLocationFetchMode(fetchMode)//byte value for Fetch Mode
                .setGeofenceMonitoringCount(geofenceCount)//int value for number of Geofences CleverTap can monitor
                .setInterval(interval)//long value for interval in milliseconds
                .setFastestInterval(fastestInterval)//long value for fastest interval in milliseconds
                .setSmallestDisplacement(displacement)//float value for smallest Displacement in meters
                .setGeofenceNotificationResponsiveness(geofenceNotificationResponsiveness)// int value for geofence notification responsiveness in milliseconds
                .build();*/
    }

    //REQUEST PERMISSIONS
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @AfterPermissionGranted(REQUEST_LOCATION_PERMISSION)
    public void requestLocationPermission() {
        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION};
        if(EasyPermissions.hasPermissions(this, perms)) {
            Toast.makeText(this, "Permission already granted", Toast.LENGTH_SHORT).show();
        }
        else {
            EasyPermissions.requestPermissions(this, "Please grant the location permission", REQUEST_LOCATION_PERMISSION, perms);
        }
    }

    //APP INBOX METHODS!!!!
    @Override
    //init app inbox
    public void inboxDidInitialize() {
        btnAppInbox.setOnClickListener(v -> {
            ArrayList<String> tabs = new ArrayList<>();
            tabs.add("Promotions");
            tabs.add("Offers");//We support up to 2 tabs only. Additional tabs will be ignored

            CTInboxStyleConfig styleConfig = new CTInboxStyleConfig();
            styleConfig.setFirstTabTitle("First Tab");
            styleConfig.setTabs(tabs);//Do not use this if you don't want to use tabs
            styleConfig.setTabBackgroundColor("#FF0000");
            styleConfig.setSelectedTabIndicatorColor("#0000FF");
            styleConfig.setSelectedTabColor("#0000FF");
            styleConfig.setUnselectedTabColor("#FFFFFF");
            styleConfig.setBackButtonColor("#FF0000");
            styleConfig.setNavBarTitleColor("#FF0000");
            styleConfig.setNavBarTitle("MY INBOX");
            styleConfig.setNavBarColor("#FFFFFF");
            styleConfig.setInboxBackgroundColor("#ADD8E6");
            if (clevertapDefaultInstance != null) {
                clevertapDefaultInstance.showAppInbox(styleConfig); //With Tabs
            }
            //ct.showAppInbox();//Opens Activity with default style configs
        });
    }

    @Override
    public void inboxMessagesDidUpdate() {
        updateInboxButton();
    }

    private void updateInboxButton() {
        if (clevertapDefaultInstance == null) {
            return;
        }
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (btnAppInbox == null) {
                    return;
                }
                final int messageCount = clevertapDefaultInstance.getInboxMessageCount();
                final int unreadMessageCount = clevertapDefaultInstance.getInboxMessageUnreadCount();
                btnAppInbox.setText(String.format(Locale.getDefault(),"Inbox: %d messages /%d unread", messageCount, unreadMessageCount));
                btnAppInbox.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onNotificationClickedPayloadReceived(HashMap<String, Object> payload) {
        //Use your custom logic for  the payload
        Log.d("CT -------<>-----",payload.toString());
        if(payload != null){
            //Read the values
            Log.d("CT Not null-------<>-----",payload.toString());
/*            Log.d("Teste In App", payload.toString());
            Intent intent = new Intent(this, CallByPushActivity.class);
            startActivity(intent);*/
        }

    }
}
