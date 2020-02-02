package com.krishbarcode.firebase_realtime;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.UserManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Locale;
import java.util.PriorityQueue;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static View.OnClickListener myOnClickListener;
    private String email, pass;

    private EditText ed1, ed2;
    private ProgressDialog probar;
    private FirebaseAuth firebaseauth;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore firestore;
    private String vehno;
    private static String takevehno;
    int flag = 0;
    private Button btn_signin, btn_signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firestore = FirebaseFirestore.getInstance();
        firestore = FirebaseFirestore.getInstance();
        btn_signin = (Button) findViewById(R.id.but_signin);
        btn_signup = (Button) findViewById(R.id.but_signup);

        //it will directly redirect to prifile page
        firebaseauth = FirebaseAuth.getInstance();
        if (firebaseauth.getCurrentUser() != null) {
            if (firebaseUser.isEmailVerified() == true) {
                firestore.collection(firebaseUser.getUid() + "vehno").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                takevehno = document.get("vehno").toString();
                                // Toast.makeText(MainActivity.this, ""+ takevehno, Toast.LENGTH_SHORT).show();
                                Log.d("try", "=>" + document.get("vehno").toString());
                                flag = 1;

                            }

                        } else {
                            Log.e("data", "error occured user main" + task.getException());
                        }

                    }
                });
                Intent i = new Intent(this, UserMainActivity.class);
                i.putExtra("takevehno", takevehno);
                startActivity(i);


            }


            Log.v("load", "intent usermain");
            Log.v("try", "start of fetching data");


        }
        probar = new ProgressDialog(this);

        ed1 = (EditText) findViewById(R.id.email1);
        ed2 = (EditText) findViewById(R.id.pass1);
        btn_signup.setOnClickListener(this);
        btn_signin.setOnClickListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.lang_setting_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.eng:
                String languageToLoad = "en"; // your language
                Locale locale = new Locale(languageToLoad);
                Locale.setDefault(locale);
                Configuration config = new Configuration();
                config.locale = locale;
                getBaseContext().getResources().updateConfiguration(config,
                        getBaseContext().getResources().getDisplayMetrics());
                restartavtivity();

                break;
            case R.id.hn:
                languageToLoad = "hn"; // your language
                locale = new Locale(languageToLoad);
                Locale.setDefault(locale);
                config = new Configuration();
                config.locale = locale;
                getBaseContext().getResources().updateConfiguration(config,
                        getBaseContext().getResources().getDisplayMetrics());
                restartavtivity();
                break;
            case R.id.bn:
                languageToLoad = "bn"; // your language
                locale = new Locale(languageToLoad);
                Locale.setDefault(locale);
                config = new Configuration();
                config.locale = locale;
                getBaseContext().getResources().updateConfiguration(config,
                        getBaseContext().getResources().getDisplayMetrics());
                restartavtivity();
                break;

            case R.id.mr:
                languageToLoad = "mr"; // your language
                locale = new Locale(languageToLoad);
                Locale.setDefault(locale);
                config = new Configuration();
                config.locale = locale;
                getBaseContext().getResources().updateConfiguration(config,
                        getBaseContext().getResources().getDisplayMetrics());
                restartavtivity();
                break;

            case R.id.pa:
                languageToLoad = "pa"; // your language
                locale = new Locale(languageToLoad);
                Locale.setDefault(locale);
                config = new Configuration();
                config.locale = locale;
                getBaseContext().getResources().updateConfiguration(config,
                        getBaseContext().getResources().getDisplayMetrics());
                restartavtivity();
                break;
            case R.id.ta:
                languageToLoad = "ta"; // your language
                locale = new Locale(languageToLoad);
                Locale.setDefault(locale);
                config = new Configuration();
                config.locale = locale;
                getBaseContext().getResources().updateConfiguration(config,
                        getBaseContext().getResources().getDisplayMetrics());
                restartavtivity();
                break;
            case R.id.te:
                languageToLoad = "te"; // your language
                locale = new Locale(languageToLoad);
                Locale.setDefault(locale);
                config = new Configuration();
                config.locale = locale;
                getBaseContext().getResources().updateConfiguration(config,
                        getBaseContext().getResources().getDisplayMetrics());
                restartavtivity();
                break;
            case R.id.ur:
                languageToLoad = "ur"; // your language
                locale = new Locale(languageToLoad);
                Locale.setDefault(locale);
                config = new Configuration();
                config.locale = locale;
                getBaseContext().getResources().updateConfiguration(config,
                        getBaseContext().getResources().getDisplayMetrics());
                restartavtivity();
                break;
            default:
                break;
        }

        this.setContentView(R.layout.activity_main);
        btn_signin = (Button) findViewById(R.id.but_signin);
        btn_signup = (Button) findViewById(R.id.but_signup);

        btn_signup.setOnClickListener(this);
        btn_signin.setOnClickListener(this);

        return super.onOptionsItemSelected(item);
    }


    public boolean verification() {

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


    @Override
    public void onClick(View v) {

        if (v == btn_signin) {
            //signin
            Log.v("user", "getting logged in with id and pass");
            email = ed1.getText().toString().trim();
            pass = ed2.getText().toString().trim();

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
                            if (firebaseUser.isEmailVerified() == false) {
                                Toast.makeText(MainActivity.this, "Email is not Verified", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(MainActivity.this, EmailVerification.class));

                            } else {

                                Log.v("user", "sign in successfully");
                                Intent i = new Intent(getApplicationContext(), UserMainActivity.class);
                                i.putExtra("EMAIL", email);
                                i.putExtra("vehno", vehno);
                                startActivity(i);
                                Toast.makeText(MainActivity.this, "Getting you Logged in....", Toast.LENGTH_SHORT).show();
                            }
                        } else {

                            Toast.makeText(MainActivity.this, "Not able to login ..Retry", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

        }
        if (v == btn_signup) {
            //signup
            Log.v("user", "creating user id");

            email = ed1.getText().toString().trim();
            pass = ed2.getText().toString().trim();

            if (verification()) {


                probar.setMessage("Registering user......");
                probar.show();

                firebaseauth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        probar.dismiss();
                        if (task.isSuccessful()) {


                            Log.v("user", "user id ban gaya");
                            Toast.makeText(MainActivity.this, "User Registered", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(getApplicationContext(), Profile.class);
                            i.putExtra("EMAIL", email);
                            startActivity(i);

                        } else {

                            Toast.makeText(MainActivity.this, "Could not registered.....  Please try again  ", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

        }


    }

    public void restartavtivity() {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }


}
