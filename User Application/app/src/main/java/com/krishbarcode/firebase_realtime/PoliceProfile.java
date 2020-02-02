package com.krishbarcode.firebase_realtime;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class PoliceProfile extends AppCompatActivity {

    TextView name,contact,loca,email,id, area;
    String policeid;
    ImageView imageView;
    FirebaseFirestore firestore;
    StorageReference proimageref, storageReference;
    FirebaseStorage storage;
    FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_police_profile);
        policeid = getIntent().getStringExtra("policeid");
        imageView = (ImageView)findViewById(R.id.i1);
        firestore = FirebaseFirestore.getInstance();
        name = (TextView)findViewById(R.id.policename);
        id = findViewById(R.id.id);
        area = findViewById(R.id.vehno);
        contact = (TextView)findViewById(R.id.con);
        email = (TextView)findViewById(R.id.email1);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();



        firestore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        String dwnldurl = "police/" + policeid + ".jpg";
        Log.v("dwnldurl", dwnldurl);
        proimageref = storageReference.child(dwnldurl);

        Glide.with(this).using(new FirebaseImageLoader()).load(proimageref).into(imageView);


        firestore.collection(policeid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                name.setText(  document.get("name").toString());
                                id.setText(   document.get("policeid").toString());
                                contact.setText(document.get("con").toString());
                                area.setText(  document.get("area").toString());
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
                Toast.makeText(PoliceProfile.this, "document not found", Toast.LENGTH_SHORT).show();
            }
        });







    }

    public void back(View view)
    {
        onBackPressed();
    }
}