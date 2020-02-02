package com.krishbarcode.police_app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.util.HashMap;
import java.util.Map;

public class create_challan_2 extends AppCompatActivity {

    private static final int CAMERA_REQUEST = 1888;
    ImageView imageView;
    EditText vehno;
    FirebaseFirestore firestore;
    private StorageReference storageReference;
    private FirebaseUser firebaseUser;
    private FirebaseStorage storage;
    String vehstring;
    int chal=0;
    Bitmap photo;

    private Uri filePath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_challan_2_mock);
        imageView = (ImageView)findViewById(R.id.veh);
        vehno = (EditText)findViewById(R.id.vehno);
        storage = FirebaseStorage.getInstance();
        firestore = FirebaseFirestore.getInstance();
    }
    public void clickimage(View view)
    { Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);


    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            filePath = data.getData();
            Toast.makeText(this, ""+filePath, Toast.LENGTH_SHORT).show();
            photo = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(photo);
        }
    }
    public void makechallan(View view)
    {

        uploadFile();
        Toast.makeText(this, "start", Toast.LENGTH_SHORT).show();

        String dtandtime="14/11/17";
        vehstring = vehno.getText().toString().trim();
        dtandtime = DateFormat.getDateTimeInstance().toString().trim();

        Map<String,Object> challan1 = new HashMap<>();
        challan1.put("name",vehstring);
        challan1.put("dtndtime",dtandtime);
       // firestore = FirebaseFirestore.getInstance();
        Toast.makeText(this, ""+vehstring, Toast.LENGTH_SHORT).show();

        firestore.collection(vehstring+"challandetails").add(challan1).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                    Log.v("tag","Document is added with id - "+documentReference.getId());
             }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.v("tag","error occured profile"+e);
            }
        });

    }
    private void uploadFile() {
        Toast.makeText(this, ""+filePath, Toast.LENGTH_SHORT).show();


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
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                progressDialog1.dismiss();

                Toast.makeText(create_challan_2.this, "uploaded", Toast.LENGTH_SHORT).show();
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
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


}}
