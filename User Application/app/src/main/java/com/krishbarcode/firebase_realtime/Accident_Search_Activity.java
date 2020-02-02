package com.krishbarcode.firebase_realtime;

import android.*;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.location.DetectedActivity;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.nlopez.smartlocation.OnActivityUpdatedListener;
import io.nlopez.smartlocation.OnGeofencingTransitionListener;
import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.OnReverseGeocodingListener;
import io.nlopez.smartlocation.SmartLocation;
import io.nlopez.smartlocation.geofencing.model.GeofenceModel;
import io.nlopez.smartlocation.geofencing.utils.TransitionGeofence;
import io.nlopez.smartlocation.location.providers.LocationGooglePlayServicesProvider;

public class Accident_Search_Activity extends AppCompatActivity implements OnLocationUpdatedListener, OnActivityUpdatedListener, OnGeofencingTransitionListener  {
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 0;
    private String vehno;
    private TextView name, con1, con2, con3;
    private String conn1, conn2, conn3;
    private FirebaseFirestore firestore;
    private ImageView imageView;
    private StorageReference proimageref, storageReference;
    private FirebaseStorage storage;
    private FirebaseUser firebaseUser;
    String locaction="null",locationlink="";
    Button b;

    private LocationGooglePlayServicesProvider provider;
    private static final int LOCATION_PERMISSION_ID = 1001;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acci_search);

        vehno = getIntent().getStringExtra("vehino");
        b = (Button)findViewById(R.id.sn);
        name = (TextView) findViewById(R.id.name);
        con1 = (TextView) findViewById(R.id.con1);
        con2 = (TextView) findViewById(R.id.con2);
        con3 = (TextView) findViewById(R.id.con3);
        imageView =(ImageView)findViewById(R.id.i1);
        firestore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        storageReference = storage.getReference();
        String dwnldurl = "uploads/" + vehno + ".jpg";
        Log.v("dwnldurl", dwnldurl);
        proimageref = storageReference.child(dwnldurl);

        Glide.with(this).using(new FirebaseImageLoader()).load(proimageref).into(imageView);

        firestore.collection(vehno + "prodata")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                Log.d("data", "=>" + document.getData() + document.get("name") + document.get("veh") + document.get("email"));
                                name.setText("Name :    " + document.get("name").toString());

                            }

                        } else {
                            Log.e("data", "error occured user main" + task.getException());
                        }

                    }
                });
        firestore.collection(vehno + "contacts")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                Log.d("data", "=>" + document.getData() + document.get("contact1") + document.get("contact2") + document.get("contact3"));
                                con1.setText("1. " + document.get("contact1"));
                                Log.v("tag", conn1 + "  " + conn2 + "  " + conn3);

                                conn1 = document.get("contact1").toString().trim();
                                Log.v("tag", conn1 + "  " + conn2 + "  " + conn3);

                                con2.setText("2. " + document.get("contact2"));
                                conn2 = document.get("contact2").toString().trim();
                                con3.setText("3. " + document.get("contact3"));


                                conn3 = document.get("contact3").toString().trim();
                                Log.v("tag", conn1 + "  " + conn2 + "  " + conn3);
                            }

                        } else {
                            Log.e("data", "error occured user main" + task.getException());
                        }

                    }
                });

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        showLast();

    }

    public void notify(View v) {
        if (ContextCompat.checkSelfPermission(Accident_Search_Activity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Accident_Search_Activity.this, new String[]{ android.Manifest.permission.SEND_SMS,android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_PHONE_STATE}, LOCATION_PERMISSION_ID);
            return;
        }
        startLocation();
        int i = conn1.length();
        conn1 = conn1.substring(i - 10, i);
        int j = conn2.length();
        conn2 = conn2.substring(j - 10, j);
        int k = conn3.length();
        conn3 = conn3.substring(k - 10, k);

        Log.v("tag",conn1 +"  "+conn2+"  "+conn3);
//        String r="9156556835";
//        SmsManager smsManager = SmsManager.getDefault();
//        smsManager.sendTextMessage(conn2, null, "https://goo.gl/idSgox", null, null);
//        Toast.makeText(this, "Message sent successfully", Toast.LENGTH_SHORT).show();
        int permissinCheck = ContextCompat.checkSelfPermission(Accident_Search_Activity.this, android.Manifest.permission.SEND_SMS);

        if (permissinCheck == PackageManager.PERMISSION_GRANTED) {
            //Name of method for calling Message
            MyMessage(conn1);
            MyMessage(conn2);
            MyMessage(conn3);

        } else {
            //TODO
            ActivityCompat.requestPermissions(Accident_Search_Activity.this, new String[]
                            {
                                    android.Manifest.permission.SEND_SMS, Manifest.permission.READ_PHONE_STATE},
                    MY_PERMISSIONS_REQUEST_SEND_SMS);

        }

    }

    private void startLocation() {
        provider = new LocationGooglePlayServicesProvider();
        provider.setCheckLocationSettings(true);

        SmartLocation smartLocation = new SmartLocation.Builder(this).logging(true).build();

        smartLocation.location(provider).start(this);
        smartLocation.activity().start((OnActivityUpdatedListener) this);

        // Create some geofences
        GeofenceModel mestalla = new GeofenceModel.Builder("1").setTransition(Geofence.GEOFENCE_TRANSITION_ENTER).setLatitude(39.47453120000001).setLongitude(-0.358065799999963).setRadius(500).build();
        smartLocation.geofencing().add(mestalla).start((OnGeofencingTransitionListener) this);
    }

    private void MyMessage(String phone) {
        SmsManager smsManager = SmsManager.getDefault();
        Date currentTime = Calendar.getInstance().getTime();
        smsManager.sendTextMessage(phone, null, "The vehical no "+vehno+" has met with an accident on "+currentTime+".\n"+"Location : "+locationlink, null, null);
        Toast.makeText(this, "Message sent successfully to " + phone, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
                if (grantResults.length >= 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    String dtandtime = DateFormat.getDateTimeInstance().toString().trim();
                    String msg="The "+vehno+" has met with an accident on "+dtandtime+".\n"+"Location : https://goo.gl/idSgox";
                    //Name of Method for Calling Message
                    MyMessage(conn1);
                    MyMessage(conn2);
                    MyMessage(conn3);
                } else {
                    Toast.makeText(this, "You dont have required permission to make the Action", Toast.LENGTH_SHORT).show();
                }
            }
        }
        if (requestCode == LOCATION_PERMISSION_ID && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startLocation();
        }


    }

    @Override
    public void onLocationUpdated(Location location) {
        showLocation(location);

    }
    private void showLast() {
        Location lastLocation = SmartLocation.with(this).location().getLastLocation();
        if (lastLocation != null) {
            locationlink = "http://www.google.com/maps/place/"+lastLocation.getLatitude()+","+
                            lastLocation.getLongitude();

        }
        else
        {
            locationlink = "http://www.google.com/maps/place/21.1764298,79.0595306";
        }

        DetectedActivity detectedActivity = SmartLocation.with(this).activity().getLastActivity();
        if (detectedActivity != null) {
//            activityText.setText(
//                    String.format("[From Cache] Activity %s with %d%% confidence",
//                            getNameFromType(detectedActivity),
//                            detectedActivity.getConfidence())
            //);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (provider != null) {
            provider.onActivityResult(requestCode, resultCode, data);
        }
    }
    private void stopLocation() {
        SmartLocation.with(this).location().stop();
       // locationText.setText("Location stopped!");

        SmartLocation.with(this).activity().stop();
        //activityText.setText("Activity Recognition stopped!");

        SmartLocation.with(this).geofencing().stop();
        //geofenceText.setText("Geofencing stopped!");
    }

    private void showLocation(Location location) {
        if (location != null) {
            locationlink = "http://www.google.com/maps/place/"+location.getLatitude()+","+
                    location.getLongitude();

            // We are going to get the address for the current position
            SmartLocation.with(this).geocoding().reverse(location, new OnReverseGeocodingListener() {
                @Override
                public void onAddressResolved(Location original, List<Address> results) {
                    if (results.size() > 0) {
                        Address result = results.get(0);
                        StringBuilder builder = new StringBuilder();
                        builder.append("\nAddress : ");
                        List<String> addressElements = new ArrayList<>();
                        for (int i = 0; i <= result.getMaxAddressLineIndex(); i++) {
                            addressElements.add(result.getAddressLine(i));
                        }
                        builder.append(TextUtils.join(", ", addressElements));
                        locaction=builder.toString();
                    }
                }
            });
        } else {
           locaction="Null location";
        }
    }


    private void showActivity(DetectedActivity detectedActivity) {
//        if (detectedActivity != null) {
//            activityText.setText(
//                    String.format("Activity %s with %d%% confidence",
//                            getNameFromType(detectedActivity),
//                            detectedActivity.getConfidence())
//            );
//        } else {
//            activityText.setText("Null activity");
//        }
    }


