package com.krishbarcode.police_app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login_page_1 extends AppCompatActivity {

    EditText ed1, ed2;
    String email, pass;
    ProgressDialog probar;
    private FirebaseAuth firebaseauth;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        firebaseauth = FirebaseAuth.getInstance();
        firebaseauth = FirebaseAuth.getInstance();
        if (firebaseauth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), Police_main.class));
        }

        probar = new ProgressDialog(this);

        ed1 = (EditText) findViewById(R.id.email1);
        ed2 = (EditText) findViewById(R.id.pass1);
    }

    public void signin(View view) {


        Log.v("user", "getting logged in with id and pass");
        email = ed1.getText().toString().trim();
        pass = ed2.getText().toString().trim();

//        if (verification()) {
//
//
//            if(email.equals("adityajain") && pass.equals("1111111"))
//            {
//
//                Intent i = new Intent(getApplicationContext(), Police_main.class);
//                i.putExtra("EMAIL", email);
//                startActivity(i);
//                Toast.makeText(Login_page_1.this, "Getting you Logged in....", Toast.LENGTH_SHORT).show();
//
//
//            }
//            else
//            {
//                Toast.makeText(Login_page_1.this, "Invalid Username or Password", Toast.LENGTH_SHORT).show();
//            }

        if (verification()) {

            probar.setMessage("Getting you logged in...");
            probar.show();
            firebaseauth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    probar.dismiss();
                    if (task.isSuccessful()) {
                        finish();
                        firebaseUser = firebaseauth.getCurrentUser();

                        Log.v("user", "sign in successfully");

                        Intent i = new Intent(getApplicationContext(), Police_main.class);
                        i.putExtra("EMAIL", email);
                        startActivity(i);
                        Toast.makeText(Login_page_1.this, "Getting you Logged in....", Toast.LENGTH_SHORT).show();
                    } else {

                        Toast.makeText(Login_page_1.this, "Not able to login ..Retry", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
    public boolean verification() {
        email = ed1.getText().toString().trim();
        pass = ed2.getText().toString().trim();

        Log.v("user", "verification of id and pass");
        if (TextUtils.isEmpty(email)) {
            ed1.setError("Enter email");
            return false;
        }

        if (TextUtils.isEmpty(pass)) {
            ed2.setError("Enter pass");
            return false;
        }
        if (pass.length() < 6) {
            ed2.setError("Password should be at least 6 characters");
            return false;

        }
        return true;

    }
}
