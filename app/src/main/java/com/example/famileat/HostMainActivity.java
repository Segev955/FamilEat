package com.example.famileat;

import static com.example.famileat.StartActivity.SHARED_PREFS;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;

import classes.Dinner;
import adapters.HostsAdapter;
import classes.Request;
import classes.User;

public class HostMainActivity extends AppCompatActivity {
    private Button logout, editprofile, new_meal, requests,search, past;
    private FirebaseUser user;
    private DatabaseReference reference;
    private String ID;
    private EditText text_date;
    private TextView text_hello;
    private RadioGroup radio_kosher;
    private RadioButton kosher_r, meat_r, dairy_r, notkosher_r, all_r;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private DatabaseReference referenceD;
    private RecyclerView recyclerView;
    private HostsAdapter hostAdapter;
    ArrayList<Dinner> dinnerList;

    private static final String ONESIGNAL_APP_ID = "d165de36-ef1b-46ff-b69b-678b6637236e";

    TextView name,email;

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




        //Set kosher radio buttons
        meat_r = findViewById(R.id.meat);
        dairy_r = findViewById(R.id.dairy);
        notkosher_r = findViewById(R.id.noKosher);
        all_r = findViewById(R.id.all);
        all_r.setChecked(true);
        radio_kosher = findViewById(R.id.kosher);

        //set Date Button:
        text_date = findViewById(R.id.date);
        Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int month = cal.get(Calendar.MONTH)+1;
        int year = cal.get(Calendar.YEAR);
        final String[] date = {day + "/" + month + "/" + year};
        text_date.setText(date[0]);
        text_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dateDialog = new DatePickerDialog(HostMainActivity.this,
                        android.R.style.Theme_Holo_Dialog_MinWidth, mDateSetListener, year, month-1, day);
                dateDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dateDialog.show();
            }
        });
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String date = dayOfMonth + "/" + (month + 1) + "/" + year;
                text_date.setText(date);
            }
        };

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
                int select_kosher = radio_kosher.getCheckedRadioButtonId();
                String kosher = "All";
                if (select_kosher != -1) {
                    kosher_r = (RadioButton) findViewById(select_kosher);
                    kosher = kosher_r.getText().toString();
                }
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Dinner dinner = dataSnapshot.getValue(Dinner.class);
                    if(Dinner.isRelevant(dinner,text_date.getText().toString()) && dinner.getHostUid().equals(ID) && (kosher.equals("All")||kosher.equals(dinner.getKosher())))
                        dinnerList.add(dinner);
                }
                dinnerList=sortDinnersByDate(dinnerList);
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
                      //  Toast.makeText(HostMainActivity.this,  "You have a new request!", Toast.LENGTH_SHORT).show();
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


        //Set past button
        past = findViewById(R.id.past_btn);
        past.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pastIntent = new Intent(HostMainActivity.this, PastActivity.class);
                pastIntent.putExtra("Type","Host");
                startActivity(pastIntent);
            }
        });

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

//.............................................................................................................................
        //Set search button
        search = findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg=Dinner.check_date_time(text_date.getText().toString(),"23:59");
                if(!msg.equals("accept")){
                    text_date.setText(date[0]);
                    Toast.makeText(HostMainActivity.this,msg,Toast.LENGTH_SHORT).show();
                }
                else {

                    referenceD.addListenerForSingleValueEvent(new ValueEventListener() {
                        @SuppressLint("NotifyDataSetChanged")
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            dinnerList.clear();
                            int select_kosher = radio_kosher.getCheckedRadioButtonId();
                            String kosher = "All";
                            if (select_kosher != -1) {
                                kosher_r = (RadioButton) findViewById(select_kosher);
                                kosher = kosher_r.getText().toString();
                            }

                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                Dinner dinner = dataSnapshot.getValue(Dinner.class);
                                if(Dinner.isRelevant(dinner,text_date.getText().toString()) && dinner.getHostUid().equals(ID) && (kosher.equals("All")||kosher.equals(dinner.getKosher())))
                                    dinnerList.add(dinner);

                            }
                            date[0]=text_date.getText().toString();
                            dinnerList = sortDinnersByDate(dinnerList);
                            hostAdapter.notifyDataSetChanged();

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }



            }
        });


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
        //Set name and rating text
        final TextView fullname_text = (TextView) findViewById(R.id.name);
        final TextView text_rates = findViewById(R.id.retestxt);
        reference.child(ID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User profile = snapshot.getValue(User.class);
                if (profile != null) {
                    String fullname = profile.getFullName();
                    fullname_text.setText(fullname);
                    int rates=profile.getRates();
                    if(rates==0)
                        text_rates.setText("No rates yet");
                    else if(rates == 1)
                        text_rates.setText((int)(profile.getRating()*20)+"% rating, (1 rate)");
                    else
                        text_rates.setText((int)(profile.getRating()*20)+"% rating, ("+rates+" rates)");
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
                return dinner.compareTo(t1);
            }
        });
        return list;
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