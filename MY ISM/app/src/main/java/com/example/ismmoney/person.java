package com.example.ismmoney;

public class person
{
    // Variable to store data corresponding
    // to firstname keyword in database
    private String time;

    private String room_no;

    // Variable to store data corresponding
    // to lastname keyword in database
    private String checkout_time;
    private String status;

    // Variable to store data corresponding
    // to age keyword in database
    private String in_time;

    // Mandatory empty constructor
    // for use of FirebaseUI
    public person() {}

    // Getter and setter method

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getRoom_no() {
        return room_no;
    }

    public void setRoom_no(String room_no) {
        this.room_no = room_no;
    }

    public String getCheckout_time() {
        return checkout_time;
    }

    public void setCheckout_time(String checkout_time) {
        this.checkout_time = checkout_time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIn_time() {
        return in_time;
    }

    public void setIn_time(String in_time) {
        this.in_time = in_time;
    }
}
