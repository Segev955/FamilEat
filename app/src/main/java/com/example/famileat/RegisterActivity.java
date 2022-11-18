package com.example.famileat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private EditText password;
    private EditText username;
    private EditText email;
    private Button signup;
//    private DatabaseReference mRootRef;
    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        signup = findViewById(R.id.signup);

        auth = FirebaseAuth.getInstance();


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt_username= username.getText().toString();
                String txt_email= email.getText().toString();
                String txt_password= password.getText().toString();
                if(TextUtils.isEmpty(txt_username))
                    Toast.makeText(RegisterActivity.this,"Please enter username.",Toast.LENGTH_SHORT).show();
                else if (TextUtils.isEmpty(txt_email))
                    Toast.makeText(RegisterActivity.this,"Please enter email.",Toast.LENGTH_SHORT).show();
                else if (TextUtils.isEmpty(txt_password))
                    Toast.makeText(RegisterActivity.this,"Please enter password.",Toast.LENGTH_SHORT).show();
                else
                    registerUser(txt_username,txt_email,txt_password);

            }
        });
    }

    private void registerUser(String username, String email, String password) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    HashMap<String,Object> map= new HashMap<String,Object>();
                    map.put("username",username);
                    map.put("email",email);
//                    FirebaseDatabase.getInstance().getReference().child("Users").child(username).child("Personal Details").setValue(map);
                    Toast.makeText(RegisterActivity.this, username + " registered successfully!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterActivity.this, UserMainActivity.class));
                }
                else
                    Toast.makeText(RegisterActivity.this," Registration failed!",Toast.LENGTH_SHORT).show();
            }
        });

    }


}