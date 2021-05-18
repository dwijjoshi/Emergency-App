package com.example.emergencyapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText contact1,contact2,contact3,contact4,contact5;
    Button submit;
    SharedPreferences sharedPreferences;
    private  static final String SHARED_PREF_NAME = "mypref";
    private  static final String KEY1 = "phone1";
    private  static final String KEY2 = "phone2";
    private  static final String KEY3 = "phone3";
    private  static final String KEY4 = "phone4";
    private  static final String KEY5 = "phone5";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        contact1 = findViewById(R.id.etcontact1);
        contact2 = findViewById(R.id.etcontact2);
        contact3 = findViewById(R.id.etcontact3);
        contact4 = findViewById(R.id.etcontact4);
        contact5 = findViewById(R.id.etcontact5);
        submit = findViewById(R.id.btnSubmit);
        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);



        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 String con1 = contact1.getText().toString().trim();
                 String con2 = contact2.getText().toString().trim();
                 String con3 = contact3.getText().toString().trim();
                 String con4 = contact4.getText().toString().trim();
                 String con5 = contact5.getText().toString().trim();


                //For permanently saving the numbers in the app
                 SharedPreferences.Editor editor = sharedPreferences.edit();
                 editor.putString(KEY1,con1);
                 editor.putString(KEY2,con2);
                 editor.putString(KEY3,con3);
                 editor.putString(KEY4,con4);
                 editor.putString(KEY5,con5);
                 editor.apply();
                 contact1.setText(con1);
                 contact2.setText(con2);
                 contact3.setText(con3);
                 contact4.setText(con5);
                 contact5.setText(con5);


                //The user is required to enter atleast one contact
                if(!con1.equals("") || !con2.equals("") || !con3.equals("") || !con4.equals("") || !con5.equals("") )
                {
                    Intent intent = new Intent(MainActivity.this,Send_Message.class);
                    startActivity(intent);
                }

                else
                {
                    Toast.makeText(MainActivity.this, "Please enter atleast one contact", Toast.LENGTH_SHORT).show();
                }

                
            }
        });

        //For loading the saved numbers and showing it on edit texts
        SharedPreferences getShared = getSharedPreferences(SHARED_PREF_NAME,MODE_PRIVATE);
        String value1 = getShared.getString(KEY1,"");
        String value2 = getShared.getString(KEY2,"");
        String value3 = getShared.getString(KEY3,"");
        String value4 = getShared.getString(KEY4,"");
        String value5 = getShared.getString(KEY5,"");
        contact1.setText(value1);
        contact2.setText(value2);
        contact3.setText(value3);
        contact4.setText(value4);
        contact5.setText(value5);



    }








}