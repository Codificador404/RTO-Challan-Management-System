package com.krishbarcode.firebase_realtime;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;

//import static com.google.common.io.Files.getFileExtension;

public class Documents extends AppCompatActivity {

    private final int GALLERY_ACTIVITY_CODEadhar=200;

    private final int GALLERY_ACTIVITY_CODElicence=201;
    private final int GALLERY_ACTIVITY_CODErcbook=202;
    private final int RESULT_CROPadh = 400;
    private final int RESULT_CROPli = 401;
    private final int RESULT_CROPrc = 402;
    Button button1, button2, button3;
    String vehno = "MH49AN2001";
    Bitmap bit;
    private String file1,file2,file3;
    String picturePath1,picturePath2,picturePath3;
    private Uri filePath1,filePath2,filePath3;
    private static final int PICK_adhar_REQUEST = 1;
    private static final int PICK_licence_REQUEST = 2;
    private static final int PICK_rcbook_REQUEST = 3;
    ImageView adahr,licence,rcbook;
    private StorageReference storageReference;
    private  FirebaseStorage storage;
    private FirebaseUser firebaseUser;
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_documents);
        Profile p = new Profile();
        vehno = "MH49AN2001";
        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);
        button3 = (Button) findViewById(R.id.button3);
        adahr = (ImageView)findViewById(R.id.adhar);
        licence = (ImageView)findViewById(R.id.licence);
        rcbook = (ImageView)findViewById(R.id.rcbook);
        adahr.setVisibility(View.GONE);
        licence.setVisibility(View.GONE);
        rcbook.setVisibility(View.GONE);
        storage = FirebaseStorage.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference(vehno);
    }


    public void done(View view) {
 startActivity(new Intent(this,UserMainActivity.class));


    }
    public String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile(Uri filePath, final String file) {

        if (filePath != null) {
           // Toast.makeText(this, "uploading", Toast.LENGTH_SHORT).show();
            //displaying progress dialog while image is uploading
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading");
            progressDialog.show();
            storageReference = storage.getReference();
            firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

            //getting the storage reference
            StorageReference sRef = storageReference.child(vehno+"/"+file + "." + getFileExtension(filePath));
            String stoname = Constants.STORAGE_PATH_UPLOADS +firebaseUser.getUid();
            //adding the file to reference
            sRef.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //dismissing the progress dialog
                            progressDialog.dismiss();

                            //displaying success toast
                            Toast.makeText(getApplicationContext(), file+"File Uploaded ", Toast.LENGTH_LONG).show();





                            //creating the upload object to store uploaded image details
                            @SuppressWarnings("VisibleForTests")Upload upload = new Upload(firebaseUser.getUid(), taskSnapshot.getDownloadUrl().toString());

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

    public void view(View view) {
        adahr.setVisibility(View.VISIBLE);
        licence.setVisibility(View.VISIBLE);
        rcbook.setVisibility(View.VISIBLE);

    }

    public void adhar(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), GALLERY_ACTIVITY_CODEadhar);
    }

    public void licence(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), GALLERY_ACTIVITY_CODElicence);

    }

    public void rcbook(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), GALLERY_ACTIVITY_CODErcbook);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
      ///  Toast.makeText(this, "file bahar "+data.getData(), Toast.LENGTH_SHORT).show();
        if (requestCode == GALLERY_ACTIVITY_CODEadhar && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath1 = cursor.getString(columnIndex);
            cursor.close();

            filePath1 = data.getData();
            performCropahdar(picturePath1);
        }
        else if (requestCode == GALLERY_ACTIVITY_CODElicence && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath2 = cursor.getString(columnIndex);
            cursor.close();

            filePath2 = data.getData();
            performCroplicence(picturePath2);
        }
        else if (requestCode == GALLERY_ACTIVITY_CODErcbook && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath3 = cursor.getString(columnIndex);
            cursor.close();

            filePath3 = data.getData();
            performCroprcbook(picturePath3);
        }
