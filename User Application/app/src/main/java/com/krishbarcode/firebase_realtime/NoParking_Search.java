package com.krishbarcode.firebase_realtime;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class NoParking_Search extends AppCompatActivity {
    EditText vehno;
    String veh_no;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_parking__search);
        vehno = (EditText)findViewById(R.id.vehno);




    }

    public void noparking(View view)
    {
        veh_no = vehno.getText().toString().trim();
        Intent i4=new Intent(getApplicationContext(),NoParking_Report.class);
        i4.putExtra("vehino",veh_no);
        startActivity(i4);

    }
}
