package com.example.foodifyy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;


public class Cart extends AppCompatActivity {

    ImageView back, ImageEmpty;
    TextView Imgtxt1, Imgtxt2;
    Button Checkoutbtn;
    Handler h = new Handler();

    private ArrayList<CartItem> cartItems = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        back = findViewById(R.id.backbutton);
        ImageEmpty = findViewById(R.id.emptyimage);
        Imgtxt1 = findViewById(R.id.imagetext1);
        Imgtxt2 = findViewById(R.id.imagetext2);
        Checkoutbtn = findViewById(R.id.checkout);

        ListView cartListView = findViewById(R.id.cartListView);

        CartItemAdapter adapter = new CartItemAdapter(this, cartItems);
        cartListView.setAdapter(adapter);

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
}