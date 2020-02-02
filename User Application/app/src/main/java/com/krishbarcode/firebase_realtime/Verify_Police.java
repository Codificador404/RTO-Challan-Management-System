package com.krishbarcode.firebase_realtime;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class Verify_Police extends AppCompatActivity {

    EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify__police);
        editText = findViewById(R.id.vehno);


    }

    public void policeveri(View view)
    {
        Intent i = new Intent(this,PoliceProfile.class);
        i.putExtra("policeid",editText.getText().toString());
        startActivity(i);


    }
}