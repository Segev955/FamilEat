package classes;

import java.util.LinkedList;
import java.util.List;

public class Host {
    private String Uid, fullName;
    private List<Dinner> dinners;

    public Host(String Uid, String fullName) {
        this.Uid = Uid;
        this.fullName = fullName;
        this.dinners = new LinkedList<Dinner>();
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

