package com.example.foodifyy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CartAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<CartItem> cartItems;

    public CartAdapter(Context context, ArrayList<CartItem> cartItems) {
        this.context = context;
        this.cartItems = cartItems;
    }

    @Override
    public int getCount() {
        return cartItems.size();
    }

    @Override
    public Object getItem(int position) {
        return cartItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.cart_item_layout, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        CartItem cartItem = (CartItem) getItem(position);

        viewHolder.foodNameTextView.setText(cartItem.getFoodName());
        viewHolder.quantityTextView.setText("Quantity: " + cartItem.getQuantity());
        viewHolder.amountTextView.setText("Amount: ₱" + cartItem.getAmount());

        return convertView;
    }

    private static class ViewHolder {
        TextView foodNameTextView;
        TextView quantityTextView;
        TextView amountTextView;

        ViewHolder(View view) {
            foodNameTextView = view.findViewById(R.id.foodNameTextView);
            quantityTextView = view.findViewById(R.id.quantityTextView);
            amountTextView = view.findViewById(R.id.amountTextView);
        }
    }
}

