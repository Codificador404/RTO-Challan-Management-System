package com.krishbarcode.police_app;



public class Challan {
    String time;
    String date;
    String loc;
    String vehno;

    public Challan() {
    }

    public Challan(String time, String date, String loc, String vehno) {
        this.time = time;
        this.date = date;
        this.loc = loc;
        this.vehno = vehno;
    }
}
