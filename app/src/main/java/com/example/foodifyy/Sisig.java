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
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Sisig extends AppCompatActivity {

    Handler h = new Handler();
    ImageView backbtn;
    TextView Menudisplay, Pricedisplay, Quantity;
    Button btnAdd, btnMinus, addtocart;
    DatabaseReference databaseReference;
    DatabaseReference cartReference;
    FirebaseAuth auth;

    private int quantity = 1;
    private double itemPrice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sisig);

        backbtn = findViewById(R.id.backbutton);
        Menudisplay = findViewById(R.id.displayMenu);
        Pricedisplay = findViewById(R.id.display_price);
        addtocart = findViewById(R.id.addtocart);
        Quantity = findViewById(R.id.tvQuantity);
        btnAdd = findViewById(R.id.btnAdd);
        btnMinus = findViewById(R.id.btnMinus);
        databaseReference = FirebaseDatabase.getInstance().getReference("food").child("FOOD003");
        auth = FirebaseAuth.getInstance();

        retrieveMenuInfo();
        String userId = auth.getCurrentUser().getUid();
        cartReference = FirebaseDatabase.getInstance().getReference("cart").child(userId);

        addtocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                addToCart();
            }
        });

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                h.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent i = new Intent(Sisig.this, mainscreen.class);
                        startActivity(i);
                    }
                }, 0);
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incrementQuantity();
            }
        });

        btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decrementQuantity();

            }
        });

        addValueEventListener();

    }

    private void addToCart() {
        if (auth.getCurrentUser() != null) {
            int currentQuantity = quantity;
            double currentAmount = currentQuantity * itemPrice;
            String foodName = Menudisplay.getText().toString();

            CartItem cartItem = new CartItem(foodName, currentQuantity, currentAmount);

            String cartItemId = cartReference.push().getKey();
            cartReference.child(cartItemId).setValue(cartItem);

            Toast.makeText(Sisig.this, "Added to Cart", Toast.LENGTH_SHORT).show();
        }
    }

    private void addValueEventListener() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Long menuPrice = snapshot.child("foodPrice").getValue(Long.class);

                    if (menuPrice != null) {
                        itemPrice = menuPrice.doubleValue();
                        updateDisplayPrice();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void decrementQuantity() {
        if (quantity > 1) {
            quantity--;

            // Update the TextView with the new quantity
            Quantity.setText(String.valueOf(quantity));

            // Update the display price
            updateDisplayPrice();
        }
    }

    private void incrementQuantity() {
        // Increment the quantity
        quantity++;

        // Update the TextView with the new quantity
        Quantity.setText(String.valueOf(quantity));

        // Update the display price
        updateDisplayPrice();
    }

    private void retrieveMenuInfo() {
        databaseReference.get().addOnSuccessListener(dataSnapshot -> {
            if (dataSnapshot.exists()) {
                String MenuName = dataSnapshot.child("foodName").getValue(String.class);
                Long MenuPrice = dataSnapshot.child("foodPrice").getValue(Long.class);

                if (MenuPrice != null) {
                    itemPrice = MenuPrice.doubleValue();
                    Pricedisplay.setText(String.format("₱ %.2f", itemPrice));
                    updateDisplayPrice();
                }
                Menudisplay.setText(MenuName);

            }
        });
    }

    private void updateDisplayPrice() {
        // Calculate the total amount based on quantity and item price
        double totalAmount = quantity * itemPrice;

        // Display the total amount in the TextView
        Pricedisplay.setText(String.format("₱ %.2f", totalAmount));


    }
}