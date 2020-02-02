package com.krishbarcode.firebase_realtime;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class View_Documents extends AppCompatActivity {
    String vehno = "MH49AN2001";
    StorageReference adharref,licenceref,rcbookref, storageReference;
    FirebaseStorage storage;
    ImageView adhar,licence,rcbook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_documents2);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        adhar = (ImageView)findViewById(R.id.adhar);
        licence = (ImageView)findViewById(R.id.licence);
        rcbook = (ImageView)findViewById(R.id.rcbook);

        String adharurl = vehno+"/" + "adhar.jpg";
        Log.v("dwnldurl", adharurl);
        adharref = storageReference.child(adharurl);

        //Toast.makeText(this, ""+adharurl, Toast.LENGTH_SHORT).show();
       Glide.with(this).using(new FirebaseImageLoader()).load(adharref).into(adhar);
        String licenceurl =vehno+"/" +"licence.jpg";
        Log.v("dwnldurl", licenceurl);
        licenceref = storageReference.child(licenceurl);


        Glide.with(this).using(new FirebaseImageLoader()).load(licenceref).into(licence);

        String rcbookurl = vehno+"/" + "rcbook.jpg";
        Log.v("dwnldurl", rcbookurl);
        rcbookref = storageReference.child(rcbookurl);
        Glide.with(this).using(new FirebaseImageLoader()).load(rcbookref).into(rcbook);



    }
}
