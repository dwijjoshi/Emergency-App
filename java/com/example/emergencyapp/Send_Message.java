package com.example.emergencyapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Send_Message extends AppCompatActivity {

    Button record, emergency;
    int i = 0;
    int count = 0;
    SpeechRecognizer speechRecognizer;
    EditText editText;
    FusedLocationProviderClient fusedLocationProviderClient;
    LocationManager locationManager;
    double latitude, longitude;


    String contact1,contact2,contact3,contact4,contact5;


    SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "mypref";
    private static final String KEY1 = "phone1";
    private static final String KEY2 = "phone2";
    private static final String KEY3 = "phone3";
    private static final String KEY4 = "phone4";
    private static final String KEY5 = "phone5";




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send__message);


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(
                Send_Message.this
        );

        final String PREFS_NAME = "MyPrefsFile";

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);

        if (settings.getBoolean("my_first_time", true)) {
           //This means the app is loaded for the first time and user needs to enter contacts
            startActivity(new Intent(Send_Message.this,MainActivity.class));


            // record the fact that the app has been started at least once
            settings.edit().putBoolean("my_first_time", false).commit();
        }

        else
        {
            //if the app is started atleast one
            setContentView(R.layout.activity_send__message);
        }






        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        record = findViewById(R.id.btnRecord);
        editText = findViewById(R.id.ettext);
        emergency = findViewById(R.id.btn_emergency);



        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
         contact1 = sharedPreferences.getString(KEY1, null);
         contact2 = sharedPreferences.getString(KEY2, null);
         contact3 = sharedPreferences.getString(KEY3, null);
         contact4 = sharedPreferences.getString(KEY4, null);
         contact5 = sharedPreferences.getString(KEY5, null);


        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);

        final Intent speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);


        emergency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                i++;
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //If the button is clicked twice
                        if(i==2)
                        {
                            //Check Permissions
                            if (ActivityCompat.checkSelfPermission(Send_Message.this
                                    , Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                                    && ActivityCompat.checkSelfPermission(Send_Message.this
                                    , Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {


                                getLocation();
                                Toast.makeText(Send_Message.this, "Message sent", Toast.LENGTH_SHORT).show();


                            } else {
                                //If permission is denied
                                ActivityCompat.requestPermissions(Send_Message.this
                                        , new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.SEND_SMS}, 44);
                            }
                        }
                        i=0;

                    }
                },450);

            }
        });


        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Permission
                if(ActivityCompat.checkSelfPermission(Send_Message.this
                        , Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
                    if (count == 0) {
                        //If button is clicked once : mic on
                        record.setBackground(getDrawable(R.drawable.mic_on));
                        speechRecognizer.startListening(speechRecognizerIntent);
                        count = 1;
                    } else {
                        //If button is clicked again : mic off
                        record.setBackground(getDrawable(R.drawable.mic_off));
                        speechRecognizer.stopListening();
                        count = 0;

                    }
                }

                else
                {
                    //request permission
                    ActivityCompat.requestPermissions(Send_Message.this
                            , new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.SEND_SMS}, 44);
                }
            }
        });

        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {

            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float rmsdB) {

            }

            @Override
            public void onBufferReceived(byte[] buffer) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int error) {

            }

            @Override
            public void onResults(Bundle results) {
                ArrayList<String> data = results.getStringArrayList(speechRecognizer.RESULTS_RECOGNITION);
                editText.setText(data.get(0));
                final String word = editText.getText().toString();
                if (word.equals("emergency")) {
                    Toast.makeText(Send_Message.this, "Message sent", Toast.LENGTH_SHORT).show();
                    getLocation();

                } else {
                    Toast.makeText(Send_Message.this, "Please try again", Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onPartialResults(Bundle partialResults) {

            }

            @Override
            public void onEvent(int eventType, Bundle params) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit:
                Intent intent = new Intent(Send_Message.this,MainActivity.class);
                startActivity(intent);
                return true;
            case R.id.privacy_info:
                Intent intent1 = new Intent(Send_Message.this,Privacy_info.class);
                startActivity(intent1);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    public void getLocation() {


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions

            return ;
        }
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location = task.getResult();
                if (location != null) {

                    try {
                        Geocoder geocoder = new Geocoder(Send_Message.this, Locale.getDefault());
                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                        latitude = location.getLatitude();
                        longitude = location.getLongitude();

                        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
                        contact1 = sharedPreferences.getString(KEY1, null);
                        contact2 = sharedPreferences.getString(KEY2, null);
                        contact3 = sharedPreferences.getString(KEY3, null);
                        contact4 = sharedPreferences.getString(KEY4, null);
                        contact5 = sharedPreferences.getString(KEY5, null);




                            String[] phone_nos = {contact1, contact2, contact3, contact4, contact5};

                            for (int n = 0; n < 5; n++) {

                                if (!phone_nos[n].equals("")) {

                                    String smessage = "My location is : http://maps.google.com/?q=" + latitude + "," + longitude;
                                    String sphone = phone_nos[n];

                                    SmsManager smsManager = SmsManager.getDefault();
                                    smsManager.sendTextMessage(sphone, null, smessage, null, null);

                                }
                            }

                    }
                    catch(IOException e){
                            e.printStackTrace();
                    }


                }
            }

        });

        }


}