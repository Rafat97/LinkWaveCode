package com.example.linkwave.ExtraClass;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.util.Log;

import com.example.linkwave.PushBotsNotification;
import com.example.linkwave.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import static com.example.linkwave.PushBotsNotification.CHANNEL_1_ID;

public class TestJobService extends JobService {
    private static final String TAG = "ExampleJobServiceTest";
    private boolean jobCancelled = false;
    ActivityManager myActivityManager ;
    ComponentName myComponentName ;
    Context context;
    private NotificationManagerCompat notificationManager;


    @Override
    public boolean onStartJob(JobParameters params) {
        Log.d(TAG, "Job started");
        notificationManager = NotificationManagerCompat.from(getApplicationContext());
        myActivityManager = (ActivityManager)getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        myComponentName = myActivityManager.getRunningTasks(1).get(0).topActivity;
        doBackgroundWork(params);
        return true;
    }

    private void doBackgroundWork(final JobParameters params) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){

                        myActivityManager = (ActivityManager)getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
                        myComponentName = myActivityManager.getRunningTasks(1).get(0).topActivity;
                    Log.d(TAG, "run: " + myComponentName.toString() + "\n" + myActivityManager.toString());


                        if(myComponentName.getClassName().equals("com.sec.android.app.launcher.activities.LauncherActivity") && FirebaseAuth.getInstance().getCurrentUser() != null) {



                            FirebaseDatabase myFirebaseDatabase_UserInfo;
                            DatabaseReference myDatabaseReference_UserInfo_reference;

                            myFirebaseDatabase_UserInfo = FirebaseDatabase.getInstance();
                            myDatabaseReference_UserInfo_reference = myFirebaseDatabase_UserInfo.getReference().child("notification").child(FirebaseAuth.getInstance().getUid());



                            myDatabaseReference_UserInfo_reference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    Log.d(TAG, "run: \n" + dataSnapshot.toString() + "\n" );

                                    for (DataSnapshot dt : dataSnapshot.getChildren()) {
                                        Log.d(TAG, "run: \n" + dt.toString() + "\n" );
                                        UserNotification not = dt.getValue(UserNotification.class);
                                        Log.d(TAG, "run: \n" + not.getMessageNotifactionSender() + "\n" );
                                        Notification notification = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_1_ID)
                                                .setSmallIcon(R.drawable.linkrip_logo)
                                                .setContentTitle("Message")
                                                .setContentText(not.getMessageNotifactionSender())
                                                .setPriority(NotificationCompat.PRIORITY_HIGH)
                                                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                                                .build();
                                        notificationManager.notify(1, notification);
                                        dt.getRef().removeValue();
                                    }

                                   // Log.d(TAG, "run: \n" + dataSnapshot.getRef().removeValue() + "\n" );
                                    //UserNotification not = dataSnapshot.getValue(UserNotification.class);

                                    /*
                                    if (not.getMessageNotifaction().equals("1")){
                                        String title = "Message Sent";
                                        String message = not.getMessageNotifactionSender();


                                        Notification notification = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_1_ID)
                                                .setSmallIcon(R.drawable.linkrip_logo)
                                                .setContentTitle(title)
                                                .setContentText(message)
                                                .setPriority(NotificationCompat.PRIORITY_HIGH)
                                                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                                                .build();
                                        notificationManager.notify(1, notification);
                                    }
                                    */
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    Log.d(TAG, "run: \n" + databaseError.toString() + "\n" );
                                }
                            });
                            try {
                                Thread.sleep(2000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
/*
                            for (int i = 0; i < 100; i++) {
                                myActivityManager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
                                myComponentName = myActivityManager.getRunningTasks(1).get(0).topActivity;
                                Log.d(TAG, "run: " + i + myComponentName.toString() + "\n" + myActivityManager.toString());
                                if(!myComponentName.getClassName().equals("com.sec.android.app.launcher.activities.LauncherActivity")) {
                                    break;
                                }

                                String title = "Test Notification";
                                String message = "Discraption";


                                Notification notification = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_1_ID)
                                        .setSmallIcon(R.drawable.linkrip_logo)
                                        .setContentTitle(title)
                                        .setContentText(message)
                                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                                        .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                                        .build();

                                notificationManager.notify(1, notification);

                                if (jobCancelled) {
                                    return;
                                }

                                try {

                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
*/
                            Log.d(TAG, "Job finished");
                            //jobFinished(params, false);
                        }
                        else {
                            myActivityManager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
                            myComponentName = myActivityManager.getRunningTasks(1).get(0).topActivity;
                            Log.d(TAG, "run: "+myComponentName.getClassName() + "\n" +myComponentName.getShortClassName() + "\n" + myActivityManager.isLowRamDevice());
                        }
                    }
            }
        }).start();

    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.d(TAG, "Job cancelled before completion");
        jobCancelled = true;
        return true;
    }
}
