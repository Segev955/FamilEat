package com.example.famileat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

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

public class HostRequestsActivity extends AppCompatActivity {

    private FirebaseUser user;
    private DatabaseReference reference;
    private String ID;

    private DatabaseReference referenceD;
    private RecyclerView recyclerView;
    private HostsAdapter hostAdapter;
    ArrayList<Dinner> dinnerList;
    private AlertDialog.Builder dialog_builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_requests);
//        //set user and ID
//        user = FirebaseAuth.getInstance().getCurrentUser();
//        reference = FirebaseDatabase.getInstance().getReference("Users");
//        ID = user.getUid();
//
//        //Dinner List: ...........................................................................
//        recyclerView = findViewById(R.id.requestsList);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        referenceD = FirebaseDatabase.getInstance().getReference("Dinners");
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        dinnerList = new ArrayList<>();
//
//        //Set host adapter..............................................
//        dialog_builder = new AlertDialog.Builder(this);
//        hostAdapter = new HostsAdapter(this,dinnerList, R.drawable.google, dialog_builder);
//        recyclerView.setAdapter(hostAdapter);
//
//        referenceD.addValueEventListener(new ValueEventListener() {
//            @SuppressLint("NotifyDataSetChanged")
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                dinnerList.clear();
//                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
//                    Dinner dinner = dataSnapshot.getValue(Dinner.class);
//                    if(dinner.getHostUid().equals(ID))
//                        dinnerList.add(dinner);
//                }
//                hostAdapter.notifyDataSetChanged();
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//        //.........................................................................................
//
//
    }
}