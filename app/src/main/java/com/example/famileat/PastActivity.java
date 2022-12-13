package com.example.famileat;

import static com.example.famileat.StartActivity.SHARED_PREFS;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
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
import java.util.Collections;
import java.util.Comparator;

import adapters.HostsAdapter;
import adapters.PastGuestAdapter;
import adapters.PastHostAdapter;
import classes.Dinner;
import classes.Request;
import classes.User;

public class PastActivity extends AppCompatActivity {
    private Button logout, editprofile, requests, mymealsbtn;
    private FirebaseUser user;
    private DatabaseReference reference;
    private String ID, Type;
    private DatabaseReference referenceD;
    private RecyclerView recyclerView;
    private PastHostAdapter hostAdapter;
    private PastGuestAdapter guestAdapter;

    ArrayList<Dinner> dinnerList;

    private static final String ONESIGNAL_APP_ID = "d165de36-ef1b-46ff-b69b-678b6637236e";

    TextView name,email;

    private boolean backPressed = false;
    private AlertDialog.Builder dialog_builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past);

        //set user and ID
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        ID = user.getUid();

        //set Type
        Bundle bundle = getIntent().getExtras();
        this.Type=(String) bundle.get("Type");

        //Dinner List: ...........................................................................
        recyclerView = findViewById(R.id.dinnerList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        referenceD = FirebaseDatabase.getInstance().getReference("Dinners");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        dinnerList = new ArrayList<>();

        //Set adapters..............................................
        dialog_builder = new AlertDialog.Builder(this);
        if (Type.equals("Host")) {
            hostAdapter = new PastHostAdapter(this, dinnerList, R.drawable.google, dialog_builder);
            recyclerView.setAdapter(hostAdapter);
        }
        else {
            guestAdapter = new PastGuestAdapter(this, dinnerList, R.drawable.google, dialog_builder);
            recyclerView.setAdapter(guestAdapter);
        }


        referenceD.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dinnerList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Dinner dinner = dataSnapshot.getValue(Dinner.class);
                    if(!Dinner.isRelevant(dinner,"") && (dinner.getHostUid().equals(ID)||Dinner.isAccepted(dinner,ID)))
                        dinnerList.add(dinner);
                }
                dinnerList=sortDinnersByDate(dinnerList);
                if (Type.equals("Host"))
                    hostAdapter.notifyDataSetChanged();
                else
                    guestAdapter.notifyDataSetChanged();

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
                        //  Toast.makeText(.this,  "You have a new request!", Toast.LENGTH_SHORT).show();
                    }

                }
                if (sum == 0) {
                    requests.setText("requests");
                    requests.setVisibility(View.GONE);
                    requests.setBackgroundColor(0x988E8F);
                }
                else {
                    requests.setVisibility(View.VISIBLE);
                    requests.setText("requests (" + sum + ")");
                    requests.setBackgroundColor(0xff99cc00);
                }
                sum = 0;
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
                    Toast.makeText(PastActivity.this,"You have no requests!",Toast.LENGTH_SHORT).show();
                else
                    startActivity(new Intent(PastActivity.this, RequestsActivity.class));
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
                Toast.makeText(PastActivity.this,"Logged out!",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(PastActivity.this, StartActivity.class));
                finish();
            }
        });
        //........................................................

        //edit profile............................................
        editprofile = findViewById(R.id.edit_profile);
        editprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PastActivity.this, EditProfileActivity.class));
            }
        });
        //........................................................
        //Set my meals button
        mymealsbtn = findViewById(R.id.my_meals_btn);
        mymealsbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Type.equals("Host"))
                    startActivity(new Intent(PastActivity.this, HostMainActivity.class));
                else
                    startActivity(new Intent(PastActivity.this, GuestMainActivity.class));
            }
        });
        //Requests button............................................
        requests = findViewById(R.id.btnRequests);
        requests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(requests.getText().equals("requests"))
                    Toast.makeText(PastActivity.this,"You have no requests!",Toast.LENGTH_SHORT).show();
                else
                    startActivity(new Intent(PastActivity.this, RequestsActivity.class));
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
                    String email = profile.getEmail();

                    fullname_text.setText(fullname);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public ArrayList<Dinner> sortDinnersByDate(ArrayList<Dinner> list)
    {
        Collections.sort(list, new Comparator<Dinner>() {
            @Override
            public int compare(Dinner dinner, Dinner t1) {
                return -dinner.compareTo(t1);
            }
        });
        return list;
    }


}