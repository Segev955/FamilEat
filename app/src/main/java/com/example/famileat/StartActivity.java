package com.example.famileat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

public class StartActivity extends AppCompatActivity {
    private EditText email;
    private EditText password;
    private Button login;
    private Button register;

    private FirebaseAuth auth;

//    private GoogleSignInOptions gso;
    private GoogleSignInClient gsc;
    ImageView google_b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        login = findViewById(R.id.signin);

        auth = FirebaseAuth.getInstance();
        //Google Sign in: .........................................
        google_b = findViewById(R.id.googleb);
        GoogleSignInOptions  gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        gsc = GoogleSignIn.getClient(this,gso);
        // ........................................................

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_email= email.getText().toString();
                String txt_password= password.getText().toString();

                if (TextUtils.isEmpty(txt_email))
                    Toast.makeText(StartActivity.this,"Please enter email.",Toast.LENGTH_SHORT).show();
                else if (TextUtils.isEmpty(txt_password))
                    Toast.makeText(StartActivity.this,"Please enter password.",Toast.LENGTH_SHORT).show();
                else
                    registerUser(txt_email,txt_password);
            }
        });
        register = findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent n = new Intent(getApplicationContext(),RegisterActivity.class);
                startActivity(n);
            }
        });
// Google ...................................................................
        google_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
    }

    void signIn() {
        Intent signInIntent = gsc.getSignInIntent();
        startActivityForResult(signInIntent, 1234);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1234  ){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(),null);
                FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(getApplicationContext(), UserMainActivity.class);
                            startActivity(intent);
                        }
                        else {
                            Toast.makeText(StartActivity.this,task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
//                navigateToSecondActivity();
            } catch (ApiException e) {
                System.out.println("expt: " + e);
                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        }
    }

    void navigateToSecondActivity(){
        finish();
        Intent intent = new Intent(StartActivity.this,UserMainActivity.class);
        startActivity(intent);
    }

    private void registerUser(String email, String password) {
        auth.signInWithEmailAndPassword(email,password).addOnSuccessListener(StartActivity.this, new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Toast.makeText(StartActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(StartActivity.this, UserMainActivity.class));
                finish();
            }
        }).addOnFailureListener(StartActivity.this,new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(StartActivity.this, "Wrong email or password!", Toast.LENGTH_SHORT).show();
                System.out.println("Login problem: "+e.getMessage());
            }
        });
    }
}