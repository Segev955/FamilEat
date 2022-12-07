package classes;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Request {
    private String requestId, hostUid, guestUid, dinnerid;
    private boolean isAccepted;

    public Request(){
        this.isAccepted = false;
    }
    public Request(String requestId, String hostUid,String guestUid, String dinnerid){
        this.requestId=requestId;
        this.hostUid = hostUid;
        this.guestUid = guestUid;
        this.dinnerid = dinnerid;
        this.isAccepted = false;
    }

    public Request(String requestId, String hostUid,String guestUid, String dinnerid, boolean isAccepted){
        this.requestId=requestId;
        this.hostUid = hostUid;
        this.guestUid = guestUid;
        this.dinnerid = dinnerid;
        this.isAccepted = isAccepted;
    }

    public String getRequestId() {return this.requestId;}
    public String getHostUid(){
        return this.hostUid;
    }
    public String getGuestUid(){
        return this.guestUid;
    }
    public String getDinnerid(){
        return this.dinnerid;
    }
    public void setId(String ID){
        this.requestId=requestId;
    }
    public void setHostUid(String hostUid){
        this.hostUid=hostUid;
    }
    public void setGuestUid(String guestUid){
        this.guestUid=guestUid;
    }
    public void setDinnerid(String dinnerid){
        this.dinnerid=dinnerid;
    }

    public static boolean acceptRequest(Request r)
    {
        Dinner d =Dinner.getDinnerById(r.dinnerid);
        if (d==null)
            return false;
        return Dinner.acceptUser(d,r.guestUid);
    }

    public static void deleteRequstByDinnerIdAndGuestId(String dinID,String guestID){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Requests");;
        reference.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Request request = dataSnapshot.getValue(Request.class);
                    if(request.dinnerid.equals(dinID) && request.guestUid.equals(guestID)){
                        FirebaseDatabase.getInstance().getReference().child("Requests").child(request.requestId).removeValue();
                        break;
                    }

                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}

