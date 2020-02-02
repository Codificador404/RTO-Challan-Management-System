package com.krishbarcode.police_app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.ui.auth.ui.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Police_main extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final int CAMERA_REQUEST = 1888;
    ImageView imageView;
    EditText vehno;
    FirebaseFirestore firestore;
    private StorageReference storageReference;
    private FirebaseUser firebaseUser;
    private FirebaseStorage storage;
    String vehstring;
    private DatabaseReference mFirebaseParkingDatabase;
    private FirebaseDatabase mFirebaseInstance;

    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;

    int chal=0;
    Bitmap photo;
    private FirebaseAuth firebaseAuth;

    private Uri filePath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_police_main);
        imageView = (ImageView)findViewById(R.id.veh);
        vehno = (EditText)findViewById(R.id.vehno);
        storage = FirebaseStorage.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        auth = FirebaseAuth.getInstance();
        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    startActivity(new Intent(Police_main.this, Login_page_1.class));
                    finish();
                } else {
                    //Toast.makeText(ParkingActivity.this, "User not null!!", Toast.LENGTH_SHORT).show();
                }
            }
        };

        mFirebaseInstance = FirebaseDatabase.getInstance();
        mFirebaseParkingDatabase = mFirebaseInstance.getReference("challandetails");


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.police_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.serch_user) {
            startActivity(new Intent(this,searchuser.class));
        }

//        } else if (id == R.id.rep_crime) {
//
//
//            Intent i=new Intent(Intent.ACTION_SEND);
//            i.setData(Uri.parse("email"));
//            i.putExtra(Intent.EXTRA_EMAIL,"kriteshsharma2014@gmail.com");
//            i.putExtra(Intent.EXTRA_SUBJECT,"Filing a Complaint");
//            i.putExtra(Intent.EXTRA_TEXT,"This is to file a complaint regarding a crime");
//            i.setType("message/rfc822");
//            Intent chooser = Intent.createChooser(i,"Launch Email");
//            startActivity(chooser);
//
//
//        }
        else if (id == R.id.rep_acci) {
            startActivity(new Intent(this,search_rep.class));

        }
        else if (id == R.id.logout) {
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(this, Login_page_1.class));

        }
        else if (id == R.id.nav_camera) {
            startActivity(new Intent(this,viewPolicedetail.class));

        }
        else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void clickimage(View view)
    { Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);


    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            filePath = data.getData();
           // Toast.makeText(this, ""+filePath, Toast.LENGTH_SHORT).show();
            photo = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(photo);
        }
    }
    public void makechallan(View view)
    {

        vehstring = vehno.getText().toString().trim();
        vehstring = mFirebaseParkingDatabase.push().getKey();

        SimpleDateFormat currentMonth = new SimpleDateFormat("dd/MM/yyyy");
        Date todayMonth = new Date();
        String thisDate = currentMonth.format(todayMonth);

        String time="";
        String loc="";


        Challan c = new Challan(time,thisDate,loc,vehstring);
        mFirebaseParkingDatabase.child(vehstring).setValue(c);

        uploadFile();
       // Toast.makeText(this, "start", Toast.LENGTH_SHORT).show();

//        String dtandtime="15/11/17";
//        vehstring = vehno.getText().toString().trim();
//        dtandtime = DateFormat.getDateTimeInstance().toString().trim();
//
//        Map<String,Object> challan1 = new HashMap<>();
//        challan1.put("name",vehstring);
//        challan1.put("dtndtime",dtandtime);
//        // firestore = FirebaseFirestore.getInstance();
//      //  Toast.makeText(this, ""+vehstring, Toast.LENGTH_SHORT).show();
//
//        firestore.collection(vehstring+"challandetails").add(challan1).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//            @Override
//            public void onSuccess(DocumentReference documentReference) {
//                Log.v("tag","Document is added with id - "+documentReference.getId());
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Log.v("tag","error occured profile"+e);
//            }
//        });

    }
    private void uploadFile() {
       // Toast.makeText(this, ""+filePath, Toast.LENGTH_SHORT).show();

        vehstring = vehno.getText().toString().trim();

        storageReference = storage.getReference();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final ProgressDialog progressDialog1 = new ProgressDialog(this);
        progressDialog1.setTitle("Wait.....");
        progressDialog1.show();

        //getting the storage reference
        StorageReference sRef1 = storageReference.child(vehstring+"/"+vehstring+"challan");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = sRef1.putBytes(data);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                progressDialog1.dismiss();

                Toast.makeText(Police_main.this, "uploaded", Toast.LENGTH_SHORT).show();
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                progressDialog1.dismiss();
                Log.v("tag",exception.toString());
                // Handle unsuccessful uploads
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                //displaying the upload progress
                @SuppressWarnings("VisibleForTests")double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                progressDialog1.setMessage("Uploaded " + ((int) progress) + "%...");
            }
        });

        if (filePath != null) {
            //displaying progress dialog while image is uploading
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Wait.....");
            progressDialog.show();
            storageReference = storage.getReference();
            firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

            //getting the storage reference
            vehstring = vehno.getText().toString().trim();
            StorageReference sRef = storageReference.child(vehstring+"/"+vehstring+"challan");
            sRef.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //dismissing the progress dialog
                            progressDialog.dismiss();

                            //displaying success toast
                            Toast.makeText(getApplicationContext(), "File Uploaded ", Toast.LENGTH_LONG).show();

                            //adding an upload to firebase database


                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            //displaying the upload progress
                            @SuppressWarnings("VisibleForTests")double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                        }
                    });
        } else {
            //display an error if no file is selected
        }


    }

}
