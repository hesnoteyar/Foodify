package com.example.foodifyy;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HistoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HistoryFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ListView historyListView;
    private List<String> transactionList;
    private ArrayAdapter<String> adapter;
    private TextView noTransactionTV;
    private ImageView noTransactionIV;


    public HistoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HistoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HistoryFragment newInstance(String param1, String param2) {
        HistoryFragment fragment = new HistoryFragment();
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
        View v = inflater.inflate(R.layout.fragment_history, container, false);

        historyListView = v.findViewById(R.id.historyListView);
        noTransactionIV = v.findViewById(R.id.nohistoryImageView);
        noTransactionTV = v.findViewById(R.id.nohistoryTextView);
        transactionList = new ArrayList<>();
        adapter = new ArrayAdapter<>(requireContext(), R.layout.list_item_history, R.id.textViewHistory, transactionList);
        historyListView.setAdapter(adapter);

        // Retrieve and display transaction history
        retrieveTransactionHistory();

        return v;
    }

    private void retrieveTransactionHistory() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();

        if (currentUser != null) {
            DatabaseReference transactionHistoryReference = FirebaseDatabase.getInstance().getReference("transaction_history").child(currentUser.getUid());

            transactionHistoryReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    transactionList.clear();

                    for (DataSnapshot transactionSnapshot : snapshot.getChildren()) {
                        Transaction transaction = transactionSnapshot.getValue(Transaction.class);

                        if  (transaction != null) {
                            transaction.setTransactionKey(transactionSnapshot.getKey());


                            String transactionInfo = "Transaction Key: " + transaction.getTransactionKey() + "\nDate: " + transaction.getFormattedTransactionDate() + "\nType: " + transaction.getType() + "\nPoints: " + transaction.getPoints();
                            transactionList.add(transactionInfo);
                        }
                    }

                    updateNoTransactionViewsVisibility();

                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    private void updateNoTransactionViewsVisibility() {
        if (transactionList.isEmpty()) {
            noTransactionTV.setVisibility(View.VISIBLE);
            noTransactionIV.setVisibility(View.VISIBLE);
        } else {
            noTransactionTV.setVisibility(View.GONE);
            noTransactionIV.setVisibility(View.GONE);
        }
    }
}