package com.example.famileat;

import static com.example.famileat.StartActivity.SHARED_PREFS;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
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
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

import adapters.GuestAvAdapder;
import classes.Dinner;
import adapters.GuestMyAdapter;
import classes.User;
import adapters.ChatAdapter;

public class GuestMainActivity extends AppCompatActivity {
    private Button logout, editprofile, meals_btn, search, past;
    private TextView meals_txt;
    private EditText text_date;
    private FirebaseUser user;
    private DatabaseReference reference;
    private DatabaseReference referenceD;
    private String ID;
    private RadioGroup radio_kosher, radio_gender;
    private RadioButton kosher_r, meat_r, dairy_r, notkosher_r, all_r, gender_r, male_r, female_r, both_r;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private RecyclerView recyclerView;
    private GuestAvAdapder avAdapter;
    private GuestMyAdapter myAdapter;
    ArrayList<Dinner> av_dinnerList,my_dinnerList;



    private boolean backPressed = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_main);

        //.........................................................................................
        //Set kosher radio buttons
        meat_r = findViewById(R.id.meat);
        dairy_r = findViewById(R.id.dairy);
        notkosher_r = findViewById(R.id.noKosher);
        all_r = findViewById(R.id.all);
        all_r.setChecked(true);
        radio_kosher = findViewById(R.id.kosher);

        //Set gender radio buttons
        male_r = findViewById(R.id.male);
        female_r = findViewById(R.id.female);
        both_r = findViewById(R.id.both);
        both_r.setChecked(true);
        radio_gender = findViewById(R.id.gender);

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
                DatePickerDialog dateDialog = new DatePickerDialog(GuestMainActivity.this,
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
                int select_kosher = radio_kosher.getCheckedRadioButtonId();
                String kosher = "All";
                if (select_kosher != -1) {
                    kosher_r = (RadioButton) findViewById(select_kosher);
                    kosher = kosher_r.getText().toString();
                }

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Dinner dinner = dataSnapshot.getValue(Dinner.class);
                    if (Dinner.isRelevant(dinner,text_date.getText().toString()) && (kosher.equals("All")||kosher.equals(dinner.getKosher()))) {
                        if (Dinner.isAvailable(dinner) && !Dinner.isAccepted(dinner, ID))
                            av_dinnerList.add(dinner);
                        if (Dinner.isAccepted(dinner, ID))
                            my_dinnerList.add(dinner);
                    }

                }
                date[0] = text_date.getText().toString();
                av_dinnerList = sortDinnersByDate(av_dinnerList);
                my_dinnerList = sortDinnersByDate(my_dinnerList);
                myAdapter.notifyDataSetChanged();
                avAdapter.notifyDataSetChanged();

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
                    Toast.makeText(GuestMainActivity.this,msg,Toast.LENGTH_SHORT).show();
                }
                else {

                    referenceD.addListenerForSingleValueEvent(new ValueEventListener() {
                        @SuppressLint("NotifyDataSetChanged")
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            av_dinnerList.clear();
                            my_dinnerList.clear();
                            int select_kosher = radio_kosher.getCheckedRadioButtonId();
                            String kosher = "All";
                            if (select_kosher != -1) {
                                kosher_r = (RadioButton) findViewById(select_kosher);
                                kosher = kosher_r.getText().toString();
                            }

                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                Dinner dinner = dataSnapshot.getValue(Dinner.class);
                                if (Dinner.isRelevant(dinner, text_date.getText().toString()) && (kosher.equals("All") || kosher.equals(dinner.getKosher()))) {
                                    if (Dinner.isAvailable(dinner) && !Dinner.isAccepted(dinner, ID))
                                        av_dinnerList.add(dinner);
                                    if (Dinner.isAccepted(dinner, ID))
                                        my_dinnerList.add(dinner);
                                }

                            }
                            date[0]=text_date.getText().toString();
                            av_dinnerList = sortDinnersByDate(av_dinnerList);
                            my_dinnerList = sortDinnersByDate(my_dinnerList);
                            myAdapter.notifyDataSetChanged();
                            avAdapter.notifyDataSetChanged();

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }



            }
        });

        //Set past button
        past = findViewById(R.id.past_btn);
        past.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pastIntent = new Intent(GuestMainActivity.this, PastActivity.class);
                pastIntent.putExtra("Type","Guest");
                startActivity(pastIntent);
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
        //Set my meals or available meals
        if(meals_btn.getText().equals("my meals")){
            recyclerView.setAdapter(avAdapter);
            meals_txt.setText("Available Meals:");
        }
        else{
            recyclerView.setAdapter(myAdapter);
            meals_txt.setText("My Meals:");

        }
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