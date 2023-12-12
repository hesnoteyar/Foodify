package com.example.foodifyy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;

public class loginscreen extends AppCompatActivity {

    FirebaseAuth auth;

    Button login;
    EditText emailfield, passwordfield;
    TextView newacc, forgotpassword;
    Handler h = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginscreen);
        auth = FirebaseAuth.getInstance();
        login = findViewById(R.id.login_btn);
        newacc = findViewById(R.id.newacc_btn);
        emailfield = findViewById(R.id.email_field);
        passwordfield = findViewById(R.id.password_field);
        forgotpassword = findViewById(R.id.forgotpass);
        
        checkLoginStatus();

        forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                h.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent i = new Intent(loginscreen.this, ForgotPassword.class);
                        startActivity(i);
                    }
                }, 0);
            }
        });
        
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailfield.getText().toString();
                String password = passwordfield.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    emailfield.setError("Email is Required");
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    passwordfield.setError("Password is Required");
                    return;
                }

                if (password.length() < 7) {
                    passwordfield.setError("Password must be 8 characters long");
                    return;
                }

                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            FirebaseUser user = auth.getCurrentUser();

                            if (user != null && user.isEmailVerified()) {
                                Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), mainscreen.class));
                            } else {
                                Toast.makeText(getApplicationContext(), "Please verify your email first!", Toast.LENGTH_SHORT).show();
                                auth.signOut();
                            }
                        } else {
                            Toast.makeText(loginscreen.this, "Login Failed" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        newacc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                h.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent i = new Intent(loginscreen.this, registerscreen.class);
                        startActivity(i);
                    }
                }, 0);
            }
        });




    }

    private void checkLoginStatus() {
        FirebaseUser user = auth.getCurrentUser();

        if  (user != null) {
            if (user.getMetadata().getCreationTimestamp() == user.getMetadata().getLastSignInTimestamp()) {
                Toast.makeText(loginscreen.this, "New user, going to Dashboard", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent (loginscreen.this, mainscreen.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Welcome", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent (loginscreen.this, mainscreen.class);
                startActivity(intent);
            }
            finish();
        }
    }
}