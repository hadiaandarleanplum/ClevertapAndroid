package com.hadia.clevertapandroid;

import static com.clevertap.android.sdk.inapp.customtemplates.CustomTemplatesExtKt.templatesSet;

import static pub.devrel.easypermissions.EasyPermissions.hasPermissions;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
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
import android.widget.TextView;
import android.widget.Toast;

import com.clevertap.android.geofence.CTGeofenceAPI;
import com.clevertap.android.geofence.CTGeofenceSettings;
//import com.clevertap.android.sdk.CTInboxActivity;
import com.clevertap.android.sdk.ActivityLifecycleCallback;
import com.clevertap.android.sdk.CTInboxListener;
import com.clevertap.android.sdk.CTInboxStyleConfig;
import com.clevertap.android.sdk.CTWebInterface;
import com.clevertap.android.sdk.CleverTapAPI;
import com.clevertap.android.sdk.CleverTapInstanceConfig;
import com.clevertap.android.sdk.InAppNotificationListener;
import com.clevertap.android.sdk.InAppNotificationButtonListener;
import com.clevertap.android.sdk.PushPermissionResponseListener;
import com.clevertap.android.sdk.inapp.CTInAppNotification;
import com.clevertap.android.sdk.inapp.customtemplates.CustomTemplate;
import com.clevertap.android.sdk.inapp.customtemplates.CustomTemplateContext;
import com.clevertap.android.sdk.inapp.customtemplates.CustomTemplatesExtKt;
import com.clevertap.android.sdk.pushnotification.CTPushNotificationListener;
import com.clevertap.android.sdk.displayunits.DisplayUnitListener;
import com.clevertap.android.sdk.displayunits.model.CleverTapDisplayUnit;

import java.io.File;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

import com.clevertap.android.sdk.variables.Var;
import com.clevertap.android.sdk.variables.callbacks.FetchVariablesCallback;
import com.clevertap.android.sdk.variables.callbacks.VariablesChangedCallback;
import com.google.android.gms.tasks.Task;
import com.google.android.play.core.review.ReviewException;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.google.android.play.core.review.model.ReviewErrorCode;
import com.google.android.play.core.review.testing.FakeReviewManager;
import com.mixpanel.android.mpmetrics.MixpanelAPI;

//request play store - this wont import for some reason lol after i imported the other play core thing
/*import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.google.android.play.core.tasks.Task;*/

public class MainActivity extends AppCompatActivity implements PushPermissionResponseListener, CTInboxListener, InAppNotificationButtonListener {
    //Context ctx;
    public CleverTapAPI clevertapDefaultInstance;

    private final int REQUEST_LOCATION_PERMISSION = 1;

    long longValue = 1234L;
    long longValue2 = 12345L;
    long longValue3 = 123456L;
    long longValue5 = 1234567L;

   // I REMOVED THIS FROM THE ANDROID MANIFEST KEEPING IT HERE
    //
    //android:name="com.hadia.clevertapandroid.registerCustomInAppTemplates"

    // Define a specific date in UTC
    ZonedDateTime utcDateTime = ZonedDateTime.of(2024, 11, 20, 8, 0, 0, 0, ZoneId.of("UTC"));

    // Convert to Instant for UTC representation
    Instant instant = utcDateTime.toInstant();

    //Button btnAppInbox = findViewById(R.id.btnAppInbox);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActivityLifecycleCallback.register(this.getApplication());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Get the custom Application class
        //registerCustomInAppTemplates app = (registerCustomInAppTemplates) getApplication();

        // Access the template presenter
        //MyTemplatePresenter templatePresenter = app.getTemplatePresenter();

        // Set the Activity or any other configurations on the presenter
       /* if (templatePresenter != null) {
            templatePresenter.setActivity(this); // Assuming your presenter has a setActivity method
        }*/


        CleverTapAPI.setDebugLevel(CleverTapAPI.LogLevel.VERBOSE);
        System.out.println("VERBOSE DEBUG LEVEL");
        //clevertapDefaultInstance.setCTPushNotificationListener(this);
        //clevertapDefaultInstance.setCTNotificationInboxListener(this);

        clevertapDefaultInstance = CleverTapAPI.getDefaultInstance(getApplicationContext()); //init clevertap sdk


        if (clevertapDefaultInstance != null) {
            // Fetch CleverTap ID
            clevertapDefaultInstance.getCleverTapID(cleverTapID -> {
                // Use the retrieved CleverTap ID
                Log.d("CleverTapID", "CleverTap Device ID: " + cleverTapID);
                System.out.println("CleverTap Device ID: " + cleverTapID);
            });
        } else {
            Log.e("CleverTap", "Failed to initialize CleverTapAPI instance");
        }


