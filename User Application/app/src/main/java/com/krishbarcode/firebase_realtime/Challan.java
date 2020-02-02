package com.krishbarcode.firebase_realtime;


public class Challan {
    String time;
    String date;
    String loc;
    String vehno;
    private boolean is_paid;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLoc() {
        return loc;
    }

    public void setLoc(String loc) {
        this.loc = loc;
    }

    public String getVehno() {
        return vehno;
    }

    public void setVehno(String vehno) {
        this.vehno = vehno;
    }

    public boolean isIs_paid() {
        return is_paid;
    }

    public void setIs_paid(boolean is_paid) {
        this.is_paid = is_paid;
    }

    public Challan() {
    }

    public Challan(String time, String date, String loc, String vehno, boolean is_paid) {
        this.time = time;
        this.date = date;
        this.loc = loc;
        this.vehno = vehno;
        this.is_paid = is_paid;
    }

    public boolean getIsPaid() {
        return is_paid;
    }
}
