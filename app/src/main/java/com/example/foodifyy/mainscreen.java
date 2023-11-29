package com.example.foodifyy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class mainscreen extends AppCompatActivity {
    ImageView Logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainscreen);

        final DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);

        findViewById(R.id.menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        NavigationView navigationView = findViewById(R.id.nav_view);

        NavController navController = Navigation.findNavController(this, R.id.navHostFragment);
        NavigationUI.setupWithNavController(navigationView, navController);

        Logout = findViewById(R.id.logout);

        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLogOutConfirmationDialog();
            }
        });

    }


    private void showLogOutConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to logout?").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                FirebaseAuth.getInstance().signOut();
                redirectToLoginPage();
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void redirectToLoginPage() {
        Intent intent = new Intent(this, loginscreen.class);
        startActivity(intent);
        finish();
    }


}