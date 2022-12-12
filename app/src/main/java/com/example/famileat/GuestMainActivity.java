package com.example.famileat;

import static com.example.famileat.StartActivity.SHARED_PREFS;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
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

import adapters.GuestAvAdapder;
import classes.Dinner;
import adapters.GuestMyAdapter;
import classes.User;
import adapters.ChatAdapter;

public class GuestMainActivity extends AppCompatActivity {
    private Button logout, editprofile, meals_btn;
    private TextView meals_txt;
    private FirebaseUser user;
    private DatabaseReference reference;
    private DatabaseReference referenceD;
    private String ID;

    private RecyclerView recyclerView;
    private GuestAvAdapder avAdapter;
    private GuestMyAdapter myAdapter;
    ArrayList<Dinner> av_dinnerList,my_dinnerList;



    private boolean backPressed = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_main);

        //Dinner List: ...........................................................................
        recyclerView = findViewById(R.id.dinnerList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        referenceD = FirebaseDatabase.getInstance().getReference("Dinners");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        av_dinnerList = new ArrayList<>();
        my_dinnerList = new ArrayList<>();
        myAdapter = new GuestMyAdapter(this, my_dinnerList, R.drawable.google);
        avAdapter = new GuestAvAdapder(this, av_dinnerList, R.drawable.google);
        recyclerView.setAdapter(avAdapter);

        referenceD.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                av_dinnerList.clear();
                my_dinnerList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Dinner dinner = dataSnapshot.getValue(Dinner.class);
                    if (Dinner.isAvailable(dinner) && !Dinner.isAccepted(dinner, ID))
                        av_dinnerList.add(dinner);
                    if(Dinner.isAccepted(dinner,ID))
                        my_dinnerList.add(dinner);

                }
                myAdapter.notifyDataSetChanged();
                avAdapter.notifyDataSetChanged();

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //.........................................................................................

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
                Toast.makeText(GuestMainActivity.this,"Logged out!",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(GuestMainActivity.this, StartActivity.class));
//                finish();
            }
        });
        //........................................................
        //edit profile............................................
        editprofile = findViewById(R.id.edit_profile);
        editprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(GuestMainActivity.this, EditProfileActivity.class));
            }
        });
        //........................................................
        //Meals button............................................
        meals_txt = findViewById(R.id.meals_txt);
        meals_btn = findViewById(R.id.meals_btn);
        meals_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(meals_btn.getText().equals("my meals")){
                    meals_btn.setText("available meals");
                    meals_txt.setText("My Meals:");
                    recyclerView.setAdapter(myAdapter);
                }
                else{
                    meals_btn.setText("my meals");
                    meals_txt.setText("Available Meals:");
                    recyclerView.setAdapter(avAdapter);
                }

            }
        });
        //........................................................
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        ID = user.getUid();

        final TextView fullname_text = (TextView) findViewById(R.id.name);

        reference.child(ID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User profile = snapshot.getValue(User.class);
                if (profile != null) {
                    String fullname = profile.getFullName();
                    String email = profile.getEmail();

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