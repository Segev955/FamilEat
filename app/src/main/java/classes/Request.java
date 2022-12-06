package classes;

public class Request {
    private String hostUid, guestUid, dinnerUid;
    private boolean isAccepted;

    public Request(){
        this.isAccepted = false;
    }
    public Request(String hostUid,String guestUid, String dinnerUid){
        this.hostUid = hostUid;
        this.guestUid = guestUid;
        this.dinnerUid = dinnerUid;
        this.isAccepted = false;
    }

    public Request(String hostUid,String guestUid, String dinnerUid, boolean isAccepted){
        this.hostUid = hostUid;
        this.guestUid = guestUid;
        this.dinnerUid = dinnerUid;
        this.isAccepted = isAccepted;
    }


    public String getHostUid(){
        return this.hostUid;
    }
    public String getGuestUid(){
        return this.guestUid;
    }
    public String getDinnerUid(){
        return this.dinnerUid;
    }

    public static boolean acceptRequest(Request r)
    {
        Dinner d =Dinner.getDinnerById(r.dinnerUid);
        if (d==null)
            return false;
        return Dinner.acceptUser(d,r.guestUid);
    }


}

