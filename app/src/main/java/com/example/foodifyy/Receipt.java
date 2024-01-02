package com.example.foodifyy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class Receipt extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        int totalAmount = getIntent().getIntExtra("totalAmount", 0);
        Log.d("PayMethod", "Total Amount: " + totalAmount);
        TextView totalAmountTextView = findViewById(R.id.totalamount);
        totalAmountTextView.setText(String.valueOf(totalAmount));



        // Check if the user is authenticated
        if (user != null) {
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(user.getUid());

            // Retrieve username from the database
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String username = snapshot.child("username").getValue(String.class);

                        // Set the username in the TextView
                        TextView greeting2 = findViewById(R.id.greeting2);
                        greeting2.setText("Hello " + username + ",");

                        // Continue with the rest of the logic
                        int totalAmount = getIntent().getIntExtra("totalAmount", 0);
                        ArrayList<CartItem> cartItems = (ArrayList<CartItem>) getIntent().getSerializableExtra("cartItems");

                        // Set the total amount in the TextView
                        TextView totalAmountTextView = findViewById(R.id.totalamount);
                        totalAmountTextView.setText(String.valueOf(totalAmount));

                        // Set up the ListView with the cart items
                        ListView receiptListView = findViewById(R.id.ListviewItem);
                        ReceiptAdapter receiptAdapter = new ReceiptAdapter(Receipt.this, cartItems);
                        receiptListView.setAdapter(receiptAdapter);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle database error
                }
            });
        } else {
            // User is not authenticated, handle accordingly
            // You might want to redirect the user to the login screen or take appropriate action
        }
    }
}
