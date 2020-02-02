package com.krishbarcode.firebase_realtime;

import android.util.Log;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Kritesh on 08-10-2017.
 */

public class User {

    public String userid;
    public String email;
    public String name;
    public String age;
    public String veh_no;
    public String con_no;
    public String adharno;


    public User() {


        Log.v("user", "user ka constructor call hua");
    }

    public User(String email, String name, String age, String veh_no,
                String con_no, String adharno,String userid) {

        this.userid = userid;
        this.email = email;
        this.name = name;
        this.age = age;
        this.veh_no = veh_no;
        this.con_no = con_no;
        this.adharno = adharno;
    }



    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", userid);
        result.put("Name", name);
        result.put("Email", email);
        result.put("Age", age);
        result.put("Veh.No.", veh_no);
        result.put("Con.No.", con_no);
        result.put("AdharNo", adharno);

        return result;
    }






    public void name(String name) {
        this.name = name;
        Log.v("user", "first name hua");
    }

    public String name() {
        return name;

    }


    public void setemail(String email) {
        this.email = email;
        Log.v("user", "email hua");
    }

    public String getemail() {
        return email;
    }

    public void setage(String age) {
        this.age = age;
        Log.v("user", "age hua");
    }

    public String getAge() {
        return age;
    }


    public void setvehno(String veh_no) {
        this.veh_no = veh_no;
        Log.v("user", "vehical no hua");
    }

    public String getvehno() {
        return veh_no;
    }


    public void setconno(String con_no) {
        this.con_no = con_no;
        Log.v("user", "contact no hua");
    }

    public String getconno() {
        return con_no;
    }


    public void setadhar(String adharno) {
        this.adharno = adharno;
        Log.v("user", "adhar no hua");
    }

    public String getadhar() {
        return adharno;
    }

    public void setUserid(String userid) {
        this.userid = userid;

        Log.v("user", "user id hua");
    }


    public String getuserid() {
        return userid;
    }
}
