package com.example.linkwave;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.linkwave.ExtraClass.User;
import com.example.linkwave.ExtraClass.UserNotification;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.pushbots.push.Pushbots;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.PicassoProvider;

public class ProfileActivity extends AppCompatActivity {



    private FirebaseDatabase myFirebaseDatabase_UserInfo;
    private DatabaseReference myDatabaseReference_UserInfo_reference;
    private StorageReference myStorageReference_uplodeImage;
    private Button myButton_submit;
    private Button myButton_ChooseFile;
    private ImageView myImageView_ShowImage;
    private EditText myEditText_FirstName;
    private EditText myEditText_LastName;

    String TAG ="FIREBASE_DATABASE";

    public static final int PICK_IMAGE_REQUEST = 1;
    private Uri ImageUplodedUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        myButton_ChooseFile = (Button) findViewById(R.id.Profile_Info_FileChoose);
        myButton_submit = (Button) findViewById(R.id.Profile_Info_SubmitButton);
        myImageView_ShowImage = (ImageView) findViewById(R.id.Profile_Info_ShowImageView);
        myEditText_FirstName =  (EditText) findViewById(R.id.Profile_Info_firstName);
        myEditText_LastName = (EditText) findViewById(R.id.Profile_Info_lastName);

        myButton_ChooseFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageSelect();
            }
        });
        myButton_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isFormValid()){
                    myButton_submit.setEnabled(false);
                    myStorageReference_uplodeImage = FirebaseStorage.getInstance().getReference();
                    UplodeFile();
                }
            }
        });



        if(FirebaseAuth.getInstance().getCurrentUser() != null){

            alradyHaveUser();



        }
        else{
            FirebaseAuth.getInstance().signOut();
            Intent myInternt_Error = new Intent(ProfileActivity.this,LoginActivity.class);
            startActivity(myInternt_Error);
            finish();
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);



        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            ImageUplodedUrl = data.getData();
            Picasso.get().load(data.getData()).into(myImageView_ShowImage);
            //myImageView_ShowImage.setImageURI(data.getData());
        }

    }



    public void alradyHaveUser(){
        myFirebaseDatabase_UserInfo = FirebaseDatabase.getInstance();
        myDatabaseReference_UserInfo_reference = myFirebaseDatabase_UserInfo.getReference().child("USERS");
        myDatabaseReference_UserInfo_reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //String value = dataSnapshot.getValue(String.class);
                //Log.d(TAG, "Value is: " + value);

                for(DataSnapshot data: dataSnapshot.getChildren()){
                    Log.i(TAG,data.toString());


                    if (data.getKey().equals(FirebaseAuth.getInstance().getUid())) {
                        Log.i(TAG,"Find User In data base");
                        Intent myInternt= new Intent(ProfileActivity.this,FirstPageActivity.class);
                        startActivity(myInternt);
                        finish();
                        break;

                    }

                }
                myDatabaseReference_UserInfo_reference.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.i(TAG,"DatabaseError Got");
            }
        });
    }


    public void ImageSelect(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);

    }

    public String getExtention(Uri uri){
        ContentResolver cR = getContentResolver();
        MimeTypeMap mp = MimeTypeMap.getSingleton();
        return  mp.getExtensionFromMimeType(cR.getType(uri));

    }
    public void UplodeFile(){


            if(ImageUplodedUrl != null){
                StorageReference storageReference = myStorageReference_uplodeImage.child("profile_pic/"+FirebaseAuth.getInstance().getUid()+"<idANDtime>"+System.currentTimeMillis()+"."+getExtention(ImageUplodedUrl));
                storageReference.putFile(ImageUplodedUrl)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                Toast.makeText(ProfileActivity.this,"Thank you for giving information",Toast.LENGTH_SHORT).show();


                                Task<Uri> task = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                                task.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String photoLink = uri.toString();


                                        User myUser = new User(FirebaseAuth.getInstance().getUid(),myEditText_FirstName.getText().toString(),myEditText_LastName.getText().toString(),FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber(), photoLink);
                                        myFirebaseDatabase_UserInfo = FirebaseDatabase.getInstance();
                                        myDatabaseReference_UserInfo_reference = myFirebaseDatabase_UserInfo.getReference().child("USERS").child(FirebaseAuth.getInstance().getUid());
                                        myDatabaseReference_UserInfo_reference.setValue(myUser);


                                        Pushbots.sharedInstance().setName(myUser.getFirstname()+" "+myUser.getLastname());
                                        Pushbots.sharedInstance().setFirstName(myUser.getFirstname());
                                        Pushbots.sharedInstance().setLastName(myUser.getLastname());
                                       // Pushbots.sharedInstance().setEmail("john@smith.com");//Must be valid email
                                        //Pushbots.sharedInstance().setGender("M"); // M=> male /F=> female //O=>other
                                        Pushbots.sharedInstance().setPhone(myUser.getMobilenumber());
                                        myDatabaseReference_UserInfo_reference = myFirebaseDatabase_UserInfo.getReference().child("MOBILE_NUMBERS").child(myUser.getMobilenumber());
                                        myDatabaseReference_UserInfo_reference.setValue(FirebaseAuth.getInstance().getUid());


                                        Intent myInternt= new Intent(ProfileActivity.this,FirstPageActivity.class);
                                        startActivity(myInternt);
                                        finish();

                                    }
                                });


                                Log.i(TAG,taskSnapshot.getUploadSessionUri().toString());
                                Log.i(TAG,taskSnapshot.getMetadata().getReference().getDownloadUrl().toString());

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(ProfileActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                Log.i(TAG,taskSnapshot.toString());
                            }
                        });
            }

            else {

                String photoLink = "";

                User myUser = new User(FirebaseAuth.getInstance().getUid(),myEditText_FirstName.getText().toString(),myEditText_LastName.getText().toString(),FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber(), photoLink);

                myFirebaseDatabase_UserInfo = FirebaseDatabase.getInstance();
                myDatabaseReference_UserInfo_reference = myFirebaseDatabase_UserInfo.getReference().child("USERS").child(FirebaseAuth.getInstance().getUid());
                myDatabaseReference_UserInfo_reference.setValue(myUser);

                Pushbots.sharedInstance().setName(myUser.getFirstname()+" "+myUser.getLastname());
                Pushbots.sharedInstance().setFirstName(myUser.getFirstname());
                Pushbots.sharedInstance().setLastName(myUser.getLastname());
                // Pushbots.sharedInstance().setEmail("john@smith.com");//Must be valid email
                //Pushbots.sharedInstance().setGender("M"); // M=> male /F=> female //O=>other
                Pushbots.sharedInstance().setPhone(myUser.getMobilenumber());
                myDatabaseReference_UserInfo_reference = myFirebaseDatabase_UserInfo.getReference().child("MOBILE_NUMBERS").child(myUser.getMobilenumber());
                myDatabaseReference_UserInfo_reference.setValue(FirebaseAuth.getInstance().getUid());

                Intent myInternt= new Intent(ProfileActivity.this,FirstPageActivity.class);
                startActivity(myInternt);
                finish();


            }





    }

    private boolean isValidName(String name){
        String regex = "^[\\p{L} .'-]+$";
        return name.matches(regex);
    }

    private boolean isFormValid(){
        if(!isValidName(myEditText_FirstName.getText().toString())){
            Toast.makeText(ProfileActivity.this,"Please give correct First Name ",Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (!isValidName(myEditText_LastName.getText().toString())) {
            Toast.makeText(ProfileActivity.this, "Please give correct Last Name ", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}
