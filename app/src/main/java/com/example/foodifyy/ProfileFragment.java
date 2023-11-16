package com.example.foodifyy;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View view;
    FirebaseAuth auth;
    DatabaseReference databaseReference;
    TextView emailEditText, unameEditText, contactEditText, fnameEditText, mnameEditText, lnameEditText, addressEditText, edit;


    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        

        auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("users").child(currentUser.getUid());

        Context context = getContext();


        emailEditText = view.findViewById(R.id.display_email);
         fnameEditText = view.findViewById(R.id.display_fname);
         lnameEditText = view.findViewById(R.id.display_lname);
         unameEditText = view.findViewById(R.id.display_uname);
         contactEditText = view.findViewById(R.id.display_number);
         mnameEditText = view.findViewById(R.id.display_mname);
         addressEditText = view.findViewById(R.id.display_address);
         edit = view.findViewById(R.id.edit_profile);

        retrieveProfileInformation();

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity( new Intent(context, EditProfile.class));
            }
        });
       

       

        return view;
    }

    private void retrieveProfileInformation() {
        databaseReference.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                String email = documentSnapshot.child("emailaddress").getValue(String.class);
                String firstname = documentSnapshot.child("firstname").getValue(String.class);
                String middlename = documentSnapshot.child("middlename").getValue(String.class);
                String lastname = documentSnapshot.child("lastname").getValue(String.class);
                String username = documentSnapshot.child("username").getValue(String.class);
                String phone = documentSnapshot.child("phonenumber").getValue(String.class);
                String house_no = documentSnapshot.child("housenumber").getValue(String.class);
                String barangay_city = documentSnapshot.child("barangay,city").getValue(String.class);
                String region_province = documentSnapshot.child("region,province").getValue(String.class);

                emailEditText.setText(email);
                fnameEditText.setText(firstname);
                lnameEditText.setText(lastname);
                unameEditText.setText(username);
                mnameEditText.setText(middlename);
                contactEditText.setText(phone);
                addressEditText.setText(house_no + ", " + barangay_city + ", " + region_province);
            }
        });
    }

}