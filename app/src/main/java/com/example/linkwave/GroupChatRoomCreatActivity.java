package com.example.linkwave;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.telecom.TelecomManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

public class GroupChatRoomCreatActivity extends AppCompatActivity {

    Button CreatGroup;
    Button JoinGroup;
    Button AllRooms;
    AlertDialog.Builder builder;
    Firebase reference1, reference2,reference3;
    String GroupId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat_room_creat);
        CreatGroup = findViewById(R.id.CreatGroupButton);
        JoinGroup = findViewById(R.id.JoinGroupButton);
        AllRooms = findViewById(R.id.AllRoom);
        builder = new AlertDialog.Builder(this);

        CreatGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                View newView = getLayoutInflater().inflate(R.layout.group_creat_form,null);
                final EditText rd = newView.findViewById(R.id.GroupName);
                Button bt = newView.findViewById(R.id.GroupCreatSubmitButton);
                Button bt2 = newView.findViewById(R.id.groupCreatSubmitCancelButton);

                builder.setMessage(R.string.dialog_message).setTitle(R.string.dialog_title);


                builder.setCancelable(false);
                builder.setView(newView);
                final AlertDialog dil = builder.create();
                dil.show();


                bt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (!rd.getText().toString().isEmpty() && !rd.getText().toString().equals("")){

                            Firebase.setAndroidContext(GroupChatRoomCreatActivity.this);
                            GroupId = getRandomString(10)+"_"+System.currentTimeMillis();
                            reference1 = new Firebase("https://androidlinkwave.firebaseio.com/GROUP_CHAT_ANOS/"+GroupId+"/userCreat");
                            reference2 = new Firebase("https://androidlinkwave.firebaseio.com/GROUP_CHAT_ANOS/"+GroupId+"/groupName");

                            reference1.setValue(FirebaseAuth.getInstance().getUid());
                            reference2.setValue(rd.getText().toString());


                            reference1.keepSynced(true);

                            reference1.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    Log.i("IS_GROUP_CREAT",dataSnapshot.toString());
                                    Intent it = new Intent(GroupChatRoomCreatActivity.this,GroupChattingActivity.class);
                                    it.putExtra("GroupId",  GroupId);
                                    startActivity(it);
                                    dil.dismiss();
                                    reference1.removeEventListener(this);
                                    finish();


                                }

                                @Override
                                public void onCancelled(FirebaseError firebaseError) {
                                    Toast.makeText(GroupChatRoomCreatActivity.this," Some problem occur ",Toast.LENGTH_LONG).show();
                                    Log.i("IS_GROUP_CREAT",firebaseError.toString());
                                }
                            });


                        }
                        else{
                            Toast.makeText(GroupChatRoomCreatActivity.this,"Please Give Correct Group Name ",Toast.LENGTH_LONG).show();
                        }



                    }
                });
                bt2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dil.dismiss();
                    }
                });
            }
        });

        JoinGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View newView = getLayoutInflater().inflate(R.layout.group_creat_form,null);
                final EditText rd = newView.findViewById(R.id.GroupName);
                Button bt = newView.findViewById(R.id.GroupCreatSubmitButton);
                Button bt2 = newView.findViewById(R.id.groupCreatSubmitCancelButton);


                rd.setText("Give Room ID");



                builder.setMessage("Group ID").setTitle("Please Give Group ID");

                builder.setCancelable(false);
                builder.setView(newView);
                final AlertDialog dil = builder.create();
                dil.show();



                bt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (!rd.getText().toString().isEmpty() && !rd.getText().toString().equals("")){

                            Firebase.setAndroidContext(GroupChatRoomCreatActivity.this);
                            GroupId = rd.getText().toString();
                            reference1 = new Firebase("https://androidlinkwave.firebaseio.com/GROUP_CHAT_ANOS/"+GroupId);


                            reference1.keepSynced(true);

                            reference1.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    Log.i("IS_GROUP_CREAT",dataSnapshot.toString());
                                   if (dataSnapshot.hasChildren()){
                                       Intent it = new Intent(GroupChatRoomCreatActivity.this,GroupChattingActivity.class);
                                       it.putExtra("GroupId",  GroupId);
                                       startActivity(it);
                                       dil.dismiss();
                                       reference1.removeEventListener(this);
                                       finish();

                                   }
                                   else {
                                       Toast.makeText(GroupChatRoomCreatActivity.this," Group Id is wrong ",Toast.LENGTH_LONG).show();
                                   }


                                }

                                @Override
                                public void onCancelled(FirebaseError firebaseError) {
                                    Toast.makeText(GroupChatRoomCreatActivity.this," Check Group Id ",Toast.LENGTH_LONG).show();
                                    Log.i("IS_GROUP_CREAT",firebaseError.toString());
                                }
                            });


                        }
                        else{
                            Toast.makeText(GroupChatRoomCreatActivity.this,"Please Give Correct Group ID ",Toast.LENGTH_LONG).show();
                        }



                    }
                });
                bt2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dil.dismiss();
                    }
                });

            }
        });


        AllRooms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(GroupChatRoomCreatActivity.this,GroupChatAllRoomsShow.class);
                startActivity(it);
                finish();
            }
        });

    }

    private static final String ALLOWED_CHARACTERS ="0123456789qwertyuiopasdfghjklzxcvbnm";

    private static String getRandomString(final int sizeOfRandomString)
    {
        final Random random=new Random();
        final StringBuilder sb=new StringBuilder(sizeOfRandomString);
        for(int i=0;i<sizeOfRandomString;++i)
            sb.append(ALLOWED_CHARACTERS.charAt(random.nextInt(ALLOWED_CHARACTERS.length())));
        return sb.toString();
    }

}
