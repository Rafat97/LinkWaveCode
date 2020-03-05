package com.example.linkwave;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class MessageChatFragment extends Fragment {
    View myView;
    Button ContactButtomList;
    Button GroupChat;
    Button VideoCalling;
    Context CurrentContext;
    final int MY_PERMISSIONS_REQUEST_CONTACTLIST = 103;

    public MessageChatFragment(Context context) {
        // Required empty public constructor
        this.CurrentContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        myView = inflater.inflate(R.layout.fragment_message_chat, container, false);

        ContactButtomList = myView.findViewById(R.id.ContactButtom);
        GroupChat =  myView.findViewById(R.id.GroupChatEnter);
        VideoCalling =  myView.findViewById(R.id.VideoCalling);

        ContactButtomList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContactButtomListPrermission();

            }
        });
        VideoCalling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentCallerActivity();
            }
        });

        GroupChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentGroupChatRoomCreatActivity();
            }
        });
        return myView;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

         if (requestCode == MY_PERMISSIONS_REQUEST_CONTACTLIST) {


            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent it = new Intent(this.CurrentContext,ContactListActivity.class);
                startActivity(it);

            }
            else{
                Intent mySuperIntent = new Intent(this.CurrentContext, ErrorActivity.class);
                mySuperIntent.putExtra("Error_Title", "Contact");
                mySuperIntent.putExtra("Error_Des", "Please give contact permission. ");
                startActivity(mySuperIntent);

            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    void intentCallerActivity(){
        Intent it = new Intent(this.CurrentContext,CallerActivity.class);
        startActivity(it);
    }
    void intentGroupChatRoomCreatActivity(){
        Intent it = new Intent(this.CurrentContext,GroupChatRoomCreatActivity.class);
        startActivity(it);
    }

    void ContactButtomListPrermission(){
        if (ActivityCompat.checkSelfPermission(CurrentContext, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {


            Intent it = new Intent(this.CurrentContext,ContactListActivity.class);
            startActivity(it);



        } else {

            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) && ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)) {
                //Toast.makeText(this, "shouldShowRequestPermissionRationale", Toast.LENGTH_SHORT).show();
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_CONTACTS}, MY_PERMISSIONS_REQUEST_CONTACTLIST);
                }

            } else {
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_CONTACTS}, MY_PERMISSIONS_REQUEST_CONTACTLIST);
                }
            }

        }
    }

}
