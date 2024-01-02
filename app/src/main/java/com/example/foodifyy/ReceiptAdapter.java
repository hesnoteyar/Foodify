package com.example.foodifyy;

// ReceiptAdapter.java
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class ReceiptAdapter extends ArrayAdapter<CartItem> {

    public ReceiptAdapter(@NonNull Context context, @NonNull List<CartItem> items) {
        super(context, 0, items);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.receipt_list_item, parent, false);
        }

        TextView itemNameTextView = convertView.findViewById(R.id.itemNameTextView);
        TextView itemQuantityTextView = convertView.findViewById(R.id.itemQuantityTextView);
        TextView itemAmountTextView = convertView.findViewById(R.id.itemAmountTextView);

        CartItem cartItem = getItem(position);

        if (cartItem != null) {
            itemNameTextView.setText(cartItem.getFoodName());
            itemQuantityTextView.setText(String.valueOf(cartItem.getQuantity()));
            itemAmountTextView.setText(String.valueOf(cartItem.getAmount()));
        }

        return convertView;
    }
}

