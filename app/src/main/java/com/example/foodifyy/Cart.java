package com.example.foodifyy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class Cart extends AppCompatActivity {

    FirebaseAuth auth;
    ImageView back, ImageEmpty;
    ListView cartListView;
    TextView Imgtxt1, Imgtxt2;
    Button Checkoutbtn;
    Handler h = new Handler();

    private ArrayList<CartItem> cartItems = new ArrayList<>();
    private CartAdapter cartAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        back = findViewById(R.id.backbutton);
        ImageEmpty = findViewById(R.id.emptyimage);
        Imgtxt1 = findViewById(R.id.imagetext1);
        Imgtxt2 = findViewById(R.id.imagetext2);
        cartListView = findViewById(R.id.cartListView);
        Checkoutbtn = findViewById(R.id.checkout);


        cartAdapter = new CartAdapter(this, cartItems);
        cartListView.setAdapter(cartAdapter);
        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null) {
            String userId = auth.getCurrentUser().getUid();
            DatabaseReference cartReference = FirebaseDatabase.getInstance().getReference("cart").child(userId);

            cartReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    cartItems.clear();

                    for (DataSnapshot cartItemSnapshot : snapshot.getChildren()) {
                        CartItem cartItem = cartItemSnapshot.getValue(CartItem.class);
                        cartItems.add(cartItem);
                    }

                    cartAdapter.notifyDataSetChanged();
                    updateVisibility();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } else {
            
        }



        back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                h.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent i = new Intent(Cart.this, mainscreen.class);
                        startActivity(i);
                    }
                }, 0);
            }
        });


    }

    private void updateVisibility() {
        if (cartItems.isEmpty()) {
            ImageEmpty.setVisibility(View.VISIBLE);
            Imgtxt1.setVisibility(View.VISIBLE);
            Imgtxt2.setVisibility(View.VISIBLE);
            cartListView.setVisibility(View.GONE);
            Checkoutbtn.setVisibility(View.GONE);
        } else {
            ImageEmpty.setVisibility(View.GONE);
            Imgtxt1.setVisibility(View.GONE);
            Imgtxt2.setVisibility(View.GONE);
            cartListView.setVisibility(View.VISIBLE);
            Checkoutbtn.setVisibility(View.VISIBLE);
        }
    }
}