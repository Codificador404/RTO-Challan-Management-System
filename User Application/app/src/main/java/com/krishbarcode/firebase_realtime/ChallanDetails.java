package com.krishbarcode.firebase_realtime;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChallanDetails extends AppCompatActivity implements ChallanAdapter.ItemClickListener {
    FirebaseFirestore firestore;
    private StorageReference storageReference;
    private FirebaseUser firebaseUser;
    private FirebaseStorage storage;
    String vehstring;
    private DatabaseReference mFirebaseParkingDatabase;
    private FirebaseDatabase mFirebaseInstance;

    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;
    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    private static ArrayList<Challan> data;
    static View.OnClickListener myOnClickListener;
    private static ArrayList<Integer> removedItems;
    private CardView cardView;
    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challan_detail);

        context =  getApplicationContext();
        cardView = findViewById(R.id.card_view);

        vehstring = getIntent().getStringExtra("vehno");
        mFirebaseParkingDatabase = FirebaseDatabase.getInstance().getReference("Challan");

        mFirebaseParkingDatabase.child(vehstring).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                data = new ArrayList<Challan>();
                Map<String, Challan> challans = new HashMap<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Log.d("card",vehstring);
                    Challan challan = snapshot.getValue(Challan.class);
                    data.add(new Challan(challan.getTime(), challan.getDate(), challan.getLoc(), challan.getVehno(), challan.isIs_paid()));


                    Log.d("card", data.toString());
                    adapter = new ChallanAdapter(data,ChallanDetails.this);
                    recyclerView.setAdapter(adapter);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }});


        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


    }

    @Override
    public void onItemClicked(final ChallanAdapter.SingleChallan singleChallan) {
      //  Toast.makeText(this, "card view clicked", Toast.LENGTH_SHORT).show();
        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.paymetdialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setView(view)
                .setTitle("Proceed to Pay....")
                .setCancelable(false)
                .setPositiveButton("Pay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        Challan challan = new Challan(singleChallan.getTime().getText().toString(), singleChallan.getDate().getText().toString(),singleChallan.getLocation().getText().toString(), vehstring, true);
                        mFirebaseParkingDatabase.child(vehstring).child(singleChallan.getTime().getText().toString()).setValue(challan);

                        //mFirebaseParkingDatabase.child(vehstring).child(singleChallan.getTime().getText().toString().trim()).child("is_paid").setValue(true);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id){
                        return;
                    }
                });

        builder.create().show();

    }
}
