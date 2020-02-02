package com.krishbarcode.firebase_realtime;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class Report_Accident extends AppCompatActivity {
    EditText vehno;
    String veh_no;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_accident);
        vehno = (EditText)findViewById(R.id.vehno);

    }

    public void acci(View v)
    {
        veh_no = vehno.getText().toString().trim();
        Intent i4=new Intent(getApplicationContext(),Accident_Search_Activity.class);
        i4.putExtra("vehino",veh_no);
        startActivity(i4);
    }
}
