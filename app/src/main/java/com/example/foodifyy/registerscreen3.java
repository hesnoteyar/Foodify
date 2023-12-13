package com.example.foodifyy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class registerscreen3 extends AppCompatActivity {

    FirebaseAuth auth;
    Button create;
    EditText postal, region, barangay, house, phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registerscreen3);

        auth = FirebaseAuth.getInstance();
        create = findViewById(R.id.create_acc);
        postal = findViewById(R.id.postal_code);
        region = findViewById(R.id.region);
        barangay = findViewById(R.id.barangay);
        house = findViewById(R.id.houseno);
        phone = findViewById(R.id.phone);


        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String postalcode = postal.getText().toString();
                String regionprovince = region.getText().toString();
                String barangaycity = barangay.getText().toString();
                String housenumber = house.getText().toString();
                String phoneno = phone.getText().toString();


                if (TextUtils.isEmpty(postalcode) || TextUtils.isEmpty(regionprovince) || TextUtils.isEmpty(barangaycity) || TextUtils.isEmpty(housenumber) || TextUtils.isEmpty(phoneno)){
                    Toast.makeText(getApplicationContext(), "Please Insert all values", Toast.LENGTH_SHORT).show();
                } else {
                    saveLocationToFirebase(postalcode, regionprovince, barangaycity, housenumber, phoneno);
                    Toast.makeText(registerscreen3.this, "Info saved", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), registerscreen4.class));
                }


            }

            private void saveLocationToFirebase(String postalcode, String regionprovince, String barangaycity, String housenumber, String phoneno) {
                  FirebaseUser firebaseUser = auth.getCurrentUser();
                  String userID = firebaseUser.getUid();

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");
                databaseReference.child(userID).child("postalcode").setValue(postalcode);
                databaseReference.child(userID).child("region,province").setValue(regionprovince);
                databaseReference.child(userID).child("barangay,city").setValue(barangaycity);
                databaseReference.child(userID).child("housenumber").setValue(housenumber);
                databaseReference.child(userID).child("phonenumber").setValue(phoneno);
            }
        });
    }
}