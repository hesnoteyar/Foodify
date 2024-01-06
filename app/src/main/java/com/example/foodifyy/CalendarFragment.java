package com.example.foodifyy;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;
import java.util.List;

public class CalendarFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private TextView caloriesProgress;
    private TextView proteinProgress;
    private DatabaseReference weightDatabaseRef;
    private DatabaseReference usernameDatabaseRef;
    private TextView greeting;
    FirebaseAuth auth;
    FirebaseUser currentUser;
    private double userWeight;  // Add this variable to store the user's weight
    EditText search;
    TextView resultTextView;
    Button searchButton, logButton;

    private double totalLoggedCalories = 0;
    private double totalLoggedProtein = 0;

    // Added variables
    private double proteinValue;  // Add this variable to store protein value
    private double caloriesValue;  // Add this variable to store calories value

    public CalendarFragment() {
        // Required empty public constructor
    }

    public static CalendarFragment newInstance(String param1, String param2) {
        CalendarFragment fragment = new CalendarFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_calendar, container, false);

        resultTextView = v.findViewById(R.id.resultTextView);
        search = v.findViewById(R.id.search);
        logButton = v.findViewById(R.id.logButton);
        greeting = v.findViewById(R.id.tv1);
        caloriesProgress = v.findViewById(R.id.calorieTextview);
        proteinProgress = v.findViewById(R.id.proteinTextview);
        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();
        weightDatabaseRef = FirebaseDatabase.getInstance().getReference("users").child(currentUser.getUid());
        usernameDatabaseRef = FirebaseDatabase.getInstance().getReference("users").child(currentUser.getUid());

        fetchUserDataAndUpdateProgressTrackers();  // Fetch user data and update progress trackers
        displayUsername();

        logButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchTerm = search.getText().toString(); // Replace with your actual search term
                new FetchFoodInfoTask(resultTextView, new FoodInfoCallback() {
                    @Override
                    public void onFoodInfoLoaded(FoodInfo foodInfo) {
                        if (foodInfo != null) {
                            proteinValue = foodInfo.getProtein();  // Set protein value
                            caloriesValue = foodInfo.getCalories();  // Set calories value

                            // Fetch user data and update progress trackers
                            fetchUserDataAndUpdateProgressTrackers();

                            totalLoggedCalories += caloriesValue;
                            totalLoggedProtein += proteinValue;

                            updateTotalLoggedValues();
                        } else {
                            Log.e("FoodNutrientsApi", "API response is null");
                        }
                    }
                }).execute(searchTerm);
            }
        });
        return v;
    }

    private void updateTotalLoggedValues() {
        // Display the updated total logged values
        proteinProgress.setText(String.format("%.1f g", totalLoggedProtein));
        caloriesProgress.setText(String.format("%.1f kcal", totalLoggedCalories));
    }

    private void fetchUserDataAndUpdateProgressTrackers() {
        weightDatabaseRef.child("weight").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String weightString = snapshot.getValue(String.class);
                    userWeight = Double.parseDouble(weightString);

                    Log.d("YourTag", "User Weight: " + userWeight);

                    double suggestedCalories = calculateSuggestedCalories(userWeight);
                    double suggestedProtein = calculateSuggestedProtein(userWeight);

                    Log.d("YourTag", "Suggested Calories: " + suggestedCalories);
                    Log.d("YourTag", "Suggested Protein: " + suggestedProtein);

                    updateProgressTrackers(suggestedCalories, suggestedProtein);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled
            }
        });
    }

    private void updateProgressTrackers(double suggestedCalories, double suggestedProtein) {
        proteinProgress.setText(String.format("%.1f / %.1f g", totalLoggedProtein, suggestedProtein));
        caloriesProgress.setText(String.format("%.1f / %.1f kcal", totalLoggedCalories, suggestedCalories));
    }

    private double calculateSuggestedProtein(double weight) {
        return weight * 0.8; // Adjust this formula based on your requirements
    }

    private double calculateSuggestedCalories(double weight) {
        return weight * 30; // Adjust this formula based on your requirements
    }

    private void displayUsername() {
        usernameDatabaseRef.get().addOnSuccessListener(dataSnapshot -> {
            if (dataSnapshot.exists()) {
                String greet = dataSnapshot.child("username").getValue(String.class);

                greeting.setText("Hello, " + greet + "!");
            }
        });
    }

    private static class FetchFoodInfoTask extends AsyncTask<String, Void, FoodInfo> {
        private TextView resultTextView;
        private FoodInfoCallback callback;

        public FetchFoodInfoTask(TextView resultTextView, FoodInfoCallback callback) {
            this.resultTextView = resultTextView;
            this.callback = callback;
        }

        @Override
        protected FoodInfo doInBackground(String... params) {
            String searchTerm = params[0];
            return FoodNutrientsApi.getFoodInfo(searchTerm);
        }

        @Override
        protected void onPostExecute(FoodInfo foodInfo) {
            if (callback != null) {
                callback.onFoodInfoLoaded(foodInfo);
            }
            if (foodInfo != null) {
                resultTextView.setText(String.format("Fats: %.1f g \nProtein: %.1f g \nCalories: %.1f kcal",
                        foodInfo.getFats(), foodInfo.getProtein(), foodInfo.getCalories()));
            } else {
                resultTextView.setText("No data found");
                Log.e("FoodNutrientsApi", "API response is null");
            }
        }
    }
}
