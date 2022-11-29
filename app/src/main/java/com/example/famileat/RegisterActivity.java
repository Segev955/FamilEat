package com.example.famileat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.text.DateFormat;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    private EditText text_password;
    private EditText text_fullName;
    private EditText text_email;
    private EditText text_date;
    private RadioGroup radio;
    private RadioButton radioButton;
    private Button signup;

//    private ProgressBar progressBar;
    private FirebaseAuth auth;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        text_fullName = findViewById(R.id.fullName);
        text_email = findViewById(R.id.email);
        text_password = findViewById(R.id.password);
        text_date = findViewById(R.id.date);
        signup = findViewById(R.id.signup);
        radio = findViewById(R.id.gender);


        auth = FirebaseAuth.getInstance();

        //Date Button:
        text_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int day = cal.get(Calendar.DAY_OF_MONTH);
                int month = cal.get(Calendar.MONTH);
                int year = cal.get(Calendar.YEAR);
                DatePickerDialog dateDialog = new DatePickerDialog(RegisterActivity.this,
                        android.R.style.Theme_Holo_Dialog_MinWidth,mDateSetListener,year,month,day);
                dateDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dateDialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String date = dayOfMonth + "/" + month + "/" + year;
                text_date.setText(date);
            }
        };

        //Sign up button: ..............................................
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fullName = text_fullName.getText().toString();
                String email = text_email.getText().toString();
                String password = text_password.getText().toString();
                String date = text_date.getText().toString();
                int gender = radio.getCheckedRadioButtonId();

                if (TextUtils.isEmpty(fullName)) {
                    Toast.makeText(RegisterActivity.this, "Please enter full name.", Toast.LENGTH_SHORT).show();
                    text_fullName.requestFocus();
                } else if (TextUtils.isEmpty(email)) {
                    Toast.makeText(RegisterActivity.this, "Please enter email.", Toast.LENGTH_SHORT).show();
                    text_email.requestFocus();
                } else if (TextUtils.isEmpty(password)) {
                    Toast.makeText(RegisterActivity.this, "Please enter password.", Toast.LENGTH_SHORT).show();
                    text_password.requestFocus();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(RegisterActivity.this, "Please enter valid email.", Toast.LENGTH_SHORT).show();
                    text_email.requestFocus();
                } else if (password.length() < 6) {
                    Toast.makeText(RegisterActivity.this, "Please enter at least 6 characters.", Toast.LENGTH_SHORT).show();
                    text_password.requestFocus();
                }
                else if (TextUtils.isEmpty(date)) {
                    Toast.makeText(RegisterActivity.this, "Please enter date (dd/mm/yyyy).", Toast.LENGTH_SHORT).show();
                    text_password.requestFocus();
                }
                else if (gender == -1) {
                    Toast.makeText(RegisterActivity.this, "Please choose gender.", Toast.LENGTH_SHORT).show();
                    radio.requestFocus();
                }
                else {
                    registerUser(fullName, email, password , date, gender);
                }

            }
        });
    }
    //...................................................................

    private void registerUser(String fullName, String email, String password, String date, int gender) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    User user = new User(fullName, email, date, gender);
                    FirebaseDatabase.getInstance().getReference("Users")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(RegisterActivity.this, fullName + " registered successfully!", Toast.LENGTH_SHORT).show();
                                        FirebaseUser user2 = FirebaseAuth.getInstance().getCurrentUser();
                                        user2.sendEmailVerification();
                                        Intent n = new Intent(getApplicationContext(),StartActivity.class);
                                        startActivity(n);
                                    } else {
                                        Toast.makeText(RegisterActivity.this, " Registration failed!", Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });

                }
                else {
                    Toast.makeText(RegisterActivity.this, " Registration failed!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}