        //Use this API to get all messages in your inbox.
        /*clevertapDefaultInstance.getAllInboxMessages();

        // Retrieve the first message ID from the CleverTap inbox messages
        String firstMessageId = clevertapDefaultInstance != null && clevertapDefaultInstance.getAllInboxMessages() != null
                ? clevertapDefaultInstance.getAllInboxMessages().isEmpty() ? null : clevertapDefaultInstance.getAllInboxMessages().get(0).getMessageId()
                : null;

        // Check if the message ID is null and execute accordingly
        if (firstMessageId != null) {
            // Push the Inbox Notification Clicked Event
            clevertapDefaultInstance.pushInboxNotificationClickedEvent(firstMessageId);
            Log.i("CLEVERTAP APP INBOX", "Raised Notification Clicked event For Id = " + firstMessageId);
        } else {
            // Log a message if the inbox message ID is null
            Log.i("CLEVERTAP APP INBOX", "inboxMessage Id is null");
        }*/

        //DECLARE AND SYNC VARIABLES
        Var<Integer> var3 = clevertapDefaultInstance.defineVariable("var_int3", 4);
        Var<String> myString = clevertapDefaultInstance.defineVariable("myString", "this is my string not default");

        Var<String> AD_SEGMENT_GROUP = clevertapDefaultInstance.defineVariable("AD_SEGMENT_GROUP", "default");


        // invoked on app start and whenever vars are fetched from server
        clevertapDefaultInstance.addVariablesChangedCallback(new VariablesChangedCallback() {
            @Override
            public void variablesChanged() {
                Log.d("CLEVERTAP VARIABLES", "CLEVERTAP STRING VARIABLE VALUE VariablesChangedCallback" + myString);
                Log.d("CLEVERTAP VARIABLES", "CLEVERTAP ad_segment_group VARIABLE VALUE onVariablesFetchedCallback" + AD_SEGMENT_GROUP);
            }
        });

        clevertapDefaultInstance.syncVariables();
        //VARIABLES END


        clevertapDefaultInstance.registerPushPermissionNotificationResponseListener(this);

        if (clevertapDefaultInstance != null) {
            //Set the Notification Inbox Listener
            clevertapDefaultInstance.setCTNotificationInboxListener(this);
            //Initialize the inbox and wait for callbacks on overridden methods
            clevertapDefaultInstance.initializeInbox();
        }

        Log.d("CLEVERTAP VARIABLES", "CLEVERTAP STRING VARIABLE VALUE APP LAUNCHED" + myString);

        //GET AND SET LOCATION
        //clevertapDefaultInstance.enableDeviceNetworkInfoReporting(true);
        //requires Location Permission in AndroidManifest e.g. "android.permission.ACCESS_COARSE_LOCATION"
        //Location location = clevertapDefaultInstance.getLocation();
        //do something with location, optionally set on CleverTap for use in segmentation etc
        //clevertapDefaultInstance.setLocation(location);
        //clevertapDefaultInstance.setDisplayUnitListener(this);

        //CTGeofenceAPI.getInstance(getApplicationContext()).init(ctGeofenceSettings,CleverTapAPI);

        //init app inbox - this is broken lol
/*        if (clevertapDefaultInstance != null) {
            //Set the Notification Inbox Listener
            //clevertapDefaultInstance.setCTNotificationInboxListener(this);
            //Initialize the inbox and wait for callbacks on overridden methods
            //clevertapDefaultInstance.initializeInbox();
        }*/

        //======================================================================================================================================================================================
        //LOGIN USERS

        //JACK MONTANA USER PROPERTIES
        // each of the below mentioned fields are optional
        HashMap<String, Object> jackMontanaProfileUpdate = new HashMap<String, Object>();
        //jackMontanaProfileUpdate.put("Name", "Jack Montana10");    // String
        jackMontanaProfileUpdate.put("identity", 71026037);      // String or number
        //jackMontanaProfileUpdate.put("email", "hadiaandar@gmail.com"); // Email address of the user
        //jackMontanaProfileUpdate.put("Phone", "+14125551239");   // Phone (with the country code, starting with +)
        jackMontanaProfileUpdate.put("Gender", "");             // Can be either M or F
        //jackMontanaProfileUpdate.put("DOB", new Date("2024-11-20T08:00:00Z"));         // Date of Birth. Set the Date object to the appropriate value first
// optional fields. controls whether the user will be sent email, push etc.
        jackMontanaProfileUpdate.put("MSG-email", false);        // Disable email notifications
        jackMontanaProfileUpdate.put("MSG-push", true);          // Enable push notifications
        jackMontanaProfileUpdate.put("MSG-sms", false);          // Disable SMS notifications
        jackMontanaProfileUpdate.put("MSG-whatsapp", true);      // Enable WhatsApp notifications
        jackMontanaProfileUpdate.put("last_name", "blue");      // custom test for recargapay

