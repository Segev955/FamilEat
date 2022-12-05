package com.example.famileat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

import classes.Dinner;
import classes.User;

public class SubmitDinner extends AppCompatActivity {

    private Button location,submit;
    private EditText text_title,text_details,text_date,text_time;
    private TextView location_text;
    private RadioGroup radio_kosher;
    private RadioButton kosher_r;
    private NumberPicker amont_t;
    int PLACE_PICKER_REQUEST = 1;

    //    private ProgressBar progressBar;
    private FirebaseAuth auth;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_dinner);
        text_title = findViewById(R.id.title);
        text_date = findViewById(R.id.date);
        text_time = findViewById(R.id.time);
        amont_t = findViewById(R.id.amount_p);
        amont_t.setMinValue(0);
        amont_t.setMaxValue(500);
        radio_kosher = findViewById(R.id.kosher);
        text_details = findViewById(R.id.details);
        submit = findViewById(R.id.submit);

        auth = FirebaseAuth.getInstance();

        //location picker
        location = findViewById(R.id.location);
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    startActivityForResult(builder.build(SubmitDinner.this),
                            PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });
        //Date Button:
        text_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int day = cal.get(Calendar.DAY_OF_MONTH);
                int month = cal.get(Calendar.MONTH);
                int year = cal.get(Calendar.YEAR);
                DatePickerDialog dateDialog = new DatePickerDialog(SubmitDinner.this,
                        android.R.style.Theme_Holo_Dialog_MinWidth,mDateSetListener,year,month,day);
                dateDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dateDialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String date = dayOfMonth + "/" + (month+1) + "/" + year;
                text_date.setText(date);
            }
        };

        //Submit button: ..................................................
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String title = text_title.getText().toString();
                String date = text_date.getText().toString();
                String time = text_time.getText().toString();
                String address = "maze pinat mapo";
                int amount = amont_t.getValue();
                int select_kosher = radio_kosher.getCheckedRadioButtonId();
                String details = text_details.getText().toString();


                //validation here!!!!!!!

                submitDinner(title,date,time,address,amount,select_kosher,details);
            }
        });

    }
    //...................................................................

    private void submitDinner(String title, String date, String time, String address, int amount, int kosher, String details) {
        Dinner dinner = new Dinner(title, date, time, address, amount, kosher,details);
        FirebaseDatabase.getInstance().getReference("Dinners")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .setValue(dinner).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(SubmitDinner.this, title + " submitted successfully!", Toast.LENGTH_SHORT).show();
                            FirebaseUser user2 = FirebaseAuth.getInstance().getCurrentUser();
                            user2.sendEmailVerification();
                            Intent n = new Intent(getApplicationContext(),StartActivity.class);
                            startActivity(n);
                        } else {
                            Toast.makeText(SubmitDinner.this, "Submission failed!", Toast.LENGTH_SHORT).show();

                        }
                    }
                });


    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                StringBuilder stringBuilder = new StringBuilder();
                String latitude = String.valueOf(place.getLatLng().latitude);
                String longitude = String.valueOf(place.getLatLng().longitude);
                stringBuilder.append("LATITUDE :");
                stringBuilder.append(latitude);
                stringBuilder.append("\n");
                stringBuilder.append("LONGITUDE :");
                stringBuilder.append(longitude);
                location_text.setText(stringBuilder.toString());

            }
        }
    }
}