package com.krishbarcode.police_app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class user_profile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);


    }

    public void challanDetails(View view) {

        startActivity(new Intent(this,userchallandetail.class));

    }

    public void viewdocuments(View view) {
        startActivity(new Intent(this,userdocument.class));
    }
}
