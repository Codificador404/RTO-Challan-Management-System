package com.krishbarcode.police_app;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class viewPolicedetail extends AppCompatActivity {

    TextView name,contact,loca,email;
    String vehno;
    ImageView imageView;
    FirebaseFirestore firestore;
    StorageReference proimageref, storageReference;
    FirebaseStorage storage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_policedetail);

        Toast.makeText(this, ""+vehno, Toast.LENGTH_SHORT).show();
        imageView = (ImageView)findViewById(R.id.i1);
        firestore = FirebaseFirestore.getInstance();
        loca = (TextView)findViewById(R.id.locassign);
        name = (TextView)findViewById(R.id.name);
        contact = (TextView)findViewById(R.id.con);
        email = (TextView)findViewById(R.id.email1);


        firestore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        String dwnldurl =  "police/"+"policeman-512.jpg";
        Log.v("dwnldurl", dwnldurl);
        proimageref = storageReference.child(dwnldurl);

       // Glide.with(this).using(new FirebaseImageLoader()).load(proimageref).into(imageView);
        firestore.collection("police")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {

                                Log.v("load","adding data to component");
                                Log.d("data", "=>" + document.getData() + document.get("name") + document.get("veh") + document.get("email"));
                                name.setText(   "Name            :"+document.get("name").toString());
                                loca.setText(   "Location Assign :"+document.get("loca").toString());
                                contact.setText("Contact  No     :"+document.get("con").toString());
                                email.setText(  "Email           :"+document.get("email").toString());
                                }

                        } else {

                            Log.v("load","error");
                            Log.e("data", "error occured user main" + task.getException());
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Log.v("load","data not found");
                Toast.makeText(viewPolicedetail.this, "document not found", Toast.LENGTH_SHORT).show();
            }
        });




    }

    public void back(View view) {
   onBackPressed();
    }


}
