package com.example.linkwave;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.linkwave.ExtraClass.User;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ContactListActivity extends AppCompatActivity {

    List<String> NameList;
    public static List<String> NameListRef;
    ListView myListViewContactlist;
    CountryCodePicker ccp;
    Map<String, String> FireBaseMobileNumberGet ;
    ProgressDialog progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        FireBaseMobileNumberGet = new HashMap<String, String>();
        NameList = new ArrayList<>();
        NameListRef = new ArrayList<>();
        getContactList();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);
        progress = new ProgressDialog(this);
        progress.setMessage("Please Wait ...");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(false);
        progress.setCancelable(false);
        progress.show();




    }

    @Override
    public void onBackPressed() {
//        Intent it = new Intent(ContactListActivity.this,FirstPageActivity.class);
//        startActivity(it);
//        finish();
        super.onBackPressed();
    }

    private void getContactList() {

        FirebaseDatabase myFirebaseDatabase_UserInfo;
        DatabaseReference myDatabaseReference_UserInfo_reference;


        myFirebaseDatabase_UserInfo = FirebaseDatabase.getInstance();
        myDatabaseReference_UserInfo_reference = myFirebaseDatabase_UserInfo.getReference().child("MOBILE_NUMBERS");

        myDatabaseReference_UserInfo_reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String getdata = dataSnapshot.getValue().toString();

                Log.d("MY_PROJECT_firebase:: ", getdata);


                String value = getdata;
                value = value.substring(1, value.length()-1);
                Log.d("MY_PROJECT_firebase:: ", value);
                String[] keyValuePairs = value.split(",");

                Log.d("MY_PROJECT_firebase:: ", keyValuePairs[0]);
                Map<String,String> map = new HashMap<>();

                for(String pair : keyValuePairs)
                {
                    String[] entry = pair.split("=");                   //split the pairs to get key and value
                    map.put(entry[0].trim(), entry[1].trim());          //add them to the hashmap and trim whitespaces
                }
                FireBaseMobileNumberGet = map;

                for(Map.Entry m:FireBaseMobileNumberGet.entrySet()){

                    Log.d("MY_PROJECT_firebase:: ", "KEY : "+m.getKey() + "value : "+ m.getValue());

                }

                ContentResolver cr = getContentResolver();
                Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                        null, null, null, null);

                if ((cur != null ? cur.getCount() : 0) > 0) {
                    while (cur != null && cur.moveToNext()) {
                        String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                        String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                        if (cur.getInt(cur.getColumnIndex(
                                ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                            Cursor pCur = cr.query(
                                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                    null,
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                                    new String[]{id}, null);
                            while (pCur.moveToNext()) {
                                String phoneNo = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));


                                    if(phoneNo.startsWith("+") || phoneNo.startsWith("0")){
                                        if (!phoneNo.substring(1,3).equals("88")){
                                            phoneNo = "+88"+phoneNo;
                                        }
                                    }
                                    else{
                                        phoneNo = "+88"+phoneNo;
                                    }
                                String check = FireBaseMobileNumberGet.get(phoneNo);
                                if(check != null){
                                    if (!NameListRef.contains(check)){
                                        NameList.add(name+ " " );
                                        NameListRef.add(FireBaseMobileNumberGet.get(phoneNo));
                                    }

                                }


                                Log.i("MY_PROJECT", "Name: " + name );
                                Log.i("MY_PROJECT", "Phone Number: " + phoneNo);

                                //Log.i("MY_PROJECT", "Phone Number: " + getBaseContext().getResources().getConfiguration().locale.());



                            }
                            pCur.close();
                        }
                    }
                }
                if(cur!=null){
                    cur.close();
                }

                myListViewContactlist = findViewById(R.id.ContactlistListView);
                myListViewContactlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        Intent mySuperIntent = new Intent(ContactListActivity.this, SingleChatActivity.class);
                        mySuperIntent.putExtra("recieverUserId",  NameListRef.get(position));
                        mySuperIntent.putExtra("receiverUsername", NameList.get(position));
                        startActivity(mySuperIntent);

                    }
                });
                ContactListAdepter ct = new ContactListAdepter(NameList,ContactListActivity.this);
                myListViewContactlist.setAdapter(ct);


                progress.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
