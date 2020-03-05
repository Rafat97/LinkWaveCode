package com.example.linkwave;

import android.Manifest;
import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;
import com.pushbots.push.Pushbots;

import java.net.URL;

public class PushBotsNotification extends Application {
    public static final String CHANNEL_1_ID = "channel1";

    LocationRequest mLocationRequest;
    FusedLocationProviderClient mFusedLocationProviderClient;
    LocationCallback mLocationCallback;

    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        //Firebase.setAndroidContext(this);

        //Newer version of Firebase
        if(!FirebaseApp.getApps(this).isEmpty()) {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }
        // Initialize Pushbots Library
        createNotificationChannels();
        GetLocation();
        Pushbots.sharedInstance().init(this);
    }

    private void createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel1 = new NotificationChannel(
                    CHANNEL_1_ID,
                    "Channel 1",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel1.setDescription("This is Channel 1");


            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel1);

        }
    }
    private void GetLocation() {
        mFusedLocationProviderClient = new FusedLocationProviderClient(this);
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(10);
        mLocationRequest.setFastestInterval(5);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);




        mLocationCallback = new LocationCallback() {

            @Override
            public void onLocationResult(LocationResult locationResult) {
                Log.i("My_Project","onLocationResult Is called : "+locationResult.toString());
                Log.i("My_Project","onLocationResult Is called getAccuracy : "+locationResult.getLastLocation().getAccuracy()+"");
                Log.i("My_Project","onLocationResult Is called getProvider : "+locationResult.getLastLocation().getProvider()+"");
                String UserLatitude = locationResult.getLastLocation().getLatitude()+"";
                String UserLongitude = locationResult.getLastLocation().getLongitude()+"";

                SharedPreferences sharedPrefs  = getSharedPreferences("LOCATION_INFO", MODE_PRIVATE);
                SharedPreferences.Editor sharedPrefseditor;
                sharedPrefseditor = sharedPrefs.edit();
                sharedPrefseditor.putString("lat", UserLatitude);
                sharedPrefseditor.putString("lon", UserLongitude);
                sharedPrefseditor.putString("hospital", sharedPrefs.getString("hospital","" ));
                sharedPrefseditor.putString("police", sharedPrefs.getString("police","" ));
                sharedPrefseditor.putString("restaurant", sharedPrefs.getString("restaurant","" ));
                sharedPrefseditor.commit();

                mFusedLocationProviderClient.removeLocationUpdates(mLocationCallback);
                super.onLocationResult(locationResult);
            }

            @Override
            public void onLocationAvailability(LocationAvailability locationAvailability) {
                Log.i("My_Project","onLocationAvailability is called : "+locationAvailability.toString());
                if (!locationAvailability.isLocationAvailable()){
                    mFusedLocationProviderClient.removeLocationUpdates(mLocationCallback);

                }

                super.onLocationAvailability(locationAvailability);
            }

        };
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    Activity#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for Activity#requestPermissions for more details.
                return;
            }
        }
        mFusedLocationProviderClient.requestLocationUpdates(
                mLocationRequest,
                mLocationCallback,
                null
        );

    }
}
