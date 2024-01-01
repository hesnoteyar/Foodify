package com.example.foodifyy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PayMethod extends AppCompatActivity {
    Handler h = new Handler();
    Button confirmButton;
    FirebaseAuth auth;
    DatabaseReference userPointsRef;
    ImageView back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_method);

        confirmButton = findViewById(R.id.confirm);
        back = findViewById(R.id.backbutton);
        auth = FirebaseAuth.getInstance();
        userPointsRef = FirebaseDatabase.getInstance().getReference("users").child(auth.getCurrentUser().getUid()).child("points");

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                h.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent i = new Intent(PayMethod.this, Cart.class);
                        startActivity(i);
                    }
                }, 0);
            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RadioGroup radioGroup = findViewById(R.id.rdgrppayment);
                int selectedRadioButtonId = radioGroup.getCheckedRadioButtonId();

                if (selectedRadioButtonId == R.id.Cashrdbtn) {
                    Toast.makeText(PayMethod.this, "Will be payed upon receive", Toast.LENGTH_SHORT).show();
                    Log.d("PayMethod", "Before starting Receipt activity");
                    h.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            Intent i = new Intent(PayMethod.this, Receipt.class);
                            startActivity(i);
                            Log.d("PayMethod", "After starting Receipt activity");

                        }
                    }, 0);
                } else if (selectedRadioButtonId == R.id.Pointsrdbtn) {
                    handlePointsPayment();
                }
            }
        });

    }

    private void handlePointsPayment() {
        int totalAmount = getIntent().getIntExtra("totalAmount", 0);

        userPointsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                int currentPoints = snapshot.getValue(Integer.class);

                if (currentPoints >= totalAmount) {
                    int newPoints = currentPoints - totalAmount;
                    userPointsRef.setValue(newPoints);

                    Toast.makeText(PayMethod.this, "Points deducted successfully", Toast.LENGTH_SHORT).show();
                    h.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent i = new Intent(PayMethod.this, Receipt.class);
                            startActivity(i);
                        }
                    }, 0);
                } else {

                    Toast.makeText(PayMethod.this, "Not enough points", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(PayMethod.this, "Database error", Toast.LENGTH_SHORT).show();

            }
        });
    }
}