package com.example.famileat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class UserMainActivity extends AppCompatActivity {
    private Button logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main);
        Toast.makeText(UserMainActivity.this,"Welcome!",Toast.LENGTH_SHORT).show();
        logout = findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(UserMainActivity.this,"Logged out!",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(UserMainActivity.this, StartActivity.class));
            }
        });
    }
}