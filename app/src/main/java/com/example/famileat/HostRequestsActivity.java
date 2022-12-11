package com.example.famileat;

import static com.example.famileat.StartActivity.SHARED_PREFS;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

public class HostRequestsActivity extends AppCompatActivity {
//    private Button logout, editprofile, new_meal, requests;
//    private FirebaseUser user;
//    private DatabaseReference reference;
//    private String ID;
//
//    private DatabaseReference referenceD,referenceR;
//    private RecyclerView recyclerView;
//    private RequestsAdapter requestAdapter;
//    ArrayList<Request> requestList;
//
//    TextView name,email;
//
//    private boolean backPressed = false;
//    private AlertDialog.Builder dialog_builder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_requests);


        //set user and ID
//        user = FirebaseAuth.getInstance().getCurrentUser();
//        reference = FirebaseDatabase.getInstance().getReference("Users");
//        ID = user.getUid();

//        //Requests List: ...........................................................................
//        recyclerView = findViewById(R.id.requestsList);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        referenceR = FirebaseDatabase.getInstance().getReference("Requests");
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        requestList = new ArrayList<>();

        //Set request adapter..............................................
      //  dialog_builder = new AlertDialog.Builder(this);
  //      requestAdapter = new RequestsAdapter(this,requestList, R.drawable.google, dialog_builder);
//        recyclerView.setAdapter(requestAdapter);
//
//        referenceR.addValueEventListener(new ValueEventListener() {
//            @SuppressLint("NotifyDataSetChanged")
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                requestList.clear();
//                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
//                    Request request = dataSnapshot.getValue(Request.class);
//                    assert request != null;
//                    if(request.getHostUid().equals(ID))
//                        requestList.add(request);
//                }
//                requestAdapter.notifyDataSetChanged();
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
        //.........................................................................................


    }
    //Press twice "back" for exit .............................................................
//    @Override
//    public void onBackPressed() {
//        if (backPressed) {
//            super.onBackPressed();
//            return;
//        }
//        Toast.makeText(this,"Press 'back' again to exit", Toast.LENGTH_SHORT).show();
//        backPressed = true;
//
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                backPressed = false;
//            }
//        }, 2000);
//    }
    //..........................................................................................
}