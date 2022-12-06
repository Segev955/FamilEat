package classes;

import android.annotation.SuppressLint;
import android.os.Build;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Dinner {

    private String hostUid,title, date, time, address, kosher, details, picture;
    private int amount;
    private List<String> acceptedUid;
    public Dinner() {this.acceptedUid = new ArrayList<String>();}

    public Dinner(String hostUid,String title, String date, String time, String address, int amount, String kosher, String details, String picture) {
        this.hostUid=hostUid;
        this.title = title;
        this.date = date;
        this.time = time;
        this.address = address;
        this.amount = amount;
        this.kosher = kosher;
        this.details = details;
        this.picture=picture;
        if(this.picture.equals(""))
            this.picture="no picture";
        this.acceptedUid = new ArrayList<String>();
        this.getAcceptedUid().add("hhh");
    }
    public Dinner(String hostUid,String title, String date, String time, String address, int amount, String kosher, String details, String picture,List<String>acceptedUid) {
        this.hostUid=hostUid;
        this.title = title;
        this.date = date;
        this.time = time;
        this.address = address;
        this.amount = amount;
        this.kosher = kosher;
        this.details = details;
        this.picture=picture;
        if(this.picture.equals(""))
            this.picture="no picture";
        this.acceptedUid = acceptedUid;
    }

    public List<String> getAcceptedUid()
    {
        return this.acceptedUid;
    }

    public String getHostUid() {
        return this.hostUid;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return this.time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getAmount() {
        return this.amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getKosher() {
        return this.kosher;
    }

    public void setKosher(String kosher) {
        this.kosher = kosher;
    }

    public String getDetails() {
        return this.details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getPicture() {
        return picture;
    }

    public static String check_title(String title) {
        if (TextUtils.isEmpty(title))
            return "Please enter a title.";
        if (title.length()<4)
            return "Title too short (at least 4 characters.)";
        return "accept";
    }

    public static String check_date_time(String date, String time) {
        //date
        if (TextUtils.isEmpty(date))
            return "Please enter date (dd/mm/yyyy).";
        String[] splited = date.split("/");
        if (splited.length != 3)
            return "Please ender valid date (dd/mm/yyyy)";
        int day, month, year;
        try {
            day = Integer.parseInt(splited[0]);
            month = Integer.parseInt(splited[1]);
            year = Integer.parseInt(splited[2]);
        } catch (NumberFormatException nfe) {
            return "Date must be numbers (dd/mm/yyyy)";
        }
        int nowy = 2023, nowm = 1, nowd = 1;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            nowy = LocalDate.now().getYear();
            nowm = LocalDate.now().getMonth().getValue();
            nowd = LocalDate.now().getDayOfMonth();
        }
        if (year < nowy || year > nowy+3)
            return "Year not valid";
        if (month < 1 || month > 12)
            return "Month not valid";
        if (day < 1 || day > 31 || (day == 31 && (month == 4 || month == 6 || month == 9 || month == 11)) || (month == 2 && (day > 29 || (day == 29 && (year % 4 != 0 || (year % 100 == 0 && year % 400 != 0))))))
            return "Day not valid";
        if (year == nowy &&(month<nowm||(month==nowm&&day<nowd)))
            return "The date has already passed.";

        //time
        if (TextUtils.isEmpty(time))
            return "Please pick time.";
        splited = time.split(":");
        if (splited.length != 2)
            return "Please pick time.";
        int hour, minute;
        try {
            hour = Integer.parseInt(splited[0]);
            minute = Integer.parseInt(splited[1]);
        } catch (NumberFormatException nfe) {
            return "time must be numbers (hh:mm)";
        }
        if (hour < 0 || hour > 23)
            return "Hour not valid";
        if (minute < 0 || minute > 59)
            return "Minute not valid";
        final Calendar c = Calendar.getInstance();
        int nowho = c.get(Calendar.HOUR_OF_DAY);
        int nowmi = c.get(Calendar.MINUTE);
        if ( (year == nowy && month == nowm && day == nowd)  && (hour<nowho||(hour==nowho&&minute<nowmi)) )
            return "The time has already passed.";

        return "accept";

    }

    public static String check_amount(int amount){
        if (amount<1)
            return "Amount must be at least 1.";
        return "accept";
    }

    public static int numOfAvailables(Dinner dinner) {
        return dinner.amount-dinner.acceptedUid.size();
    }

    public static boolean isAvailable(Dinner dinner)
    {
        return numOfAvailables(dinner)>0;
    }
    public static boolean acceptUser(Dinner dinner, String Uid){
        if (!isAvailable(dinner))
            return false;
        dinner.acceptedUid.add(Uid);
        return true;
    }

    public static Dinner getDinnerById(String Did){
        final Dinner[] dinner = {null};
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Dinners");;
        reference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    if(dataSnapshot.getKey()==Did)
                        dinner[0] = dataSnapshot.getValue(Dinner.class);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return dinner[0];

    }

}