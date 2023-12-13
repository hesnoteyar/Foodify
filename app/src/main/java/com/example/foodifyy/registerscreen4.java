package com.example.foodifyy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class registerscreen4 extends AppCompatActivity {

    FirebaseAuth auth;
    Button create;

    EditText age, weight, height;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registerscreen4);

        auth = FirebaseAuth.getInstance();
        create = findViewById(R.id.create);
        age = findViewById(R.id.age);
        weight = findViewById(R.id.weight);
        height = findViewById(R.id.height);

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String agedata = age.getText().toString();
                String weightdata = weight.getText().toString();
                String heightdata = height.getText().toString();

                if (TextUtils.isEmpty(agedata) || TextUtils.isEmpty(weightdata) || TextUtils.isEmpty(heightdata)){
                    Toast.makeText(registerscreen4.this, "Please insert all values", Toast.LENGTH_SHORT).show();
                } else {
                    saveInfoToFirebase(agedata, weightdata, heightdata);
                    Toast.makeText(registerscreen4.this, "Info Saved", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), loginscreen.class));
                }
            }

            private void saveInfoToFirebase(String agedata, String weightdata, String heightdata) {
                FirebaseUser firebaseUser = auth.getCurrentUser();
                String userID = firebaseUser.getUid();

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");
                databaseReference.child(userID).child("age").setValue(agedata);
                databaseReference.child(userID).child("weight").setValue(weightdata);
                databaseReference.child(userID).child("height").setValue(heightdata);

                FirebaseAuth.getInstance().signOut();

            }


        });
    }
}