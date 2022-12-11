package com.example.famileat;

import static com.example.famileat.StartActivity.SHARED_PREFS;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import classes.Dinner;
import classes.HostsAdapter;
import classes.Request;
import classes.RequestsAdapter;
import classes.User;

public class HostMainActivity extends AppCompatActivity {
    private Button logout, editprofile, new_meal, requests;
    private FirebaseUser user;
    private DatabaseReference reference;
    private String ID;

    private DatabaseReference referenceD;
    private RecyclerView recyclerView;
    private HostsAdapter hostAdapter;
    ArrayList<Dinner> dinnerList;


    private boolean backPressed = false;
    private AlertDialog.Builder dialog_builder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_main);


        //set user and ID
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        ID = user.getUid();

        //Dinner List: ...........................................................................
        recyclerView = findViewById(R.id.dinnerList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        referenceD = FirebaseDatabase.getInstance().getReference("Dinners");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        dinnerList = new ArrayList<>();

        //Set host adapter..............................................
        dialog_builder = new AlertDialog.Builder(this);
        hostAdapter = new HostsAdapter(this,dinnerList, R.drawable.google, dialog_builder);
        recyclerView.setAdapter(hostAdapter);

        referenceD.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dinnerList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Dinner dinner = dataSnapshot.getValue(Dinner.class);
                    if(dinner.getHostUid().equals(ID))
                        dinnerList.add(dinner);
                }
                hostAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //.........................................................................................
        //set requests number

        DatabaseReference referenceR = FirebaseDatabase.getInstance().getReference("Requests");
        referenceR.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int sum = 0;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Request request = dataSnapshot.getValue(Request.class);
                    assert request != null;
                    if (request.getHostUid().equals(ID)) {
                        sum++;
                        Toast.makeText(HostMainActivity.this,  "You have a new request!", Toast.LENGTH_SHORT).show();
                    }

                }
                if (sum == 0) {
                    requests.setText("requests");
                    requests.setBackgroundColor(0x988E8F);
                }
                else {
                    requests.setText("requests (" + sum + ")");
                    requests.setBackgroundColor(0xff99cc00);
                }
                sum = 0;
//sali.sharfman@msmail.ariel.ac.il
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
            //.........................................................................................
        //Requests button............................................
        requests = findViewById(R.id.btnRequests);
        requests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(requests.getText().equals("requests"))
                    Toast.makeText(HostMainActivity.this,"You have no requests!",Toast.LENGTH_SHORT).show();
                else
                    startActivity(new Intent(HostMainActivity.this, RequestsActivity.class));
            }
        });

        //........................................................




        //logout .................................................
        logout = findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("remember", "false");
                editor.apply();
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(HostMainActivity.this,"Logged out!",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(HostMainActivity.this, StartActivity.class));
                finish();
            }
        });
        //........................................................
        //new meal............................................
        new_meal = findViewById(R.id.new_meal);
        new_meal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HostMainActivity.this, SubmitDinnerActivity.class));
            }
        });
        //........................................................
        //edit profile............................................
        editprofile = findViewById(R.id.edit_profile);
        editprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HostMainActivity.this, EditProfileActivity.class));
            }
        });
        //........................................................


        final TextView fullname_text = (TextView) findViewById(R.id.name);

        reference.child(ID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User profile = snapshot.getValue(User.class);
                if (profile != null) {
                    String fullname = profile.getFullName();
                    fullname_text.setText(fullname);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    //Press twice "back" for exit .............................................................
    @Override
    public void onBackPressed() {
        if (backPressed) {
            super.onBackPressed();
            return;
        }
        Toast.makeText(this,"Press 'back' again to exit", Toast.LENGTH_SHORT).show();
        backPressed = true;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                backPressed = false;
            }
        }, 2000);
    }
    //..........................................................................................
}