//    private void showGeofence(Geofence geofence, int transitionType) {
//        if (geofence != null) {
//            geofenceText.setText("Transition " + getTransitionNameFromType(transitionType) + " for Geofence with id = " + geofence.getRequestId());
//        } else {
//            geofenceText.setText("Null geofence");
//        }
//    }
    @Override
    public void onActivityUpdated(DetectedActivity detectedActivity) {
        showActivity(detectedActivity);
    }

    @Override
    public void onGeofenceTransition(TransitionGeofence geofence) {
       // showGeofence(geofence.getGeofenceModel().toGeofence(), geofence.getTransitionType());
    }

    private String getNameFromType(DetectedActivity activityType) {
        switch (activityType.getType()) {
            case DetectedActivity.IN_VEHICLE:
                return "in_vehicle";
            case DetectedActivity.ON_BICYCLE:
                return "on_bicycle";
            case DetectedActivity.ON_FOOT:
                return "on_foot";
            case DetectedActivity.STILL:
                return "still";
            case DetectedActivity.TILTING:
                return "tilting";
            default:
                return "unknown";
        }
    }

    private String getTransitionNameFromType(int transitionType) {
        switch (transitionType) {
            case Geofence.GEOFENCE_TRANSITION_ENTER:
                return "enter";
            case Geofence.GEOFENCE_TRANSITION_EXIT:
                return "exit";
            default:
                return "dwell";
        }
    }


//    public void policenotification(View view) {
//        SmsManager smsManager = SmsManager.getDefault();
//        Date currentTime = Calendar.getInstance().getTime();
//        //Mocked Police number to Anushree's num
//        smsManager.sendTextMessage("9130077550", null, "The vehical no "+vehno+" has met with an accident on "+currentTime+".\n"+"Location : "+locationlink, null, null);
//        Toast.makeText(this, "Message sent to Police", Toast.LENGTH_SHORT).show();
//    }
//
//    public void ambulancenotification(View view) {
//        SmsManager smsManager = SmsManager.getDefault();
//        Date currentTime = Calendar.getInstance().getTime();
//        //Mocked Ambulance number to Nandini's num
//        smsManager.sendTextMessage("7057410143", null, "The vehical no "+vehno+" has met with an accident on "+currentTime+".\n"+"Location : "+locationlink, null, null);
//        Toast.makeText(this, "Message sent to Ambulance", Toast.LENGTH_SHORT).show();
//    }
}

