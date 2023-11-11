package com.example.foodifyy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class registerscreen2 extends AppCompatActivity {

    FirebaseAuth auth;
    Button next2;
    EditText fname, lname, mname;
    Handler h = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registerscreen2);

        auth = FirebaseAuth.getInstance();
        next2 = findViewById(R.id.btn_next2);
        fname = findViewById(R.id.firstname);
        lname = findViewById(R.id.lastname);
        mname = findViewById(R.id.middlename);


        next2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fn = fname.getText().toString();
                String ln = lname.getText().toString();
                String mn = mname.getText().toString();


                if (TextUtils.isEmpty(fn) || TextUtils.isEmpty(ln) || TextUtils.isEmpty(mn)) {
                    Toast.makeText(getApplicationContext(), "Please insert all values", Toast.LENGTH_SHORT).show();
                } else {
                    saveInfoDataToFirebase(fn, ln, mn);
                    Toast.makeText(registerscreen2.this, "Info saved", Toast.LENGTH_SHORT);
                    //startActivity(new Intent(registerscreen2.this, registerscreen2.class));
                }
            }

            private void saveInfoDataToFirebase(String fn, String ln, String mn) {
                FirebaseUser firebaseUser = auth.getCurrentUser();
                String userID = firebaseUser.getUid();

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");
                databaseReference.child(userID).child("firstname").setValue(fn);
                databaseReference.child(userID).child("lastname").setValue(ln);
                databaseReference.child(userID).child("middlename").setValue(mn);
            }
        });
    }

}