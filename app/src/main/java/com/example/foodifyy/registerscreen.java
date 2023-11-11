package com.example.foodifyy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.foodifyy.databinding.ActivityMainscreenBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class registerscreen extends AppCompatActivity implements View.OnClickListener{

    FirebaseAuth auth;
    FirebaseDatabase db;
    Button next;
    EditText fname, lname, uname, email, password, retypepass;
    Handler h = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registerscreen);

        auth = FirebaseAuth.getInstance();
        next = findViewById(R.id.btn_next);
        uname = findViewById(R.id.username);
        email = findViewById(R.id.email_add);
        password = findViewById(R.id.password);
        retypepass = findViewById(R.id.password2);
        next.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        if (v == next) {
            String un = uname.getText().toString();
            String emailaddress = email.getText().toString();
            String pw = password.getText().toString();
            String pw2 = retypepass.getText().toString();



            if (TextUtils.isEmpty(un) || TextUtils.isEmpty(emailaddress) || TextUtils.isEmpty(pw) || TextUtils.isEmpty(pw2)) {
                Toast.makeText(getApplicationContext(), "Please insert all values", Toast.LENGTH_SHORT).show();
            } else {
                auth.createUserWithEmailAndPassword(emailaddress, pw).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            saveUserDataToFirebase(un, emailaddress);
                            Toast.makeText(registerscreen.this, "Info Recorded", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(registerscreen.this, registerscreen2.class));
                        } else {
                            Toast.makeText(registerscreen.this, "Registered Failed" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    private void saveUserDataToFirebase(String un, String emailaddress) {
                        FirebaseUser firebaseUser = auth.getCurrentUser();
                        String userID = firebaseUser.getUid();

                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");
                        databaseReference.child(userID).child("username").setValue(un);
                        databaseReference.child(userID).child("emailaddress").setValue(emailaddress);
                    }
                });
            }
        }
    }

}