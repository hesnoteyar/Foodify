package com.example.foodifyy;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class EmailReset extends AppCompatActivity {
    Handler h = new Handler();
    ImageView back;
    TextView displayemail;
    Button backtologin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_reset);

        back = findViewById(R.id.back);
        displayemail = findViewById(R.id.emaildisplay);
        backtologin = findViewById(R.id.back2login);


        backtologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                h.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent i = new Intent(EmailReset.this, loginscreen.class);
                        startActivity(i);
                    }
                }, 0);
            }
        });


         Intent i = getIntent();
         if (i != null) {
             String userEmail = i.getStringExtra("userEmail");

             if (displayemail != null) {
                 displayemail.setText(userEmail);
             }
         }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                h.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent i = new Intent(EmailReset.this, ForgotPassword.class);
                        startActivity(i);
                    }
                }, 0);
            }
        });
    }
}