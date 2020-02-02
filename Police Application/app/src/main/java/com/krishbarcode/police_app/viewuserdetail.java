package com.krishbarcode.police_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class viewuserdetail extends AppCompatActivity {

    TextView name,contact,age,adhar,ve;
    String vehno;
    ImageView imageView;
    FirebaseFirestore firestore;
    StorageReference proimageref, storageReference;
    FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewuserdetail);
        vehno = getIntent().getStringExtra("vehno1");

        Toast.makeText(this, ""+vehno, Toast.LENGTH_SHORT).show();
        imageView = (ImageView)findViewById(R.id.i1);
        firestore = FirebaseFirestore.getInstance();
        name = (TextView)findViewById(R.id.name);
        contact = (TextView)findViewById(R.id.con);
        age = (TextView)findViewById(R.id.age);
        adhar = (TextView)findViewById(R.id.adhar);
        ve = (TextView)findViewById(R.id.vehno);

        firestore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        String dwnldurl = "uploads/" + vehno + ".jpg";
        Log.v("dwnldurl", dwnldurl);
        proimageref = storageReference.child(dwnldurl);

        Glide.with(this).using(new FirebaseImageLoader()).load(proimageref).into(imageView);
        firestore.collection(vehno+"prodata")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {

                                Log.v("load","adding data to component");
                                Log.d("data", "=>" + document.getData() + document.get("name") + document.get("veh") + document.get("email"));
                                name.setText(   "Name       :"+document.get("name").toString());
                                adhar.setText(  "Adhar No   :"+document.get("adhar").toString());
                                contact.setText("Contact  No:"+document.get("con").toString());
                                age.setText(    "Age        :"+document.get("age").toString());
                                ve.setText(     "Vehical no :"+vehno);
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
                Toast.makeText(viewuserdetail.this, "document not found", Toast.LENGTH_SHORT).show();
            }
        });



    }

    public void docuent(View view) {
        Intent i = new Intent(this,userdocument.class);
        i.putExtra("vehno1",vehno);
        startActivity(i);

    }

    public void back(View view) {
        startActivity(new Intent(this,viewchallandetails.class));



    }


}
