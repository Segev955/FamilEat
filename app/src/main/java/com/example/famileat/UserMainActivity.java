package com.example.famileat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;

public class UserMainActivity extends AppCompatActivity {
    private Button logout;
    private GoogleSignInOptions gso;
    private GoogleSignInClient gsc;
    TextView name,email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main);
        Toast.makeText(UserMainActivity.this,"Welcome!",Toast.LENGTH_SHORT).show();
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this,gso);
        name = findViewById(R.id.name);
//        name = findViewById(R.id.username);

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account!=null) {
            String personEmail = account.getEmail();
            String personName = account.getDisplayName();
            name.setText(personName);
        }
        else {
            String name1 = "new guy";
            name.setText(name1);
        }

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