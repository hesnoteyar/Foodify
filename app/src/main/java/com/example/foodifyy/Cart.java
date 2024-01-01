package com.example.foodifyy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

public class Cart extends AppCompatActivity {

    ImageView back;
    Handler h = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        back = findViewById(R.id.backbutton);

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