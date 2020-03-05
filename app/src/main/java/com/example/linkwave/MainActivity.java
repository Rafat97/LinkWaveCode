package com.example.linkwave;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.linkwave.ExtraClass.TestJobService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pushbots.push.Pushbots;

import static com.example.linkwave.PushBotsNotification.CHANNEL_1_ID;

public class MainActivity extends AppCompatActivity {

    private NotificationManagerCompat notificationManager;
    FirebaseDatabase myFirebaseDatabase_UserInfo;
    DatabaseReference myDatabaseReference_UserInfo_reference;
    ValueEventListener myValueEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Pushbots.sharedInstance().registerForRemoteNotifications();

        /*
        Pushbots.sharedInstance().idsCallback(new Pushbots.idHandler() {

            @Override
            public void userIDs(String userId, String registrationId) {
                if (registrationId != null && userId != null){
                    Log.d("PB3", "Registration ID:" + registrationId + " | userId:" + userId);

                    //Customer profile
                    Pushbots.sharedInstance().setFirstName("Rafat ");
                    Pushbots.sharedInstance().setLastName("Haque");
                    Pushbots.sharedInstance().setName("Rafat Haque");
                    Pushbots.sharedInstance().setGender("M");

                }
            }
        });
*/



        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN , WindowManager.LayoutParams.FLAG_FULLSCREEN);
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);





        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

/*
        notificationManager = NotificationManagerCompat.from(this);

        String title = "Message Sent";
        String message = "test";
        Notification notification = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_1_ID)
                .setSmallIcon(R.drawable.linkrip_logo)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();
        notificationManager.notify(1, notification);

*/


        ComponentName componentName = new ComponentName(this, TestJobService.class);
        JobInfo info;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
             info = new JobInfo.Builder(123, componentName)
                    .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                    .setPersisted(true)
                    .setMinimumLatency( 2 * 1000)
                    .build();
        } else {
             info = new JobInfo.Builder(123, componentName)
                    .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                    .setPersisted(true)
                    .setPeriodic(2 * 1000)
                    .build();
        }


        JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        int resultCode = scheduler.schedule(info);
        if (resultCode == JobScheduler.RESULT_SUCCESS) {
            Log.d("", "Job scheduled");
        } else {
            Log.d("", "Job scheduling failed");
        }





        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {






                if (Build.VERSION.SDK_INT >= 21){
                    AlradyHaveUser();



                }else {
                    Intent myInternt_Error = new Intent(MainActivity.this,ErrorActivity.class);
                    myInternt_Error.putExtra("Error_Title","SDK ERROR");
                    myInternt_Error.putExtra("Error_Des","Sorry , Your Android Version does not Supported This App! ");
                    startActivity(myInternt_Error);
                }


            }
        }, 1000);



    }



    void  AlradyHaveUser(){
        if(FirebaseAuth.getInstance().getCurrentUser() != null){




            myFirebaseDatabase_UserInfo = FirebaseDatabase.getInstance();
            myDatabaseReference_UserInfo_reference = myFirebaseDatabase_UserInfo.getReference().child("USERS");
            myValueEventListener = myDatabaseReference_UserInfo_reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    //String value = dataSnapshot.getValue(String.class);
                    //Log.d(TAG, "Value is: " + value);


                    if(dataSnapshot.hasChild(FirebaseAuth.getInstance().getUid())){
                        Intent myInternt = new Intent(MainActivity.this, FirstPageActivity.class);
                        startActivity(myInternt);
                        myDatabaseReference_UserInfo_reference.removeEventListener(this);
                        finish();

                    }else{

                        Intent myInternt_Error = new Intent(MainActivity.this,ProfileActivity.class);
                        startActivity(myInternt_Error);
                        myDatabaseReference_UserInfo_reference.removeEventListener(this);
                        finish();
                    }

                   /*

                    for (DataSnapshot data : dataSnapshot.getChildren()) {


                        if (data.getKey().equals(FirebaseAuth.getInstance().getUid())) {

                            Intent myInternt = new Intent(LoginActivity.this, FirstPageActivity.class);
                            startActivity(myInternt);
                            finish();
                            return;

                        }
                    }
                    */
                    // Intent myInternt_Error = new Intent(MainActivity.this,ProfileActivity.class);
                    //  startActivity(myInternt_Error);
                    //  finish();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    finish();
                }
            });
        }
        else{
            Intent myInternt_Error = new Intent(MainActivity.this,LoginActivity.class);
            startActivity(myInternt_Error);
            finish();
        }
    }







    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
}