//        if (requestCode == GALLERY_ACTIVITY_CODEadhar) {
//            Toast.makeText(this, "file under "+data.getData(), Toast.LENGTH_SHORT).show();
//            if(resultCode == Activity.RESULT_OK){
//
//                //Toast.makeText(this, "requestcode gallary "+data.getData(), Toast.LENGTH_SHORT).show();
//                picturePath1 = data.getStringExtra("picturePath");
//                Log.v("tag","gallary se pucturepath liya"+picturePath1);
//                file1 = data.getStringExtra("filepath");
//                Log.v("tag","file ka naam gallry class seliya"+file1);
//                Toast.makeText(this, "file liya gallary wale class se"+file1, Toast.LENGTH_SHORT).show();
//                //perform Crop on the Image Selected from Gallery
//                Toast.makeText(this, ""+picturePath1, Toast.LENGTH_SHORT).show();
//                performCropahdar(picturePath1);
//            }
//        }
//        else if (requestCode == GALLERY_ACTIVITY_CODElicence) {
//            if(resultCode == Activity.RESULT_OK){
//                file2 = data.getStringExtra("filename");
//                picturePath2 = data.getStringExtra("picturePath");
//                //perform Crop on the Image Selected from Gallery
//                performCroplicence(picturePath2);
//            }
//        }
//        else if (requestCode == GALLERY_ACTIVITY_CODErcbook) {
//            if(resultCode == Activity.RESULT_OK){
//               // file3 = data.getData();
//                picturePath3 = data.getStringExtra("picturePath");
//                //perform Crop on the Image Selected from Gallery
//                performCroprcbook(picturePath3);
//            }
//        }

        if (requestCode == RESULT_CROPadh ) {
            if(resultCode == Activity.RESULT_OK){
                //Toast.makeText(this, "request result", Toast.LENGTH_SHORT).show();
                Bundle extras = data.getExtras();
                Bitmap selectedBitmap = extras.getParcelable("data");
                // Set The Bitmap Data To ImageView
                adahr.setImageBitmap(selectedBitmap);
                adahr.setScaleType(ImageView.ScaleType.FIT_XY);
                button1.setText("Adhar card Selected");

               // Toast.makeText(this, "uploadfile call hua", Toast.LENGTH_SHORT).show();
               // Toast.makeText(this, ""+file1, Toast.LENGTH_SHORT).show();
                uploadFile(filePath1,"adhar");
               // Toast.makeText(this, "upload file call o gaya", Toast.LENGTH_SHORT).show();;



            }
        }
        else if (requestCode == RESULT_CROPli ) {
            if(resultCode == Activity.RESULT_OK){
                Bundle extras = data.getExtras();
                Bitmap selectedBitmap = extras.getParcelable("data");
                // Set The Bitmap Data To ImageView
                licence.setImageBitmap(selectedBitmap);
                licence.setScaleType(ImageView.ScaleType.FIT_XY);
                button2.setText("Licence Selected");

                uploadFile(filePath2,"licence");
            }
        }
        else if (requestCode == RESULT_CROPrc ) {
            if(resultCode == Activity.RESULT_OK){
                Bundle extras = data.getExtras();
                Bitmap selectedBitmap = extras.getParcelable("data");
                // Set The Bitmap Data To ImageView
                rcbook.setImageBitmap(selectedBitmap);
                rcbook.setScaleType(ImageView.ScaleType.FIT_XY);
                button3.setText("rc book selected");

                uploadFile(filePath3,"rcbook");
            }
        }
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
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }
    private void performCroplicence(String picUri) {
        try {
            //Start Crop Activity

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
            startActivityForResult(cropIntent, RESULT_CROPli);
        }
        // respond to users whose devices do not support the crop action
        catch (ActivityNotFoundException anfe) {
            // display an error message
            String errorMessage = "your device doesn't support the crop action!";
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }  private void performCroprcbook(String picUri) {
        try {
            //Start Crop Activity

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
            startActivityForResult(cropIntent, RESULT_CROPrc);
        }
        // respond to users whose devices do not support the crop action
        catch (ActivityNotFoundException anfe) {
            // display an error message
            String errorMessage = "your device doesn't support the crop action!";
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}
