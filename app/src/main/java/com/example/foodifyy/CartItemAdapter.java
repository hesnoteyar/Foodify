package com.example.foodifyy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;

public class CartItemAdapter extends ArrayAdapter<CartItem> {

    public CartItemAdapter(Context context, ArrayList<CartItem> cartItems) {
        super(context, 0, cartItems);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        CartItem cartItem = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.cart_item_layout, parent, false);
        }

        // Lookup view for data population
        TextView itemName = convertView.findViewById(R.id.itemName);
        TextView itemQuantity = convertView.findViewById(R.id.itemQuantity);
        TextView itemTotalAmount = convertView.findViewById(R.id.itemTotalAmount);

        // Populate the data into the template view using the data object
        itemName.setText(cartItem.getItemName());
        itemQuantity.setText(String.valueOf(cartItem.getQuantity()));
        itemTotalAmount.setText(String.format("₱ %.2f", cartItem.getTotalAmount()));

        // Return the completed view to render on screen
        return convertView;
    }
}
