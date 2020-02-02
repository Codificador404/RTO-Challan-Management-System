package com.krishbarcode.firebase_realtime;

import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Calendar;
import java.util.Date;

public class SOSActivity extends AppCompatActivity {
    ImageView image ;
    String vehno="";
    private FirebaseFirestore firestore;
    private FirebaseUser firebaseUser;
    private String conn1="";
    private String conn2="";
    private String conn3="";
    private String locationlink;
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 0;
    private static final int LOCATION_PERMISSION_ID = 1001;
    private ProgressDialog probar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sos);
        vehno = getIntent().getStringExtra("vehino");
        image = (ImageView) findViewById(R.id.sosimage);

        firestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        probar = new ProgressDialog(this);
        probar.setMessage("fetching data from server.....");
        probar.setCancelable(false);
        probar.show();



        firestore.collection(vehno + "contacts")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                Log.d("data", "=>" + document.getData() + document.get("contact1") + document.get("contact2") + document.get("contact3"));
                                conn1 = document.get("contact1").toString().trim();
                                conn2 = document.get("contact2").toString().trim();
                                conn3 = document.get("contact3").toString().trim();
                                probar.dismiss();
                                if (document.exists())
                                    Toast.makeText(SOSActivity.this, "Data fetched succesfully", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Log.e("data", "error occured user main" + task.getException());
                        }

                    }
                });
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);






        vehno = getIntent().getStringExtra("vehino");
        image = (ImageView)findViewById(R.id.image);


    }
    private void MyMessage(String phone) {
        locationlink = "http://www.google.com/maps/place/21.1764298,79.0595306";

        SmsManager smsManager = SmsManager.getDefault();
        Date currentTime = Calendar.getInstance().getTime();
        smsManager.sendTextMessage(phone, null, "The vehical no "+vehno+" has met with an accident on "+currentTime+".\n"+"Location : "+locationlink, null, null);
        Toast.makeText(this, "Message sent successfully to " + phone, Toast.LENGTH_SHORT).show();
    }

    public void notifyer(View viewz) {
        int i = conn1.length();
        conn1 = conn1.substring(i - 10, i);
        int j = conn2.length();
        conn2 = conn2.substring(j - 10, j);
        int k = conn3.length();
        conn3 = conn3.substring(k - 10, k);

            MyMessage(conn1);
            MyMessage(conn2);
            MyMessage(conn3);
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{ android.Manifest.permission.SEND_SMS,android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_ID);
            return;
        }
        int permissinCheck = ContextCompat.checkSelfPermission(
                this, android.Manifest.permission.SEND_SMS);

        if (permissinCheck == PackageManager.PERMISSION_GRANTED) {

                MyMessage(conn1);
                MyMessage(conn2);
                MyMessage(conn3);


        } else {
            //TODO
            ActivityCompat.requestPermissions(this, new String[]
                            {
                                    android.Manifest.permission.SEND_SMS},
                    MY_PERMISSIONS_REQUEST_SEND_SMS);

        }

    }


}
