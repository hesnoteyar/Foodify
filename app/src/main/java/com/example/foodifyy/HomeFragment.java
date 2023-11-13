package com.example.foodifyy;

import android.content.Intent;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */


public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ConstraintLayout profile, bank, order, calendar;
    Handler h = new Handler();
    TextView greeting;
    FirebaseAuth auth;
    DatabaseReference databaseReference;
    private HomeFragment homeFragment;



    public HomeFragment() {
        // Required empty public constructor


    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        profile = v.findViewById(R.id.Profilebtn);
        bank = v.findViewById(R.id.Bankbtn);
        order = v.findViewById(R.id.Orderbtn);
        calendar = v.findViewById(R.id.Calendarbtn);
        greeting = v.findViewById(R.id.tv1);

        auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("users").child(currentUser.getUid());

        displayGreetings();



        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                NavController navController = Navigation.findNavController(getActivity(), R.id.navHostFragment);
                navController.navigate(R.id.action_Home_to_Profile);
                disableInteractions();
            }
        });

        bank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                NavController navController = Navigation.findNavController(getActivity(), R.id.navHostFragment);
                navController.navigate(R.id.action_Home_to_Bank);
                disableInteractions();



            }

        });

        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                NavController navController = Navigation.findNavController(getActivity(), R.id.navHostFragment);
                navController.navigate(R.id.action_Home_to_Order);
                disableInteractions();

            }
        });

        calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                NavController navController = Navigation.findNavController(getActivity(), R.id.navHostFragment);
                navController.navigate(R.id.action_Home_to_MealPrep);
                disableInteractions();

            }
        });

        return v;
    }



    private void displayGreetings() {
        databaseReference.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                String greet = documentSnapshot.child("username").getValue(String.class);

                greeting.setText("Hello, " + greet + "!");
            }
        });
    }

    private void disableInteractions() {
        profile.setEnabled(false);
        bank.setEnabled(false);
        order.setEnabled(false);
        calendar.setEnabled(false);
    }

    private void enableInteractions() {
        profile.setEnabled(true);
        bank.setEnabled(true);
        order.setEnabled(true);
        calendar.setEnabled(true);
    }

    public void onResume() {
        enableInteractions();
        super.onResume();
    }
}