        jackMontanaProfileUpdate.put("longValue", longValue);
        jackMontanaProfileUpdate.put("longValue2", longValue2);
        jackMontanaProfileUpdate.put("longValue3", longValue3);
        jackMontanaProfileUpdate.put("longValue5", longValue5);
        jackMontanaProfileUpdate.put("integervalue", 1);

        ArrayList<String> jackStuff = new ArrayList<String>();
        jackStuff.add("bag");
        jackStuff.add("shoes");
        jackMontanaProfileUpdate.put("MyStuff", jackStuff);                        //ArrayList of Strings

        ArrayList<Integer> intStuff = new ArrayList<Integer>();
        intStuff.add(1);
        jackMontanaProfileUpdate.put("intStuff", intStuff);                        //ArrayList of Int

        String[] otherStuff = {"Jeans", "Perfume"};
        jackMontanaProfileUpdate.put("MyStuff", otherStuff);                   //String Array, update user properties

        //AUTO LOGIN
        //clevertapDefaultInstance.onUserLogin(jackMontanaProfileUpdate);
        //clevertapDefaultInstance.pushEvent("myFunctionEvent");


        //ROSE DOE USER PROPERTIES
        HashMap<String, Object> roseDoeProfileUpdate = new HashMap<String, Object>();
        roseDoeProfileUpdate.put("Name", "Rose Doe");    // String
        roseDoeProfileUpdate.put("identity", 71026037);      // String or number
        //roseDoeProfileUpdate.put("email", "hadia.andar@clevertap.com"); // Email address of the user
        //roseDoeProfileUpdate.put("Phone", "+24155551234");   // Phone (with the country code, starting with +)
        roseDoeProfileUpdate.put("Gender", "F");             // Can be either M or F
        //roseDoeProfileUpdate.put("DOB", new Date("2024-11-20T08:00:00Z"));         // Date of Birth. Set the Date object to the appropriate value first
// optional fields. controls whether the user will be sent email, push etc.
        roseDoeProfileUpdate.put("MSG-email", true);        // Disable email notifications
        roseDoeProfileUpdate.put("MSG-push", true);          // Enable push notifications
        roseDoeProfileUpdate.put("MSG-sms", false);          // Disable SMS notifications
        roseDoeProfileUpdate.put("MSG-whatsapp", true);      // Enable WhatsApp notifications
        roseDoeProfileUpdate.put("last_name", "blue");      // custom test for recargapay

        roseDoeProfileUpdate.put("longValue", longValue);
        roseDoeProfileUpdate.put("longValue2", longValue2);
        roseDoeProfileUpdate.put("longValue3", longValue3);
        roseDoeProfileUpdate.put("longValue4", "hello");

        String[] roseStuff = {"Purse", "Dress"};
        roseDoeProfileUpdate.put("MyStuff", roseStuff);


        // Register CleverTap event listener
        Button btnLoginUser1 = findViewById(R.id.btnLoginUser1);
        btnLoginUser1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clevertapDefaultInstance.onUserLogin(jackMontanaProfileUpdate);
                clevertapDefaultInstance.pushEvent("myFunctionEvent");
                clevertapDefaultInstance.syncRegisteredInAppTemplates();
            }
        });

        Button btnLoginUser2 = findViewById(R.id.btnLoginUser2);
        btnLoginUser2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clevertapDefaultInstance.onUserLogin(roseDoeProfileUpdate);
                clevertapDefaultInstance.pushEvent("userLoggedIn");
                clevertapDefaultInstance.pushEvent("undefinedEventTest");
                roseDoeProfileUpdate.put("BABY_1_AGE_MONTHS", 1);
                roseDoeProfileUpdate.put("BABY_2_AGE_MONTHS", 1);
                clevertapDefaultInstance.syncRegisteredInAppTemplates();
            }
        });

        Button btnTriggerJourney = findViewById(R.id.btnTriggerJourney);
        btnTriggerJourney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //clevertapDefaultInstance.pushEvent("hadiaTriggerJourney");

                // each of the below mentioned fields are optional
