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

    public void setFullName(String fullname)
    {
        this.fullName=fullname;
    }

    public String getEmail() {
        return this.email;
    }

    public String getDate() {
        return this.date;
    }

    public void setDate(String date)
    {
        this.date=date;
    }

    public String getGender() {
        return this.gender;
    }

    public void setGender(String gender)
    {
        this.gender=gender;
    }

    public String getType() {
        return this.type;
    }

    public void editProfile(String fullName, String date, String gender) {
        this.fullName = fullName;
        this.date = date;
        this.gender = gender;
    }



}