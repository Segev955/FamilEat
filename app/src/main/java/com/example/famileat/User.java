package com.example.famileat;

public class User {

    public String fullName, email, date;
    public int gender;

    public User(){

    }
    public User(String fullName, String email, String date, int gender) {
        this.fullName = fullName;
        this.email = email;
        this.date = date;
        this.gender = gender;
    }

}
