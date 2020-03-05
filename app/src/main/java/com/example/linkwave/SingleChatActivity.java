package com.example.linkwave;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
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

public class SingleChatActivity extends AppCompatActivity {

    TextView chat;
    LinearLayout layout;
    RelativeLayout layout_2;
    ImageView sendButton;
    EditText messageArea;
    ScrollView scrollView;
    Firebase reference1, reference2,reference3;
    public static String userRef;
    public static String recieverRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_chat);
        closeKeyboard();
        layout = (LinearLayout) findViewById(R.id.layout1);
        layout_2 = (RelativeLayout)findViewById(R.id.layout2);
        sendButton = (ImageView)findViewById(R.id.sendButton);
        messageArea = (EditText)findViewById(R.id.messageArea);
        scrollView = (ScrollView)findViewById(R.id.scrollView);
        Intent intent = getIntent();
        userRef= intent.getExtras().getString("recieverUserId");
        recieverRef= intent.getExtras().getString("receiverUsername");

        scrollView.fullScroll(View.FOCUS_DOWN);

        Firebase.setAndroidContext(this);
        reference1 = new Firebase("https://androidlinkwave.firebaseio.com/messages/" + FirebaseAuth.getInstance().getUid() + "_" + userRef);
        reference2 = new Firebase("https://androidlinkwave.firebaseio.com/messages/" + userRef + "_" + FirebaseAuth.getInstance().getUid());
        reference3 = new Firebase("https://androidlinkwave.firebaseio.com/notification/"+userRef );



        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = messageArea.getText().toString();

                if(!messageText.equals("")){
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("message", messageText);
                    map.put("user", FirebaseAuth.getInstance().getUid());
                   //map.put("notification", "1");

                    Map<String, String> map2 = new HashMap<String, String>();
                    map2.put("callNotifaction", "0");
                    map2.put("messageNotifaction", "1");
                    map2.put("messageNotifactionSender", recieverRef );
                    //map2.put("message", messageText);
                    //map2.put("user", FirebaseAuth.getInstance().getUid());
                    //map2.put("notification", "0");
                    reference1.push().setValue(map);
                    reference2.push().setValue(map);
                    //reference3.push().setValue(map2);

                    //Map<String, String> mapNot = new HashMap<String, String>();
                    //mapNot.put("messageNotifaction", "1");
                    //mapNot.put("messageNotifactionSender", recieverRef );

                    //reference3.setValue(mapNot);
                    messageArea.setText("");
                }
                closeKeyboard();
            }
        });

        reference1.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map map = dataSnapshot.getValue(Map.class);
                String message = map.get("message").toString();
                String userName = map.get("user").toString();

                if(userName.equals(FirebaseAuth.getInstance().getUid())){
                    addMessageBox("You:-\n" + message, 1);
                }
                else{
                    addMessageBox(recieverRef + ":-\n" + message, 2);
                }
                Log.i("Chatreference1", message);
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
        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i("Chatreference1", dataSnapshot.toString());

                scrollView.fullScroll(View.FOCUS_DOWN);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
       /* Intent intent = getIntent();
        String userRef= intent.getExtras().getString("recieverUserId");
        chat = findViewById(R.id.chatText);
        chat.setText(userRef);*/


    }
    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
    public void addMessageBox(String message, int type){
        TextView textView = new TextView(SingleChatActivity.this);
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
