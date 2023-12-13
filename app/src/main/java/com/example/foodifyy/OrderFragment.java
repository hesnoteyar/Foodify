package com.example.foodifyy;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OrderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrderFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    TextView greeting;
    FirebaseAuth auth;
    ImageView menu1, menu2, menu3, menu4, menu5;
    DatabaseReference databaseReference;
    DatabaseReference foodDatabaseReference;
    Handler h = new Handler();
    private Context context;


    public OrderFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OrderFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OrderFragment newInstance(String param1, String param2) {
        OrderFragment fragment = new OrderFragment();
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
        View v = inflater.inflate(R.layout.fragment_order, container, false);

        greeting = v.findViewById(R.id.greetings1);
        menu1 = v.findViewById(R.id.Menu1);
        menu2 = v.findViewById(R.id.Menu2);
        menu3 = v.findViewById(R.id.Menu3);
        menu4 = v.findViewById(R.id.Menu4);
        menu5 = v.findViewById(R.id.Menu5);

        context = getContext();
        auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("users").child(currentUser.getUid());
        foodDatabaseReference = FirebaseDatabase.getInstance().getReference("food");

        displayGreetings();

        addFoodItem("Burger Steak", "FOOD001", 45);
        addFoodItem("Siomai", "FOOD002", 30);
        addFoodItem("Sisig", "FOOD003", 65);
        addFoodItem("Shawarma Rice", "FOOD004", 89);
        addFoodItem("Pinakbet", "FOOD005", 55);

        menu1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                h.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent i = new Intent(context, BurgerSteak.class);
                        startActivity(i);
                    }
                }, 0);
            }
        });


        return v;
    }

    private void addFoodItem(String foodName, String foodID, int foodPrice) {
        foodDatabaseReference.child(foodID).child("foodID").setValue(foodID);
        foodDatabaseReference.child(foodID).child("foodName").setValue(foodName);
        foodDatabaseReference.child(foodID).child("foodPrice").setValue(foodPrice);
    }


    private void displayGreetings() {
        databaseReference.get().addOnSuccessListener(documentSnapshot -> {
           if (documentSnapshot.exists()) {
               String greet = documentSnapshot.child("username").getValue(String.class);

               greeting.setText("Hi, " + greet + "!");
           }
        });
    }
}