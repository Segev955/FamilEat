package classes;

public class Dinner {

    private String title, date, time, address;
    private int amount, kosher;

    public Dinner(String title, String date, String time, String address, int amount, int kosher) {
        this.title = title;
        this.date = date;
        this.time = time;
        this.address = address;
        this.amount = amount;
        this.kosher = kosher;

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

    public int getKosher() {
        return this.kosher;
    }

    public void setKosher(int kosher) {
        this.kosher = kosher;
    }

}