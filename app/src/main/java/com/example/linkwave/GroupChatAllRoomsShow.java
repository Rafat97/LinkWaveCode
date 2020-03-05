package com.example.linkwave;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GroupChatAllRoomsShow extends AppCompatActivity {

    List<String> NameList;
    public static List<String> NameListRef;
    ListView myListViewAllRooms;
    Firebase reference;
    ProgressDialog progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat_all_rooms_show);


        progress = new ProgressDialog(this);
        progress.setMessage("Please Wait ...");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(false);
        progress.setCancelable(false);
        progress.show();




        myListViewAllRooms = findViewById(R.id.AllRoomList);
        NameList = new ArrayList<>();
        NameListRef = new ArrayList<>();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("GROUP_CHAT_ANOS_ADD_USER/"+FirebaseAuth.getInstance().getUid());

        ref.addChildEventListener(new com.google.firebase.database.ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull com.google.firebase.database.DataSnapshot dataSnapshot, @Nullable String s) {
                Log.i("ALLROOM_LIST",dataSnapshot.getValue().toString());
                Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                String Name = map.get("name").toString();
                String ID = map.get("ID").toString();
                NameList.add(Name);
                NameListRef.add(ID);
                //Log.i("ALLROOM_LIST",Name);
                ContactListAdepter ct = new ContactListAdepter(NameList,GroupChatAllRoomsShow.this);
                myListViewAllRooms.setAdapter(ct);
            }

            @Override
            public void onChildChanged(@NonNull com.google.firebase.database.DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull com.google.firebase.database.DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull com.google.firebase.database.DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        progress.dismiss();
        myListViewAllRooms.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //Toast.makeText(GroupChatAllRoomsShow.this,position + "   " +NameListRef.get(position) ,Toast.LENGTH_SHORT).show();
                Intent mySuperIntent = new Intent(GroupChatAllRoomsShow.this, GroupChattingActivity.class);
                mySuperIntent.putExtra("GroupId",  NameListRef.get(position));
                startActivity(mySuperIntent);
                finish();

            }
        });

    }


    @Override
    protected void onStart() {
        super.onStart();
        Firebase reference_1;

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
