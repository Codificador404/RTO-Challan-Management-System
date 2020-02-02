package com.krishbarcode.firebase_realtime;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.security.interfaces.DSAKey;

public class UserMainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    TextView name, navname, veh, navemail;
    ImageView proimage, navimage;
    StorageReference proimageref, storageReference;
    FirebaseStorage storage;
    FirebaseUser firebaseUser;
    View header;
    DatabaseReference mDatabase;
    FirebaseFirestore firestore;
    DocumentReference documentReference;
    private FirebaseAuth firebaseAuth;
    public static final String MyPREFERENCES = "MyPrefs";
    String vehno1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        setSupportActionBar(toolbar);
        firebaseAuth = FirebaseAuth.getInstance();
        header = navigationView.getHeaderView(0);

        name = (TextView) findViewById(R.id.name);
        veh = (TextView) findViewById(R.id.veh_no);
        proimage = (ImageView) findViewById(R.id.proimage);
        navimage = (ImageView) header.findViewById(R.id.navproimage);
        navname = (TextView) header.findViewById(R.id.navname);
        navemail = (TextView) header.findViewById(R.id.navemail);

        storage = FirebaseStorage.getInstance();
        firestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        storageReference = storage.getReference();

        documentReference = firestore.collection(firebaseUser.getUid().toString().trim()).document("profile");
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    name.setText(documentSnapshot.get("name").toString());
                    navname.setText(documentSnapshot.get("name").toString());
                    veh.setText(documentSnapshot.get("veh").toString());
                    navemail.setText(documentSnapshot.get("email").toString());
                }

            }
        });

        Log.v("try", "vehno:" + vehno1);
        String dwnldurl = "uploads/" + firebaseUser.getUid().toString().trim() + ".jpg";
        Log.v("dwnldurl", dwnldurl);
        proimageref = storageReference.child(dwnldurl);

        Glide.with(this).using(new FirebaseImageLoader()).load(proimageref).into(proimage);

        Glide.with(this).using(new FirebaseImageLoader()).load(proimageref).into(navimage);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            finish();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.edit_pro) {
            startActivity(new Intent(UserMainActivity.this, Profile.class));

        } else if (id == R.id.log_out) {

            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(UserMainActivity.this, MainActivity.class));
        } else if (id == R.id.addcontacts) {
            Intent i2 = new Intent(getApplicationContext(), AddContacts.class);
            i2.putExtra("vehno", veh.getText().toString().trim());
            startActivity(i2);
        } else if (id == R.id.changepropic) {
            startActivity(new Intent(UserMainActivity.this, SetProfileImage.class));
        } else if (id == R.id.view_document) {
            startActivity(new Intent(this, View_Documents.class));

        } else if (id == R.id.noParking) {
            startActivity(new Intent(this, NoParking_Search.class));

        } else if (id == R.id.verifypolice) {
            startActivity(new Intent(this, Verify_Police.class));

        } else if (id == R.id.report_crime) {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setData(Uri.parse("email"));
            String[] s ={"rto@gmail.com"};
            i.putExtra(Intent.EXTRA_EMAIL,s);
            i.putExtra(Intent.EXTRA_SUBJECT, "Filing a Complaint");
            i.putExtra(Intent.EXTRA_TEXT, "This is to file a complaint regarding a crime");
            i.setType("message/rfc822");
            Intent chooser = Intent.createChooser(i, "Launch Email");
            startActivity(chooser);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void reportAccident(View view) {
        Intent i3 = new Intent(getApplicationContext(), Report_Accident.class);
        startActivity(i3);
    }

    public void challanDetails(View view) {
        Intent i2 = new Intent(getApplicationContext(), ChallanDetails.class);
        i2.putExtra("vehno", veh.getText().toString().trim());
        startActivity(i2);
    }


    public void notifytoactivity(View view) {
        Intent i = new Intent(this, SOSActivity.class);
        i.putExtra("vehino", veh.getText().toString());
        startActivity(i);

    }
}
