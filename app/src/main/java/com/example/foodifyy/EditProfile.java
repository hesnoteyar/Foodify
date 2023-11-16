package com.example.foodifyy;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditProfile extends AppCompatActivity {

    FirebaseAuth auth;
    DatabaseReference databaseReference;
    EditText emailEdit, unameEdit, contactEdit, fnameEdit, mnameEdit, lnameEdit, houseEdit, barangayEdit, regionEdit;
    TextView save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);


        auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("users").child(currentUser.getUid());

        emailEdit = findViewById(R.id.edit_email);
        unameEdit = findViewById(R.id.edit_uname);
        contactEdit = findViewById(R.id.edit_number);
        fnameEdit = findViewById(R.id.edit_fname);
        mnameEdit = findViewById(R.id.edit_mname);
        lnameEdit = findViewById(R.id.edit_lname);
        houseEdit = findViewById(R.id.edit_house);
        barangayEdit = findViewById(R.id.edit_barangay);
        regionEdit = findViewById(R.id.edit_region);

        save = findViewById(R.id.save_profile);

        retrieveProfileInformation();
    }

    private void retrieveProfileInformation() {
        databaseReference.get().addOnSuccessListener(documentSnapshot -> {
            if  (documentSnapshot.exists()) {
                String email = documentSnapshot.child("emailaddress").getValue(String.class);
                String firstname = documentSnapshot.child("firstname").getValue(String.class);
                String middlename = documentSnapshot.child("middlename").getValue(String.class);
                String lastname = documentSnapshot.child("lastname").getValue(String.class);
                String username = documentSnapshot.child("username").getValue(String.class);
                String phone = documentSnapshot.child("phonenumber").getValue(String.class);
                String house_no = documentSnapshot.child("housenumber").getValue(String.class);
                String barangay_city = documentSnapshot.child("barangay,city").getValue(String.class);
                String region_province = documentSnapshot.child("region,province").getValue(String.class);

                emailEdit.setText(email);
                fnameEdit.setText(firstname);
                lnameEdit.setText(lastname);
                unameEdit.setText(username);
                mnameEdit.setText(middlename);
                contactEdit.setText(phone);
                houseEdit.setText(house_no);
                barangayEdit.setText(barangay_city);
                regionEdit.setText(region_province);
            }
        });
    }
}