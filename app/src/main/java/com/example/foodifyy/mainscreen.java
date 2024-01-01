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
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class mainscreen extends AppCompatActivity {
    ImageView Logout, Cart;
    Handler h = new Handler();
    private FloatingActionButton voiceActivationButton;
    private SpeechRecognizer speechRecognizer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainscreen);

        voiceActivationButton = findViewById(R.id.voiceActivationButton);
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
        Cart = findViewById(R.id.addtocart);

        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLogOutConfirmationDialog();
            }
        });

        Cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                h.post(new Runnable() {
                    @Override
                    public void run() {
                        Intent i = new Intent(mainscreen.this, com.example.foodifyy.Cart.class);
                        startActivity(i);
                    }
                });
            }
        });

        initializeVoiceRecognition();
        voiceActivationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startVoiceRecognition();
            }
        });

    }

    private void startVoiceRecognition() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US");  // Change the language if needed

        speechRecognizer.startListening(intent);
    }

    private void initializeVoiceRecognition() {
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        speechRecognizer.setRecognitionListener(new YourRecognitionListener());
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


    private class YourRecognitionListener implements RecognitionListener {

        @Override
        public void onReadyForSpeech(Bundle params) {

        }

        @Override
        public void onBeginningOfSpeech() {

        }

        @Override
        public void onRmsChanged(float rmsdB) {

        }

        @Override
        public void onBufferReceived(byte[] buffer) {

        }

        @Override
        public void onEndOfSpeech() {

        }

        @Override
        public void onError(int error) {
            String errorMessage;
            switch (error) {
                case SpeechRecognizer.ERROR_AUDIO:
                    errorMessage = "Audio recording error";
                    break;
                case SpeechRecognizer.ERROR_CLIENT:
                    errorMessage = "Client error";
                    break;
                case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                    errorMessage = "Insufficient permissions";
                    break;
                case SpeechRecognizer.ERROR_NETWORK:
                    errorMessage = "Network error";
                    break;
                case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                    errorMessage = "Network timeout";
                    break;
                case SpeechRecognizer.ERROR_NO_MATCH:
                    errorMessage = "No match found";
                    break;
                case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                    errorMessage = "Recognizer busy";
                    break;
                case SpeechRecognizer.ERROR_SERVER:
                    errorMessage = "Server error";
                    break;
                case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                    errorMessage = "Speech timeout";
                    break;
                default:
                    errorMessage = "Unknown error";
                    break;
            }

            showToast("Speech recognition error: " + errorMessage);
        }

        private void showToast(String s) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(mainscreen.this, s, Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public void onResults(Bundle results) {
            ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            if (matches != null && !matches.isEmpty()) {
                String command = matches.get(0).toLowerCase();

                // Check for specific commands and take actions
                if (command.contains("order")) {
                    announceActionAndNavigate("Opening Order Screen", R.id.Order);
                } else if (command.contains("history")) {
                    announceActionAndNavigate("Opening History Screen", R.id.History);
                } else if (command.contains("home")) {
                    announceActionAndNavigate("Opening Home Screen", R.id.Home);
                } else if (command.contains("planner")) {
                    announceActionAndNavigate("Opening Planner Screen", R.id.MealPrep);
                } else if (command.contains("bank")) {
                    announceActionAndNavigate("Opening Bank Screen", R.id.Bank);
                } else {
                    // If the command is not recognized
                    showToast("Command not recognized");
                }
            }
        }

        @Override
        public void onPartialResults(Bundle partialResults) {

        }

        @Override
        public void onEvent(int eventType, Bundle params) {

        }
    }

    private void announceActionAndNavigate(String announcement, int destinationID) {
        showToast(announcement);

        NavController navController = Navigation.findNavController(mainscreen.this, R.id.navHostFragment);
        navController.navigate(destinationID);
    }

    private void showToast(String announcement) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(mainscreen.this, announcement, Toast.LENGTH_SHORT).show();

            }
        });
    }




}