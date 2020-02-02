package com.krishbarcode.firebase_realtime;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class SetProfileImage extends AppCompatActivity implements View.OnClickListener{

    private ImageView imageView;
    private Button but,next;
    private Uri filePath;
    private StorageReference storageReference;
    private DatabaseReference mDatabase;
    private FirebaseUser firebaseUser;
    private  FirebaseStorage storage;
    private Bitmap bit;
    private final int RESULT_CROPadh = 400;
    private String vehno;
    private FirebaseFirestore firestore;
    private static final int PICK_IMAGE_REQUEST = 234;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_profile_image);

        imageView = (ImageView) findViewById(R.id.profileimage);
        but = (Button) findViewById(R.id.but);
        next = (Button)findViewById(R.id.next);

        firestore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference(Constants.DATABASE_PATH_UPLOADS);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        Log.v("veh",vehno+"saveprofileimage");
        next.setOnClickListener(this);
        but.setOnClickListener(this);
        next.setVisibility(View.GONE);

    }
    private void performCropahdar(String picUri) {
        try {
            //Start Crop Activity
            // Toast.makeText(this, "perform ahdar crop", Toast.LENGTH_SHORT).show();

            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            // indicate image type and Uri
            File f = new File(picUri);
            Uri contentUri = Uri.fromFile(f);

            cropIntent.setDataAndType(contentUri, "image/*");
            // set crop properties
            cropIntent.putExtra("crop", "true");
            // indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            // indicate output X and Y
            cropIntent.putExtra("outputX", 280);
            cropIntent.putExtra("outputY", 280);

            // retrieve data on return
            cropIntent.putExtra("return-data", true);
            // start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, RESULT_CROPadh);
        }
        // respond to users whose devices do not support the crop action
        catch (ActivityNotFoundException anfe) {
            // display an error message
            String errorMessage = "your device doesn't support the crop action!";
            //Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
           // toast.show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            Log.v("filepath",filePath.toString());


            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath1 = cursor.getString(columnIndex);
            cursor.close();
            performCropahdar(picturePath1);

        }
        if (requestCode == RESULT_CROPadh ) {
            if(resultCode == Activity.RESULT_OK){
                //Toast.makeText(this, "request result", Toast.LENGTH_SHORT).show();
                Bundle extras = data.getExtras();
                Bitmap selectedBitmap = extras.getParcelable("data");
                // Set The Bitmap Data To ImageView
                bit = getCroppedBitmap(selectedBitmap);
                imageView.setImageBitmap(bit);
                imageView.setVisibility(View.GONE);
                //imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                //button1.setText("Adhar card Selected");

                but.setVisibility(View.GONE);
                // Toast.makeText(this, "uploadfile call hua", Toast.LENGTH_SHORT).show();
                // Toast.makeText(this, ""+file1, Toast.LENGTH_SHORT).show();
                uploadFile();
                // Toast.makeText(this, "upload file call o gaya", Toast.LENGTH_SHORT).show();;



            }
        }
    }
    @Override
    public void onClick(View view) {
        if (view == but) {
            Log.v("tag",firebaseUser.getUid().toString().trim());
            firestore.collection(firebaseUser.getUid()+"vehno").get() .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot document : task.getResult()) {
                            vehno = document.get("vehno").toString();
                            Log.d("veh", "=>" +document.get("vehno").toString());

                        }

                    } else {
                        Log.e("data", "error occured user main" + task.getException());
                    }

                }
            });
            showFileChooser();

        } else if (view == next) {
            Intent i = new Intent(SetProfileImage.this, EmailVerification.class);
            i.putExtra("vehno",vehno);
            Log.v("veh",vehno +"saveprofileimage se gaya");
            startActivity(i);
        }
    }
    public Bitmap getCroppedBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        // canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
                bitmap.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        //Bitmap _bmp = Bitmap.createScaledBitmap(output, 60, 60, false);
        //return _bmp;
        return output;
    }



    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);

    }
    public String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile() {

        if (filePath != null) {
           // Toast.makeText(this, ""+filePath, Toast.LENGTH_SHORT).show();
            //displaying progress dialog while image is uploading
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading");
            progressDialog.show();
            storageReference = storage.getReference();
            firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

            String veee = getIntent().getStringExtra(firebaseUser.getUid().toString().trim());
            //getting the storage reference
                StorageReference sRef = storageReference.child(Constants.STORAGE_PATH_UPLOADS + firebaseUser.getUid().toString().trim() + "." + getFileExtension(filePath));
            String stoname = Constants.STORAGE_PATH_UPLOADS +firebaseUser.getUid();
            //adding the file to reference
            sRef.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //dismissing the progress dialog
                            progressDialog.dismiss();

                            //displaying success toast
                            Toast.makeText(getApplicationContext(), "File Uploaded ", Toast.LENGTH_LONG).show();
                            but.setVisibility(View.GONE);
                            next.setVisibility(View.VISIBLE);
                            imageView.setVisibility(View.VISIBLE);



                            //creating the upload object to store uploaded image details
                            @SuppressWarnings("VisibleForTests")Upload upload = new Upload(firebaseUser.getUid(), taskSnapshot.getDownloadUrl().toString());

                            //adding an upload to firebase database
                            String uploadId = mDatabase.push().getKey();
                            Log.v("tag",uploadId);
                            mDatabase.child(uploadId).setValue(upload);

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            progressDialog.dismiss();
                            but.setVisibility(View.VISIBLE);
                           // Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            //displaying the upload progress
                            @SuppressWarnings("VisibleForTests")double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                            but.setVisibility(View.VISIBLE);
                        }
                    });
        } else {
            //display an error if no file is selected
        }
    }

    private void uploadFilee() throws IOException {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading");
        progressDialog.show();
//        imageView.setDrawingCacheEnabled(true);
//        imageView.buildDrawingCache();
        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);

        bit = getCroppedBitmap(bitmap);

        //  Bitmap bitmap = imageView.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bit.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        storageReference = storage.getReference();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        //getting the storage reference
        StorageReference sRef = storageReference.child(Constants.STORAGE_PATH_UPLOADS + firebaseUser.getUid() + "." + getFileExtension(filePath));
        String stoname = Constants.STORAGE_PATH_UPLOADS +firebaseUser.getUid();
        UploadTask uploadTask = sRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                progressDialog.dismiss();

                //displaying success toast
                Toast.makeText(getApplicationContext(), "File Uploaded ", Toast.LENGTH_LONG).show();
                but.setVisibility(View.GONE);
                next.setVisibility(View.VISIBLE);




                //creating the upload object to store uploaded image details
                @SuppressWarnings("VisibleForTests")Upload upload = new Upload(firebaseUser.getUid(), taskSnapshot.getDownloadUrl().toString());

                //adding an upload to firebase database
                String uploadId = mDatabase.push().getKey();
                Log.v("tag",uploadId);
                mDatabase.child(uploadId).setValue(upload);



                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                progressDialog.dismiss();
              //  Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
            }
        })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        //displaying the upload progress
                        @SuppressWarnings("VisibleForTests")double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                        progressDialog.setMessage("Uploading - " + ((int) progress) + "%...");
                    }
                });

    }
    }





class Constants {

    public static final String STORAGE_PATH_UPLOADS = "uploads/";
    public static final String DATABASE_PATH_UPLOADS = "uploads";
}






































