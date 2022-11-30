package classes;

public class User {

    private String fullName, email, date, gender, type;

    public User() {

    }

    public User(String fullName, String email, String date, String gender, String type) {
        this.fullName = fullName;
        this.email = email;
        this.date = date;
        this.gender = gender;
        this.type = type;
    }

    public String getFullName() {
        return this.fullName;
    }

    public String getEmail() {
        return this.email;
    }

    public String getDate() {
        return this.date;
    }

    public String getGender() {
        return this.gender;
    }

    public String getType() {
        return this.type;
    }



}