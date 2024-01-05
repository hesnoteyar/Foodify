package com.example.foodifyy;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;
import java.util.Objects;

/**0
 * A simple {@link Fragment} subclass.
 * Use the {@link BankFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BankFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    FirebaseAuth auth;
    ConstraintLayout Topup, send;
    TextView points;

    public BankFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BankFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BankFragment newInstance(String param1, String param2) {
        BankFragment fragment = new BankFragment();
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
        View v = inflater.inflate(R.layout.fragment_bank, container, false);
        Topup = v.findViewById(R.id.topup);
        send = v.findViewById(R.id.send);
        auth = FirebaseAuth.getInstance();
        points = v.findViewById(R.id.mainPts);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("points");
        DatabaseReference userReference = FirebaseDatabase.getInstance().getReference("users");
        DatabaseReference transactionHistoryReference = FirebaseDatabase.getInstance().getReference("transaction_history");

        Context context = getActivity();
        FirebaseUser currentUser = auth.getCurrentUser();


        databaseReference.child("topUp1").setValue("50");
        databaseReference.child("topUp2").setValue("100");
        databaseReference.child("topUp3").setValue("500");
        databaseReference.child("topUp4").setValue("1000");

        points.setText("0");

        userReference.child(currentUser.getUid()).child("points").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Integer currentPoints = snapshot.getValue(Integer.class);
                    String pointsAsString = String.valueOf(currentPoints);
                    points.setText(pointsAsString);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "Error fetching points: " + error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSendPointsDialog();
            }
        });


        Topup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog customDialog = new Dialog(context);
                customDialog.setContentView(R.layout.topup_dialog);

                RelativeLayout first = customDialog.findViewById(R.id.first_selector);
                RelativeLayout second = customDialog.findViewById(R.id.second_selector);
                RelativeLayout third = customDialog.findViewById(R.id.third_selector);
                RelativeLayout fourth = customDialog.findViewById(R.id.fourth_selector);

                first.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        handleTopUp("topUp1");
                        customDialog.dismiss();
                        Toast.makeText(context, "TopUp Successful", Toast.LENGTH_SHORT).show();
                    }
                });

                second.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        handleTopUp("topUp2");
                        customDialog.dismiss();
                        Toast.makeText(context, "TopUp Successful", Toast.LENGTH_SHORT).show();
                    }
                });

                third.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        handleTopUp("topUp3");
                        customDialog.dismiss();
                        Toast.makeText(context, "TopUp Successful", Toast.LENGTH_SHORT).show();
                    }
                });

                fourth.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        handleTopUp("topUp4");
                        customDialog.dismiss();
                        Toast.makeText(context, "TopUp Successful", Toast.LENGTH_SHORT).show();
                    }
                });

                customDialog.setTitle("Custom Dialog");
                customDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                customDialog.show();

            }
        });

        return v;
    }

    private void openSendPointsDialog() {
        Dialog sendDialog = new Dialog(getContext());
        sendDialog.setContentView(R.layout.send_dialogue);

        EditText recipientNumberEditText = sendDialog.findViewById(R.id.number);
        EditText pointsEditText = sendDialog.findViewById(R.id.amount);
        Button sendButton = sendDialog.findViewById(R.id.send);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String recipientNumber = recipientNumberEditText.getText().toString().trim();
                String pointsStr = pointsEditText.getText().toString().trim();

                if (!recipientNumber.isEmpty() && !pointsStr.isEmpty()) {
                    int pointsToSend = Integer.parseInt(pointsStr);
                    sendPointsToUser(recipientNumber, pointsToSend);
                    sendDialog.dismiss();
                } else {
                    Toast.makeText(requireContext(), "Enter recipient number and points", Toast.LENGTH_SHORT).show();
                }

            }
        });

        sendDialog.setTitle("Custom Dialog");
        sendDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        sendDialog.show();

    }

    private void sendPointsToUser(String recipientNumber, int points) {
        FirebaseUser currentUser = auth.getCurrentUser();
        DatabaseReference userReference = FirebaseDatabase.getInstance().getReference("users");

        userReference.orderByChild("phonenumber").equalTo(recipientNumber).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        String recipientUserId = userSnapshot.getKey();
                        updatePointsForRecipient(recipientUserId, points);
                        deductPointsForSender(points);
                    }
                } else {
                    Toast.makeText(requireContext(), "Recipient not found", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(requireContext(), "Error finding recipient: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deductPointsForSender(int points) {
        DatabaseReference userReference = FirebaseDatabase.getInstance().getReference("users");
        FirebaseUser currentUser = auth.getCurrentUser();

        userReference.child(currentUser.getUid()).child("points").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    int currentPoints = snapshot.getValue(Integer.class);
                    int newPoints = currentPoints - points;
                    userReference.child(currentUser.getUid()).child("points").setValue(newPoints);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(requireContext(), "Error deducting points for sender: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updatePointsForRecipient(String recipientUserId, int points) {
        DatabaseReference userReference = FirebaseDatabase.getInstance().getReference("users");

        userReference.child(recipientUserId).child("points").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    int currentPoints = snapshot.getValue(Integer.class);
                    int newPoints = currentPoints + points;
                    userReference.child(recipientUserId).child("points").setValue(newPoints);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(requireContext(), "Error updating points for recipient: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleTopUp(String topUpKey) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("points");
        DatabaseReference userReference = FirebaseDatabase.getInstance().getReference("users");
        DatabaseReference transactionHistoryReference = FirebaseDatabase.getInstance().getReference("transaction_history");
        FirebaseUser currentUser = auth.getCurrentUser();

        // Retrieve the top-up points from the points table
        databaseReference.child(topUpKey).get().addOnSuccessListener(dataSnapshot -> {
            if (dataSnapshot.exists()) {
                int topUpPoints = Integer.parseInt(dataSnapshot.getValue(String.class));

                // Retrieve the current user's points from the users table
                userReference.child(currentUser.getUid()).child("points").get().addOnSuccessListener(userPointsSnapshot -> {
                    if (userPointsSnapshot.exists()) {
                        int currentPoints = userPointsSnapshot.getValue(Integer.class);

                        // Add the top-up points to the current user's points
                        int newPoints = currentPoints + topUpPoints;

                        // Update the user's points in the users table
                        userReference.child(currentUser.getUid()).child("points").setValue(newPoints);

                        // Save the transaction in the transaction history
                        saveTransactionToHistory(currentUser.getUid(), topUpPoints);
                    } else {
                        // If the user's points node does not exist, initialize it with the top-up points
                        userReference.child(currentUser.getUid()).child("points").setValue(topUpPoints);

                        // Save the transaction in the transaction history
                        saveTransactionToHistory(currentUser.getUid(), topUpPoints);
                    }
                });
            }
        });
    }

    private void saveTransactionToHistory(String userId, int topUpPoints) {
        DatabaseReference transactionHistoryReference = FirebaseDatabase.getInstance().getReference("transaction_history");

        // Create a unique key for the transaction
        String transactionKey = transactionHistoryReference.push().getKey();

        // Build the transaction object
        Transaction transaction = new Transaction(userId, "TopUp", topUpPoints);
        transaction.setTransactionKey(transactionKey); // Set the transactionKey

        // Set the transaction date
        transaction.setTransactionDate(new Date());

        // Save the transaction in the transaction history node
        transactionHistoryReference.child(userId).child(transactionKey).setValue(transaction);
    }

    private void retrievePoints() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");
        FirebaseUser currentUser = auth.getCurrentUser();

        databaseReference.get().addOnSuccessListener(dataSnapshot -> {
           if (dataSnapshot.exists()) {
               Integer currentPoints = dataSnapshot.child(currentUser.getUid()).child("points").getValue(Integer.class);
                String pointsAsString = String.valueOf(currentPoints);


               points.setText(pointsAsString);
           }
        });
    }
}