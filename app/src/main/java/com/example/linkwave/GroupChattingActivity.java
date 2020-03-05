package com.example.linkwave;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;
import java.util.Map;

public class GroupChattingActivity extends AppCompatActivity {

    TextView chat;
    LinearLayout layout;
    ImageButton MybuttonShareButton;
    RelativeLayout layout_2;
    ImageView sendButton;
    EditText messageArea;
    ScrollView scrollView;

    Firebase reference1, reference2,reference3 , reference4 ;
    public static String roomId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chatting);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.Main_toobar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
       // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        layout = (LinearLayout) findViewById(R.id.layout1);
        layout_2 = (RelativeLayout)findViewById(R.id.layout2);
        sendButton = (ImageView)findViewById(R.id.sendButton);
        messageArea = (EditText)findViewById(R.id.messageArea);
        scrollView = (ScrollView)findViewById(R.id.scrollView);
        //MybuttonShareButton = (ImageButton) findViewById(R.id.ButtonOfShare);


        Intent intent = getIntent();
        roomId = intent.getExtras().getString("GroupId");


        Firebase.setAndroidContext(this);
        reference1 = new Firebase("https://androidlinkwave.firebaseio.com/GROUP_CHAT_ANOS/"+roomId+"/message");
        reference2 = new Firebase("https://androidlinkwave.firebaseio.com/GROUP_CHAT_ANOS/"+roomId);
        reference1.keepSynced(true);
        reference4 = new Firebase("https://androidlinkwave.firebaseio.com/GROUP_CHAT_ANOS_ADD_USER/"+FirebaseAuth.getInstance().getUid()+"/"+roomId );



        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = messageArea.getText().toString();

                if(!messageText.equals("")){
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("message", messageText);
                    map.put("user", "Test");
                    map.put("message_user_id", FirebaseAuth.getInstance().getUid());

                    reference1.push().setValue(map);

                    messageArea.setText("");
                }
                closeKeyboard();
            }
        });


        reference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                String Group_name =  dataSnapshot.child("groupName").getValue().toString();
                String created_user =  dataSnapshot.child("userCreat").getValue().toString();
                String user_type =  "admin";
                if(!created_user.equals(FirebaseAuth.getInstance().getUid())){
                    user_type = "subscribed";
                }


                Map<String, String> map_user_group = new HashMap<String, String>();
                map_user_group.put("ID" , roomId);
                map_user_group.put("name" , Group_name);
                map_user_group.put("userType" , user_type);
                reference4.setValue(map_user_group);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        reference1.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map map = dataSnapshot.getValue(Map.class);
                String message = map.get("message").toString();
                String userName = map.get("user").toString();
                String user_message_id = map.get("message_user_id").toString();




                if(user_message_id.equals( FirebaseAuth.getInstance().getUid() )){
                    addMessageBox("You :-\n" + message, 1);
                }
                else{
                    addMessageBox("Unknown :-\n" + message, 2);
                }


            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.share_group_chat_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.share:
                shareMessage();
                break;

        }


        return super.onOptionsItemSelected(item);
    }


    void shareMessage(){
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = roomId;
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Group Id");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share via LinkWave"));
    }
    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
    public void addMessageBox(String message, int type){
        TextView textView = new TextView(GroupChattingActivity.this);
        textView.setText(message);

        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp2.weight = 1.0f;

        if(type == 1) {
            lp2.gravity = Gravity.RIGHT;
            textView.setBackgroundResource(R.drawable.bubble_in);
        }
        else{
            lp2.gravity = Gravity.LEFT;
            textView.setBackgroundResource(R.drawable.bubble_out);
        }
        textView.setLayoutParams(lp2);
        layout.addView(textView);
        scrollView.fullScroll(View.FOCUS_DOWN);
    }
}
