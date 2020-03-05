package com.example.linkwave;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class ErrorActivity extends AppCompatActivity {


    TextView myTextView_Error_Title;
    TextView myTextView_Error_Des;
    Button myButton_Error_CloseApp;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN , WindowManager.LayoutParams.FLAG_FULLSCREEN);




        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error);

        Intent intent = getIntent();
        String title = intent.getExtras().getString("Error_Title");
        String des = intent.getExtras().getString("Error_Des");

        myTextView_Error_Title = findViewById(R.id.Error_Title);
        myTextView_Error_Title.setText(title);

        myTextView_Error_Des = findViewById(R.id.Error_Descript);
        myTextView_Error_Des.setText(des);


        myButton_Error_CloseApp = findViewById(R.id.Error_Button_CloseApp);
        myButton_Error_CloseApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });








    }

    @Override
    public void onBackPressed() {

    }


    @Override
    protected void onStop() {
        finish();
        super.onStop();
    }
}
