package com.rhinodesktop.foreorder_golf_consumer.adapters;

import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.rhinodesktop.foreorder_golf_consumer.R;

import lombok.Getter;

/**
 * Created by rhinodesktop on 2017-03-28.
 */

public class OrderItemViewHolder extends RecyclerView.ViewHolder {

    // Relative Layout for each row
    @Getter
    private RelativeLayout rLayoutSubtotal;
    @Getter
    private RelativeLayout rLayoutHST;

    // Used for the cart cost layout
    @Getter
    private TextView subtotalTextView;
    @Getter
    private TextView hstTextView;
    @Getter
    private TextView totalPriceTextView;

    // Used for the cart item layout
    @Getter
    private TextView itemNameTextView;
    @Getter
    private TextView instructionsTextView;
    @Getter
    private TextView numOfItemsTextView;
    @Getter
    private TextView priceOfItemsTextView;
    @Getter
    private ImageButton removeItemButton;
    @Getter
    private LinearLayout optionItemsLayout;

    public OrderItemViewHolder(View itemView) {
        super(itemView);

        rLayoutSubtotal = itemView.findViewById(R.id.rlayout_subtotal);
        rLayoutHST = itemView.findViewById(R.id.rlayout_hst);

        subtotalTextView = itemView.findViewById(R.id.textview_subtotal_price);
        hstTextView = itemView.findViewById(R.id.textview_hst_price);
        totalPriceTextView = itemView.findViewById(R.id.textview_order_total_price);

        itemNameTextView = itemView.findViewById(R.id.textview_cart_item_name);
        instructionsTextView = itemView.findViewById(R.id.textview_cart_item_instructions);
        numOfItemsTextView = itemView.findViewById(R.id.textview_number_of_cart_items);
        priceOfItemsTextView = itemView.findViewById(R.id.textview_price_of_cart_items);
        removeItemButton = itemView.findViewById(R.id.button_remove_item);
        optionItemsLayout = itemView.findViewById(R.id.llayout_option_items_container);
    }
}
