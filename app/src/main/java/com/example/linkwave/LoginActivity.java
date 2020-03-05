package com.example.linkwave;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.hbb20.CountryCodePicker;

import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    CountryCodePicker ccp;
    EditText editTextCarrierNumber;
    EditText editText;
    Button   submit;
    Button myButton_Login_submit;
    boolean aBoolean_Login_PghoneNumber = false;


    private FirebaseAuth fbAuth;

    String TAG="FIREBASE_AUTH";

    String VerifyCode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        AlradyHaveUser();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);




        ccp = (CountryCodePicker) findViewById(R.id.ccp);
        editTextCarrierNumber = (EditText) findViewById(R.id.editText_carrierNumber);
        myButton_Login_submit = (Button) findViewById(R.id.Login_Submit);
        fbAuth = FirebaseAuth.getInstance();


        //fbAuth.setLanguageCode("en");
        //editText = (EditText)findViewById(R.id.editText);
        //submit = (Button)findViewById(R.id.button);


        ccp.registerCarrierNumberEditText(editTextCarrierNumber);



        ccp.setPhoneNumberValidityChangeListener(new CountryCodePicker.PhoneNumberValidityChangeListener() {
            @Override
            public void onValidityChanged(boolean isValidNumber) {

                aBoolean_Login_PghoneNumber = isValidNumber;
            }
        });

        myButton_Login_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(aBoolean_Login_PghoneNumber){
                    //PhoneVarification(ccp.getFormattedFullNumber(),mCallbacks);

                    PhoneVarification( ccp.getFormattedFullNumber() );


                }
                else{
                    Toast.makeText(LoginActivity.this,"Please Check Your Phone Number",Toast.LENGTH_LONG ).show();
                }
            }
        });


       /* submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = editText.getText().toString();
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(VerifyCode, code);
                signInWithPhoneAuthCredential(credential);
            }
        });
*/

    }




    private void PhoneVarification(String phoneNumber){


      /*  FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");

        myRef.setValue(phoneNumber);

        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

*/
        Toast.makeText(LoginActivity.this,phoneNumber,Toast.LENGTH_LONG).show();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,
                60,
                TimeUnit.SECONDS,
                this,
                callback);


    }

    public PhoneAuthProvider.OnVerificationStateChangedCallbacks callback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);


            Intent myInternt_Error = new Intent(LoginActivity.this,VerifiyCodeActivity.class);
            myInternt_Error.putExtra("User_Verify_Code",s);
            startActivity(myInternt_Error);

            Log.d(TAG, "SMS onCodeSent() called "+ s);
            VerifyCode = s;
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            signInWithPhoneAuthCredential(phoneAuthCredential);
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(LoginActivity.this, "Sorry Verification is Failed ", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "SMS onVerificationFailed() called "+e);

        }
    };


    @Override
    protected void onStart() {
        super.onStart();
       // Intent myInternt_Error = new Intent(LoginActivity.this,ProfileActivity.class);
       // startActivity(myInternt_Error);


      /*  if(FirebaseAuth.getInstance().getCurrentUser() != null){


             FirebaseDatabase myFirebaseDatabase_UserInfo;
             DatabaseReference myDatabaseReference_UserInfo_reference;


            myFirebaseDatabase_UserInfo = FirebaseDatabase.getInstance();
            myDatabaseReference_UserInfo_reference = myFirebaseDatabase_UserInfo.getReference().child("USERS");
            myDatabaseReference_UserInfo_reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    //String value = dataSnapshot.getValue(String.class);
                    //Log.d(TAG, "Value is: " + value);


                    if(dataSnapshot.hasChild(FirebaseAuth.getInstance().getUid())){
                        Intent myInternt = new Intent(LoginActivity.this, FirstPageActivity.class);
                        startActivity(myInternt);

                    }



                    for (DataSnapshot data : dataSnapshot.getChildren()) {


                        if (data.getKey().equals(FirebaseAuth.getInstance().getUid())) {

                            Intent myInternt = new Intent(LoginActivity.this, FirstPageActivity.class);
                            startActivity(myInternt);
                            finish();
                            return;

                        }
                    }

                    // Intent myInternt_Error = new Intent(MainActivity.this,ProfileActivity.class);
                    //  startActivity(myInternt_Error);
                    //  finish();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    finish();
                }
            });



            Log.d(TAG, "FirebaseAuth :  "+FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber());
            Intent myInternt_Error = new Intent(LoginActivity.this,ProfileActivity.class);
            startActivity(myInternt_Error);
            return;
        }
        */
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        fbAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = task.getResult().getUser();

                            AlradyHaveUserNext();

                            //Intent myInternt_Error = new Intent(VerifiyCodeActivity.this,ProfileActivity.class);
                            //myInternt_Error.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            //startActivity(myInternt_Error);


                            // ...
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(LoginActivity.this, "Please Check Your Verification Code ", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }



    void  AlradyHaveUserNext(){
        if(FirebaseAuth.getInstance().getCurrentUser() != null){


            FirebaseDatabase myFirebaseDatabase_UserInfo;
            DatabaseReference myDatabaseReference_UserInfo_reference;


            myFirebaseDatabase_UserInfo = FirebaseDatabase.getInstance();
            myDatabaseReference_UserInfo_reference = myFirebaseDatabase_UserInfo.getReference().child("USERS");
            myDatabaseReference_UserInfo_reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    //String value = dataSnapshot.getValue(String.class);
                    //Log.d(TAG, "Value is: " + value);


                    if(dataSnapshot.hasChild(FirebaseAuth.getInstance().getUid())){
                        Intent myInternt = new Intent(LoginActivity.this, FirstPageActivity.class);
                        myInternt.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(myInternt);
                        finish();

                    }else{

                        Intent myInternt_Error = new Intent(LoginActivity.this,ProfileActivity.class);
                        myInternt_Error.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(myInternt_Error);
                        finish();
                    }



                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    finish();
                }
            });
        }
        else{
            Intent myInternt_Error = new Intent(LoginActivity.this,MainActivity.class);
            myInternt_Error.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
            startActivity(myInternt_Error);
            finish();
        }
    }



    void  AlradyHaveUser(){
        if(FirebaseAuth.getInstance().getCurrentUser() != null){


            FirebaseDatabase myFirebaseDatabase_UserInfo;
            DatabaseReference myDatabaseReference_UserInfo_reference;


            myFirebaseDatabase_UserInfo = FirebaseDatabase.getInstance();
            myDatabaseReference_UserInfo_reference = myFirebaseDatabase_UserInfo.getReference().child("USERS");
            myDatabaseReference_UserInfo_reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    //String value = dataSnapshot.getValue(String.class);
                    //Log.d(TAG, "Value is: " + value);


                    if(dataSnapshot.hasChild(FirebaseAuth.getInstance().getUid())){
                        Intent myInternt = new Intent(LoginActivity.this, FirstPageActivity.class);
                        startActivity(myInternt);
                        finish();

                    }else{
                        Log.d(TAG, "FirebaseAuth :  "+FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber());
                        Intent myInternt_Error = new Intent(LoginActivity.this,ProfileActivity.class);
                        startActivity(myInternt_Error);
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
    }





}






