package com.krishbarcode.police_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;

public class searchuser extends AppCompatActivity {

    EditText vehno;
    String veh;
    TextView name,contact,age,adhar,ve;
    FirebaseFirestore firestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchuser);
        vehno = (EditText)findViewById(R.id.vehno1);


    }

    public void serch(View view) {
        veh = vehno.getText().toString().trim();
        Intent i = new Intent(this,viewuserdetail.class);
        i.putExtra("vehno1",veh);
        startActivity(i);



    }
}
