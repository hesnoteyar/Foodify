package com.example.foodifyy;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.example.foodifyy.Transaction;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class PayMethod extends AppCompatActivity {
    Handler h = new Handler();
    Button confirmButton;
    FirebaseAuth auth;
    DatabaseReference userPointsRef;
    ImageView back;
    RadioGroup radioGroup;
    private String paymentType;
    RadioButton Cash, Points;
    private ArrayList<CartItem> cartItems = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_method);




        cartItems = new ArrayList<>(); // Initialize cartItems

        Cash = findViewById(R.id.Cashrdbtn);
        Points = findViewById(R.id.Pointsrdbtn);
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
                 radioGroup = findViewById(R.id.rdgrppayment);
                int selectedRadioButtonId = radioGroup.getCheckedRadioButtonId();


                if (selectedRadioButtonId == R.id.Cashrdbtn) {

                    int totalAmount = getIntent().getIntExtra("totalAmount", 0);
                    paymentType = "Paid through Cash";
                    Intent intent = new Intent(PayMethod.this, Receipt.class);
                    intent.putExtra("totalAmount", getIntent().getIntExtra("totalAmount", 0));
                    intent.putExtra("cartItems", getIntent().getSerializableExtra("cartItems"));
                    startActivity(intent);
                    handleNoPointsPayment();

                } else if (selectedRadioButtonId == R.id.Pointsrdbtn) {

                    paymentType = "Paid through Points";
                    handlePointsPayment();

                }
            }
        });

    }

    private void handleNoPointsPayment() {
        int totalAmount = getIntent().getIntExtra("totalAmount", 0);
        ArrayList<CartItem> items = cartItems; // Implement this method to retrieve your cart items

        Toast.makeText(this, "Please wait for your food", Toast.LENGTH_SHORT).show();
        saveTransactionToHistory(paymentType, totalAmount);
        clearCartInFirebase();
    }

    private void handlePointsPayment() {
        int totalAmount = getIntent().getIntExtra("totalAmount", 0);
        Log.d("PayMethod", "Total Amount: " + totalAmount);


        userPointsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int currentPoints = snapshot.getValue(Integer.class);

                if (currentPoints >= totalAmount) {
                    int newPoints = currentPoints - totalAmount;
                    userPointsRef.setValue(newPoints);

                    saveTransactionToHistory(paymentType, totalAmount);
                    clearCartInFirebase();


                    Toast.makeText(PayMethod.this, "Points deducted successfully", Toast.LENGTH_SHORT).show();
                    h.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent i = new Intent(PayMethod.this, Receipt.class);
                            i.putExtra("totalAmount", getIntent().getIntExtra("totalAmount", 0));
                            i.putExtra("cartItems", getIntent().getSerializableExtra("cartItems"));
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

    private void clearCartInFirebase() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        DatabaseReference cartReference = FirebaseDatabase.getInstance().getReference("cart").child(auth.getCurrentUser().getUid());

        cartReference.removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                if (error == null) {
                    Log.d("ClearCart", "Cart cleared successfully");
                } else {
                    Log.e("ClearCart", "Error clearing cart: " + error.getMessage());
                }
            }
        });
    }

    private void saveTransactionToHistory(String paymentType, int amount) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        DatabaseReference historyReference = FirebaseDatabase.getInstance().getReference("transaction_history").child(auth.getCurrentUser().getUid());

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = dateFormat.format(System.currentTimeMillis());

        Transaction transaction = new Transaction(auth.getCurrentUser().getUid(), paymentType, amount);
        transaction.setTransactionDate(new Date());


        historyReference.push().setValue(transaction, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                if (error == null) {
                    Log.d("SaveTransaction", "Transaction saved to history successfully");
                } else {
                    Log.e("SaveTransaction", "Error saving transaction: " + error.getMessage());
                }
            }
        });
    }
}