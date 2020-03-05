package com.example.linkwave;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



public class NearbyLocationFragment extends Fragment {

    View myView;
    CardView MapButton;
    CardView MapButtonHospital;
    CardView MapButtonPolice;
    final int MY_PERMISSIONS_REQUEST_LOCATION = 102;
    public static int LOCATION_TYPE = 0;
    Context CurrentContext;
    public NearbyLocationFragment(Context CurrentContext) {
        // Required empty public constructor
        this.CurrentContext = CurrentContext;



    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        myView = inflater.inflate(R.layout.fragment_nearby_location, container, false);
        MapButton = myView.findViewById(R.id.MapButton);
        MapButtonHospital =  myView.findViewById(R.id.MapButtonHospital);
        MapButtonPolice =  myView.findViewById(R.id.MapButtonPolice);


        MapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LOCATION_TYPE = 1;
                LocationPermissionCheck(LOCATION_TYPE);
            }
        });
        MapButtonHospital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LOCATION_TYPE = 2;
                LocationPermissionCheck(LOCATION_TYPE);
            }
        });
        MapButtonPolice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LOCATION_TYPE = 3;
                LocationPermissionCheck(LOCATION_TYPE);
            }
        });


        return myView;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_LOCATION) {


            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //When PERMISSION is GRANTED
                LocationPermissionCheck(LOCATION_TYPE);
                //Intent it = new Intent(FirstPageActivity.this,NearbyPlaceActivity.class);
                // it.putExtra("NearestPlace_Type", "RESTAURENT");
                // startActivity(it);
                // finish();


            } else {

                Intent mySuperIntent = new Intent(this.CurrentContext, ErrorActivity.class);
                mySuperIntent.putExtra("Error_Title", "Location");
                mySuperIntent.putExtra("Error_Des", "Location Permission Not given ");
                startActivity(mySuperIntent);
                //finish();

            }


        }


        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    void LocationPermissionCheck(int Type){
        if (ActivityCompat.checkSelfPermission(this.CurrentContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this.CurrentContext, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //Do the main task

            //getLocation();

            // Intent myIntent = getIntent();
            // String place = myIntent.getExtras().getString("NearBy");

            // textView = findViewById(R.id.textView);
            if(Type == 1){
                Intent it = new Intent(this.CurrentContext,NearbyPlaceActivity.class);
                it.putExtra("NearestPlace_Type", "RESTAURANTS");
                startActivity(it);

            }
            else if(Type == 2){
                Intent it = new Intent(this.CurrentContext,NearbyPlaceActivity.class);
                it.putExtra("NearestPlace_Type", "HOSPITALS");
                startActivity(it);

            }
            else if(Type == 3){
                Intent it = new Intent(this.CurrentContext,NearbyPlaceActivity.class);
                it.putExtra("NearestPlace_Type", "POLICES");
                startActivity(it);

            }


        } else {

            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) && ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)) {
                //Toast.makeText(this, "shouldShowRequestPermissionRationale", Toast.LENGTH_SHORT).show();
                Location_RequestPermission();

            } else {
                Location_RequestPermission();
            }

        }

    }

    public void Location_RequestPermission() {

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
        }
    }
}
