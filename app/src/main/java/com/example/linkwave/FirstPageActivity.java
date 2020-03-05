package com.example.linkwave;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.linkwave.ExtraClass.User;
import com.firebase.client.Firebase;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.net.URL;


public class FirstPageActivity extends AppCompatActivity {

    private DrawerLayout myDrawerLayout;
    private NavigationView nv;
    // cRestaurant;
    CardView MapButton;
    Button VideoCalling;
    CardView MapButtonHospital;
    CardView MapButtonPolice;
    Button ContactButtomList;
    Button GroupChat;
    TextView mTitle;
    FirebaseDatabase myFirebaseDatabase_UserInfo;
    DatabaseReference myDatabaseReference_UserInfo_reference;
    ValueEventListener myDatabaseReference_UserInfo_ReferenceEventListener;
    Fragment CurrentFragent;
    CoordinatorLayout coordinatorLayoutShare;
    ExtendedFloatingActionButton extendedFloatinShare;
    LocationRequest mLocationRequest;
    FusedLocationProviderClient mFusedLocationProviderClient;
    LocationCallback mLocationCallback;
    ProgressDialog progress;

    public static NavigationView navigationView;
    final int MY_PERMISSIONS_REQUEST_LOCATION = 102;
    final int MY_PERMISSIONS_REQUEST_CONTACTLIST = 103;
    public static int LOCATION_TYPE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_page);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.Main_toobar);
        mTitle = (TextView) myToolbar.findViewById(R.id.toolbar_title);
        mTitle.setText("Welcome to LinkWave!");
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        myDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle myActionBarDrawerToggle = new ActionBarDrawerToggle(this, myDrawerLayout, myToolbar, R.string.Open_Nav_Drawer,R.string.Close_Nav_Drawer);
        myDrawerLayout.addDrawerListener(myActionBarDrawerToggle);
        myActionBarDrawerToggle.syncState();
//        MapButton = findViewById(R.id.MapButton);
//        VideoCalling = findViewById(R.id.VideoCalling);
//        MapButtonHospital =  findViewById(R.id.MapButtonHospital);
//        MapButtonPolice =  findViewById(R.id.MapButtonPolice);
//        ContactButtomList = findViewById(R.id.ContactButtom);
//        GroupChat = findViewById(R.id.GroupChatEnter);

        progress = new ProgressDialog(this);
        progress.setMessage("Please Wait ...");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(false);
        progress.setCanceledOnTouchOutside(false);

        coordinatorLayoutShare = findViewById(R.id.ExtendedFloatingShe);
        coordinatorLayoutShare.setVisibility(View.GONE);
        extendedFloatinShare = findViewById(R.id.ShareLoacation);
        CurrentFragent = new MessageChatFragment(this);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bottom_nav_bar);

        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment fragment;
                switch (menuItem.getItemId()){
                    case R.id.navigation_location:
                        fragment = new NearbyLocationFragment(FirstPageActivity.this);
                        CurrentFragent = fragment;
                        loadFragment(CurrentFragent);
                        coordinatorLayoutShare.setVisibility(View.VISIBLE);
                        break;
                    case R.id.navigation_message:
                        fragment = new MessageChatFragment(FirstPageActivity.this);
                        CurrentFragent = fragment;
                        loadFragment(CurrentFragent);
                        coordinatorLayoutShare.setVisibility(View.GONE);
                        break;
//                    case R.id.navigation_call:
//                        fragment = new CallFragment(FirstPageActivity.this);
//                        CurrentFragent = fragment;
//                        loadFragment(CurrentFragent);
//                        break;

                }
                return true;
            }
        });


        navigationView = (NavigationView) findViewById(R.id.nv_var);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                if (menuItem.getItemId() == R.id.nav_logout){
                    FirebaseAuth.getInstance().signOut();

                    Intent it = new Intent(FirstPageActivity.this,LoginActivity.class);
                    it.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(it);
                    finish();

                }
             /*   else if (menuItem.getItemId() == R.id.nav_Profile){
                    Toast.makeText(FirstPageActivity.this,"Not Work Profile",Toast.LENGTH_SHORT).show();
                }*/
                else if (menuItem.getItemId() == R.id.nav_Support){
                    Toast.makeText(FirstPageActivity.this,"This app was created by Emdadul Haque and Faraz Kabir",Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });




        loadFragment(CurrentFragent);

        extendedFloatinShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirstPageActivity.LOCATION_TYPE = 4;
                LocationPermissionCheck(FirstPageActivity.LOCATION_TYPE);
            }
        });

