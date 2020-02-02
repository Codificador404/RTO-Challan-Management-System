package com.krishbarcode.firebase_realtime;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class Profile extends AppCompatActivity {

    private EditText fname;
    private EditText lname;
    private EditText age;
    private EditText veh_no;
    private EditText con_no;
    private EditText adharno;
    private String name;
    private Button save, verify;
    private FirebaseFirestore firestore;
    private String frstname,lstname,ae,veh,con,adhar;
    public static final String MyPREFERENCES = "MyPrefs" ;
    private String USERID = "";
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private DatabaseReference mDatabase, mref, userref;
    private FirebaseDatabase firedatabaase;

    private String globalvehno;
    private String ema;
    private  DocumentReference documentReference;
    private     SharedPreferences sharedpreferences;
    private ProgressDialog probar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        probar = new ProgressDialog(this);


        firestore = FirebaseFirestore.getInstance();

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        firedatabaase = FirebaseDatabase.getInstance();
        mref = firedatabaase.getReference();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        USERID = firebaseUser.getUid();


        firebaseAuth = FirebaseAuth.getInstance();
        fname = (EditText) findViewById(R.id.fname);
        lname = (EditText) findViewById(R.id.lname);
        age = (EditText) findViewById(R.id.age);

        veh_no = (EditText) findViewById(R.id.veh_no);

        con_no = (EditText) findViewById(R.id.contact_no);
        adharno = (EditText) findViewById(R.id.adhar_no);
        save = (Button) findViewById(R.id.save);

        ema = firebaseUser.getEmail();

        if(firebaseUser.isEmailVerified())
        {documentReference = firestore.collection(firebaseUser.getUid().toString().trim()).document("profile");
            documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot documentSnapshot = task.getResult();
                        String name =documentSnapshot.get("name").toString();
                        fname.setText(name.substring(0,name.indexOf(" ")));
                        lname.setText(name.substring(name.indexOf(" "),name.length()));
                        age.setText(documentSnapshot.get("age").toString());
                        veh_no.setText(documentSnapshot.get("veh").toString());
                        con_no.setText(documentSnapshot.get("con").toString());
                        adharno.setText(documentSnapshot.get("adhar").toString());
                    }

                }
            });

        }


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.v("user","user ka save button");
                if (validateForm()) {

                    frstname = fname.getText().toString().trim();
                    lstname = lname.getText().toString().trim();
                    name = frstname +" "+ lstname;
                    ae = age.getText().toString().trim();
                    veh = veh_no.getText().toString().trim();
                    Log.v("veh",veh);
                    globalvehno = veh;
                    con = con_no.getText().toString().trim();
                    adhar = adharno.getText().toString().trim();
                    Log.v("tag","\n"+name+"\n"+ae+"\n"+veh+"\n"+con+"\n"+adhar+"\n");



                    Map<String,Object> user = new HashMap<>();
                    user.put("name",name);
                    user.put("age",ae);
                    user.put("veh",veh);
                    user.put("con",con);
                    user.put("adhar",adhar);
                    user.put("email",ema);
                    user.put("userid",USERID);

                    firestore.collection(veh).document("profile").set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.v("tag","Document is added with id - ");

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
                    firestore.collection(USERID).document("profile").set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.v("tag","Document is added with id - ");

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
//                    firestore.collection(veh+"prodata").add(user).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                        @Override
//                        public void onSuccess(DocumentReference documentReference) {
//
//                            Log.v("tag","Document is added with id - "+documentReference.getId());
//
//
//                        }
//                    }).addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            Log.v("tag","error occured profile"+e);
//                        }
//                    });
                    savevehno();

                }

                Intent i = new Intent(Profile.this,SetProfileImage.class);
                i.putExtra("vehno",veh);
                Log.v("veh",veh);
                startActivity(i);

            }
        });



    }

    private void savevehno() {
        Map<String,Object> veh1 = new HashMap<>();
       veh1.put("vehno",veh);
       veh1.put("userid",firebaseAuth.getInstance().getCurrentUser().getUid());
        //Toast.makeText(this, ""+veh_no+firebaseUser.getUid(), Toast.LENGTH_SHORT).show();
        firestore.collection(firebaseAuth.getInstance().getCurrentUser().getUid()+"vehno").add(veh1).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                Log.v("veh","vehno stored");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.v("veh","error occured profile"+e);

            }
        });

    }

    public String getvehno()
    {
        return globalvehno;
    }

    public boolean validateForm() {
        boolean alldone = true;
        String frstname = fname.getText().toString().trim();
        String lstname = lname.getText().toString().trim();

        if (TextUtils.isEmpty(frstname)) {
            fname.setError("Enter your first name");
            return false;
        } else {
            alldone = true;
            fname.setError(null);
        }
        if (TextUtils.isEmpty(lstname)) {
            lname.setError("Enter your last name");
            return false;
        }
        if(con_no.getText().length()!=10)
        {
            con_no.setError("Invalid Contact Number");
            android.widget.Toast.makeText(this, "Invalid Contact Number", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(adharno.getText().length()!=12)
        {
            adharno.setError("Invalid Aadhaar Number");
            android.widget.Toast.makeText(this, "Invalid Aadhaar Number", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(Pattern.matches("^[A-Z]{2}[0-9]{2}[A-Z]{2}[0-9]{4}$", veh_no.getText()))
        {
            alldone = true;
            veh_no.setError(null);
        }
        else
        {
            veh_no.setError("Invalid Vehicle Number");
            android.widget.Toast.makeText(this, "Invalid Vehicle Number ( eg.: MH49AN2001 )", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(age.getText().length()>3 && (Integer.getInteger(age.getText().toString()) < 122))
        {
            age.setError("Invalid age");
           // android.widget.Toast.makeText(this, "Invalid Aadhaar Number", Toast.LENGTH_SHORT).show();
            return false;
        }



        return alldone;
    }


}


