package classes;

import static android.content.ContentValues.TAG;

import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Dinner {

    private List<String> chat;
    private String ID, hostUid,title, date, time, address, kosher, details, picture;
    private int amount;
    private List<String> acceptedUid, requestsUid;

    public Dinner() {
        this.chat=new ArrayList<String>();

        this.acceptedUid = new ArrayList<String>();
        this.requestsUid= new ArrayList<String>();
    }

    public Dinner(String ID,String hostUid,String title, String date, String time, String address, int amount, String kosher, String details, String picture) {
        this.ID=ID;
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
        this.requestsUid= new ArrayList<String>();
        this.chat=new ArrayList<String>();

    }
    public Dinner(String ID, String hostUid,String title, String date, String time, String address, int amount, String kosher, String details,List<String>requestsUid, String picture,List<String>acceptedUid, List<String>chat) {
        this.ID=ID;
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
        this.requestsUid=requestsUid;
        this.chat = chat;
    }


    public String getID()
    {
        return this.ID;
    }
    public void setID(String id){
        this.ID=id;
    }

    public List<String> getAcceptedUid()
    {
        return this.acceptedUid;
    }

    public List<String> getRequestsUid()
    {
        return this.requestsUid;
    }

    public List<String> getChat()
    {
        return this.chat;
    }

    public void getRequestsUid(List<String>requestsUid)
    {
        this.requestsUid=requestsUid;
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
        return this.picture;
    }

    public void setPicture(String picture) {
        this.picture=picture;
    }



    public static Dinner sendGroupMessage(Dinner dinner, String fullName, String msg, String type){
        if (msg.length()>0) {
            String message = type + "\n" + Request.getCurrDate() + "     " + Request.getCurrTime()+"\n"+ fullName + "\n" + msg;
            dinner.chat.add(message);
        }
        return dinner;
    }
    public static Dinner clearChat(Dinner dinner){
        String msg=dinner.chat.get(0);
        dinner.chat.clear();
        dinner.chat.add(msg);
        return dinner;
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

    public static String check_amount(int amount,int accepted){
        if (amount<1)
            return "Amount must be at least 1.";
        if(amount<accepted)
            return "You have accepted more guests then amount.";
        return "accept";
    }

    public static int numOfAvailables(Dinner dinner) {
        return dinner.amount-dinner.acceptedUid.size();
    }

    public static boolean isAvailable(Dinner dinner)
    {
        return numOfAvailables(dinner)>0;
    }

    public static boolean isRequested(Dinner d,String Uid){
        for(String i:d.requestsUid)
            if (i.equals(Uid))
                return true;
        return false;
    }

    public static boolean isAccepted(Dinner d,String Uid){
        for(String i:d.acceptedUid)
            if (i.equals(Uid))
                return true;
        return false;
    }

    public static Dinner requestUser(Dinner dinner,String Uid){
        if (isRequested(dinner,Uid))
            return null;
        dinner.requestsUid.add(Uid);
        return dinner;
    }

    public static Dinner cancelRequest(Dinner dinner,String Uid){
        if (isRequested(dinner,Uid)) {
            dinner.requestsUid.remove(Uid);
            Request.deleteRequstByDinnerIdAndGuestId(dinner.getID(),Uid);
            return dinner;
        }
        return null;
    }

    public static boolean acceptUser(Dinner dinner, Request request){
        if (!isAvailable(dinner)&&!isRequested(dinner,request.getGuestUid()))
            return false;
        dinner.acceptedUid.add(request.getGuestUid());
        dinner.requestsUid.remove(request.getGuestUid());
        Request.deleteRequstByDinnerIdAndGuestId(dinner.getID(),request.getGuestUid());
        return true;
    }

    public static Dinner removeGuest(Dinner dinner, String currUid) {
        dinner.acceptedUid.remove(currUid);
        return dinner;
    }

    public static void deleteDinnerById(String dinID) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Dinners");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Dinner dinner = dataSnapshot.getValue(Dinner.class);
                    if (dinner.getID().equals(dinID)) {
                        FirebaseDatabase.getInstance().getReference().child("Dinners").child(dinID).removeValue();
                        deletePicture(dinner.getPicture());
                }
            }

        }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }});
    }

    public static void deletePicture(String picName){
        if(!picName.equals("default_dinner.jpg")&&!picName.equals("")) {
            StorageReference storageReference = FirebaseStorage.getInstance().getReference("images/" + picName);
            storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    // File deleted successfully
                    Log.d(TAG, "onSuccess: deleted file");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Uh-oh, an error occurred!
                    Log.d(TAG, "onFailure: did not delete file");
                }
            });
        }
    }



    public static Dinner getDinnerById(String Did){
//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Dinners");;
//        reference.child(Did).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                Dinner dinner = snapshot.getValue(Dinner.class);
//                if(dinner!=null){
//                    ID[0] =dinner.getID();
//                    hostUid[0] =dinner.getHostUid();
//                    title=dinner.getTitle();
//
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//        return dinner[0];
        return null;

    }

}