//
//        MapButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                FirstPageActivity.LOCATION_TYPE = 1;
//                LocationPermissionCheck(FirstPageActivity.LOCATION_TYPE);
//            }
//        });
//        MapButtonHospital.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                FirstPageActivity.LOCATION_TYPE = 2;
//                LocationPermissionCheck(FirstPageActivity.LOCATION_TYPE);
//            }
//        });
//        MapButtonPolice.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                FirstPageActivity.LOCATION_TYPE = 3;
//                LocationPermissionCheck(FirstPageActivity.LOCATION_TYPE);
//            }
//        });
//        ContactButtomList.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ContactButtomListPrermission();
//
//            }
//        });
//        VideoCalling.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Intent it = new Intent(FirstPageActivity.this,CallerActivity.class);
//                startActivity(it);
//
//            }
//        });
//
//        GroupChat.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent it = new Intent(FirstPageActivity.this,GroupChatRoomCreatActivity.class);
//                startActivity(it);
//
//            }
//        });
    }

    @Override
    protected void onStart() {
        myFirebaseDatabase_UserInfo = FirebaseDatabase.getInstance();
        myDatabaseReference_UserInfo_reference = myFirebaseDatabase_UserInfo.getReference().child("USERS").child(FirebaseAuth.getInstance().getUid());
        myDatabaseReference_UserInfo_ReferenceEventListener = myDatabaseReference_UserInfo_reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    User getUser = dataSnapshot.getValue(User.class);
                    View headerView = navigationView.getHeaderView(0);
                    TextView navUsername = (TextView) headerView.findViewById(R.id.UserName);
                    ImageView navImage = (ImageView) headerView.findViewById(R.id.profile_image);

                    String link = getUser.getProfilePicLink();
                    Picasso.get().load(link).into(navImage);
                    navUsername.setText(getUser.getFirstname() + " "+getUser.getLastname());
                    mTitle.setText("Hello " +getUser.getFirstname() + "!");
                }else{
                    FirebaseAuth.getInstance().signOut();
                    Intent it = new Intent(FirstPageActivity.this,LoginActivity.class);
                    it.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(it);
                    finish();
                    myDatabaseReference_UserInfo_reference.removeEventListener(this);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        super.onStart();
    }

    @Override
    protected void onDestroy() {
        myDatabaseReference_UserInfo_reference.removeEventListener(myDatabaseReference_UserInfo_ReferenceEventListener);
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if(myDrawerLayout.isDrawerOpen(GravityCompat.START)){
            myDrawerLayout.closeDrawer(GravityCompat.START);
        }else{
            finish();
            super.onBackPressed();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_LOCATION) {


            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //When PERMISSION is GRANTED
                LocationPermissionCheck(FirstPageActivity.LOCATION_TYPE);
                //Intent it = new Intent(FirstPageActivity.this,NearbyPlaceActivity.class);
               // it.putExtra("NearestPlace_Type", "RESTAURENT");
               // startActivity(it);
               // finish();


            } else {

                Intent mySuperIntent = new Intent(this, ErrorActivity.class);
                mySuperIntent.putExtra("Error_Title", "Location");
                mySuperIntent.putExtra("Error_Des", "Location Permission Not given ");
                startActivity(mySuperIntent);
                finish();

            }


        }
       else if (requestCode == MY_PERMISSIONS_REQUEST_CONTACTLIST) {


            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent it = new Intent(FirstPageActivity.this,ContactListActivity.class);
                startActivity(it);

            }
            else{
                Intent mySuperIntent = new Intent(this, ErrorActivity.class);
                mySuperIntent.putExtra("Error_Title", "Contact");
                mySuperIntent.putExtra("Error_Des", "Please give contact permission. ");
                startActivity(mySuperIntent);
                finish();
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        //transaction.addToBackStack(null);
        transaction.commit();
    }
    void LocationPermissionCheck(int Type){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //Do the main task

            //getLocation();

           // Intent myIntent = getIntent();
           // String place = myIntent.getExtras().getString("NearBy");

           // textView = findViewById(R.id.textView);
            if(Type == 1){
                Intent it = new Intent(FirstPageActivity.this,NearbyPlaceActivity.class);
                it.putExtra("NearestPlace_Type", "RESTAURANTS");
                startActivity(it);

            }
            else if(Type == 2){
                Intent it = new Intent(FirstPageActivity.this,NearbyPlaceActivity.class);
                it.putExtra("NearestPlace_Type", "HOSPITALS");
                startActivity(it);

            }
            else if(Type == 3){
                Intent it = new Intent(FirstPageActivity.this,NearbyPlaceActivity.class);
                it.putExtra("NearestPlace_Type", "POLICES");
                startActivity(it);

            }
            else if(Type == 4){
                progress.show();
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
                        progress.dismiss();
                        mFusedLocationProviderClient.removeLocationUpdates(this);

                        shareLocation(locationResult.getLastLocation().getLatitude()+"",locationResult.getLastLocation().getLongitude()+"");
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


        } else {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION) && ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                //Toast.makeText(this, "shouldShowRequestPermissionRationale", Toast.LENGTH_SHORT).show();
                Location_RequestPermission();

            } else {
                Location_RequestPermission();
            }

        }

    }

    void shareLocation(String lat,String lon){
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = "https://www.google.com/maps/search/?api=1&query="+lat+","+lon;
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Location");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share via LinkWave"));
    }
    public void Location_RequestPermission() {

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
        }
    }
    void ContactButtomListPrermission(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {


            Intent it = new Intent(FirstPageActivity.this,ContactListActivity.class);
            startActivity(it);
            finish();


        } else {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION) && ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                //Toast.makeText(this, "shouldShowRequestPermissionRationale", Toast.LENGTH_SHORT).show();
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, MY_PERMISSIONS_REQUEST_CONTACTLIST);
                }

            } else {
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, MY_PERMISSIONS_REQUEST_CONTACTLIST);
                }
            }

        }
    }
}