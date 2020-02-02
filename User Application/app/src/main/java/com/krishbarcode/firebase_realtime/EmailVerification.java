package com.krishbarcode.firebase_realtime;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class EmailVerification extends AppCompatActivity implements View.OnClickListener {

    private Button verify, done;
    private TextView status;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private ImageView refesh;
    private String vehno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_verification);
        verify = (Button) findViewById(R.id.verifybutton);
        done = (Button) findViewById(R.id.done);
        status = (TextView) findViewById(R.id.verificationstatus);
        vehno = getIntent().getStringExtra("vehno");
        Log.v("veh", vehno + "emailverificatioin me aya");

        verify.setOnClickListener(this);
        done.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
    }


    @Override
    public void onClick(View view) {
        if (view == verify) {
            verifyemail();
        } else if (view == done) {
            Intent i = new Intent(EmailVerification.this,UserMainActivity.class);
            i.putExtra("vehno", vehno);
            Log.v("veh", vehno + "saveprofileimage se gaya");
            startActivity(i);
        }
    }

    public void refresh(View v) {

        Log.v("email", String.valueOf(user.isEmailVerified()));
        if (user.isEmailVerified()) {
            status.setText("Email is Successfully Verified ");
            Toast.makeText(this, "Now Click on \"Done\" and Login to the acoount ", Toast.LENGTH_SHORT).show();

            done.setVisibility(v.VISIBLE);
        } else {
            Toast.makeText(this, "Email is not Verified", Toast.LENGTH_SHORT).show();
        }
    }

    private void verifyemail() {
        findViewById(R.id.verifybutton).setEnabled(false);
        final FirebaseUser user = mAuth.getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        findViewById(R.id.verifybutton).setEnabled(true);

                        if (task.isSuccessful()) {
                            Toast.makeText(EmailVerification.this,
                                    "Verification email sent to " + user.getEmail(),
                                    Toast.LENGTH_SHORT).show();
                            status.setText("Go to the Mail and verify the account and come back and continue");
                        } else {
                            Log.e("tag", "sendEmailVerification", task.getException());
                            Toast.makeText(EmailVerification.this,
                                    "Failed to send verification email.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
