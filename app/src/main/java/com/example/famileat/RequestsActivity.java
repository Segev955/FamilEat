package com.example.famileat;

import static com.example.famileat.StartActivity.SHARED_PREFS;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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

import classes.Request;
import adapters.RequestsAdapter;
import classes.User;

public class RequestsActivity extends AppCompatActivity {
    private Button logout, editprofile, new_meal, requests;
    private FirebaseUser user;
    private DatabaseReference reference;
    private String ID;

    private DatabaseReference referenceD,referenceR;
    private RecyclerView recyclerView;
    private RequestsAdapter requestAdapter;
    ArrayList<Request> requestList;


    private boolean backPressed = false;
    private AlertDialog.Builder dialog_builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests);

        //set user and ID
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        ID = user.getUid();

        //Requests List: ...........................................................................
        recyclerView = findViewById(R.id.requestsList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        referenceR = FirebaseDatabase.getInstance().getReference("Requests");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        requestList = new ArrayList<>();

        //Set request adapter..............................................
          dialog_builder = new AlertDialog.Builder(this);
          requestAdapter = new RequestsAdapter(this,requestList, R.drawable.google);
          recyclerView.setAdapter(requestAdapter);

          referenceR.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                requestList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Request request = dataSnapshot.getValue(Request.class);
                    assert request != null;
                    if(request.getHostUid().equals(ID))
                        requestList.add(request);
                }
                requestAdapter.notifyDataSetChanged();

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
                Toast.makeText(RequestsActivity.this,"Logged out!",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(RequestsActivity.this, StartActivity.class));
                finish();
            }
        });
        //........................................................
        //new meal............................................
        new_meal = findViewById(R.id.new_meal);
        new_meal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RequestsActivity.this, SubmitDinnerActivity.class));
            }
        });
        //........................................................
        //edit profile............................................
        editprofile = findViewById(R.id.edit_profile);
        editprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RequestsActivity.this, EditProfileActivity.class));
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
}