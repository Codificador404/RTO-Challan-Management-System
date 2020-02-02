package com.krishbarcode.police_app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class search_rep extends AppCompatActivity {
EditText vehno;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_rep);
        vehno = (EditText)findViewById(R.id.vehno1);


    }

    public void acci(View view) {
        //Intent i  =new Intent(this,view_report.class);
        Toast.makeText(this, ""+vehno.getText().toString(), Toast.LENGTH_SHORT).show();
        //i.putExtra("vehno1",vehno.getText().toString().trim());


       // startActivity(i);
    }
}
