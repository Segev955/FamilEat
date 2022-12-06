package classes;

import android.os.Build;
import android.text.TextUtils;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

public class Host {
    private String Uid, fullName;
    private List<Dinner> dinners;

    public Host(){
        if (this.dinners==null)
            this.dinners = new LinkedList<Dinner>();
    }
    public Host(String Uid, String fullName) {
        this.Uid = Uid;
        this.fullName = fullName;
        this.dinners = new LinkedList<Dinner>();
    }
    public Host(String Uid, String fullName,List<Dinner> dinners) {
        this.Uid = Uid;
        this.fullName = fullName;
        this.dinners = dinners;
    }

    public String getUid() {
        return Uid;
    }

    public String getFullName() {
        return fullName;
    }

    public List<Dinner> getDinners() {
        return dinners;
    }

    public void addDinner(Dinner dinner) {
        this.dinners.add(dinner);
    }


}