// if set, these populate demographic information in the Dashboard
                HashMap<String, Object> profileUpdate = new HashMap<String, Object>();
                profileUpdate.put("Photo", "www.foobar.com/image.jpeg");    // URL to the Image
                profileUpdate.put("integervalue", 2);

                clevertapDefaultInstance.pushProfile(profileUpdate);

                HashMap<String, Object> prodViewedAction2 = new HashMap<String, Object>();
                prodViewedAction2.put("campaignID", 1725054603);

                //clevertapDefaultInstance.pushEvent("hadiaTriggerAppInbox", prodViewedAction2);
                clevertapDefaultInstance.syncRegisteredInAppTemplates();

                //Trigger two events for mutual llc journey
                clevertapDefaultInstance.pushEvent("account_deleted");
                clevertapDefaultInstance.pushEvent("delete_not_finding_anyone");

                //fetch variables
                clevertapDefaultInstance.fetchVariables(new FetchVariablesCallback() {
                    @Override
                    public void onVariablesFetched(boolean isSuccess) {
                        //System.out.println("CLEVERTAP VAR3 VARIABLE VALUE" + var3);
                        Log.d("CLEVERTAP VARIABLES", "CLEVERTAP STRING VARIABLE VALUE onVariablesFetchedCallback" + myString);
                        Log.d("CLEVERTAP VARIABLES", "CLEVERTAP ad_segment_group VARIABLE VALUE onVariablesFetchedCallback" + AD_SEGMENT_GROUP);
                        // isSuccess is true when server request is successful, false otherwise
                    }
                });

            }
        });

        Button btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CacheUtils.clearAppCache(getApplicationContext());
                AppDataUtils.clearAppData(getApplicationContext());

            }
        });

        //======================================================================================================================================================================================
        //PUSH NOTIFICATIONS
        //CleverTapAPI.createNotificationChannelGroup(getApplicationContext(),"PushNotifsGroupID","PushNotifsGroupName");
        //CleverTapAPI.createNotificationChannel(getApplicationContext(),"YourChannelId","YourChannelName","YourChannelDescription", NotificationManager.IMPORTANCE_MAX,"PushNotifsGroupID",true);
        CleverTapAPI.createNotificationChannel(getApplicationContext(), "YourChannelId", "Your Channel Name", "Your Channel Description", NotificationManager.IMPORTANCE_MAX, true);


        //EVENTS
        // product event with properties
        HashMap<String, Object> prodViewedAction = new HashMap<String, Object>();
        prodViewedAction.put("Product Name", "Casio Chronograph Watch");
        prodViewedAction.put("Category", "Mens Accessories");
        prodViewedAction.put("Price", 65.99);
        prodViewedAction.put("Date", new java.util.Date());

        clevertapDefaultInstance.pushEvent("Product viewed - Test CleverTap", prodViewedAction);



        //clevertapDefaultInstance.onShow

        //clevertapDefaultInstance.pushEvent("hadiaTriggerJourney");

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

   /* @AfterPermissionGranted(REQUEST_LOCATION_PERMISSION)
    public void requestLocationPermission() {
        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION};
        if(EasyPermissions.hasPermissions(this, perms)) {
            Toast.makeText(this, "Permission already granted", Toast.LENGTH_SHORT).show();
        }
        else {
            EasyPermissions.requestPermissions(this, "Please grant the location permission", REQUEST_LOCATION_PERMISSION, perms);
        }
    }*/

    @Override
    public void onPushPermissionResponse(boolean accepted) {
        Log.i("TAG", "onPushPermissionResponse :  InApp---> response() called accepted="+accepted);
        if(accepted){
            CleverTapAPI.createNotificationChannel(getApplicationContext(), "YourChannelId", "Your Channel Name", "Your Channel Description", NotificationManager.IMPORTANCE_MAX, true);

        }
    }

/*    @Override
    protected void onDestroy() {
        super.onDestroy();
        clevertapDefaultInstance.unregisterPushPermissionNotificationResponseListener(this);

    }*/

  /*  @Override
    public void onDisplayUnitsLoaded(ArrayList<CleverTapDisplayUnit> units) {
        // you will get display units here
        // implement your logic to create your display views using these Display Units here
        for (int i = 0; i <units.size() ; i++) {
            CleverTapDisplayUnit unit = units.get(i);
            prepareDisplayView(unit);
        }
    }*/


    //APP INBOX METHODS!!!!
    @Override
    public void inboxDidInitialize() {
        Button btnAppInbox = findViewById(R.id.btnAppInbox);

        btnAppInbox.setOnClickListener(v -> {
            ArrayList<String> tabs = new ArrayList<>();
            tabs.add("Promotions");
            tabs.add("Offers");//We support upto 2 tabs only. Additional tabs will be ignored

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

            clevertapDefaultInstance.showAppInbox(styleConfig); //With Tabs

            //Use this API to get all messages in your inbox.
            clevertapDefaultInstance.getAllInboxMessages();

            System.out.println(clevertapDefaultInstance.getAllInboxMessages().toString());

            //ct.showAppInbox();//Opens Activity with default style configs
        });
    }

    @Override
    public void inboxMessagesDidUpdate() {
        System.out.println("INBOX UPDATED");
        clevertapDefaultInstance.getAllInboxMessages();
    }

    @Override
    public void onInAppButtonClick(HashMap<String, String> payload) {
        //
    }

    //Clear app cache after user logs out
    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) { e.printStackTrace();}
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if(dir!= null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }
}















