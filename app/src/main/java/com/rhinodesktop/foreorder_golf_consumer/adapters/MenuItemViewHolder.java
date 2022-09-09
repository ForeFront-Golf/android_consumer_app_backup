package com.rhinodesktop.foreorder_golf_consumer.adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.rhinodesktop.foreorder_golf_consumer.R;

import lombok.Getter;

/**
 * Created by rhinodesktop on 2017-03-17.
 */

public class MenuItemViewHolder extends RecyclerView.ViewHolder {

    // Used for the header menu item layout
    @Getter
    private TextView sectionHeaderNameTextview;
    @Getter
    private RelativeLayout menuInfoLayout;
    @Getter
    private TextView menuTitleTextView;
    @Getter
    private TextView menuInfoTextView;

    // Used for the normal menu item layout
    @Getter
    private TextView menuItemNameTextview;
    @Getter
    private TextView menuItemPriceTextview;
    @Getter
    private ImageView menuItemImageview;

    public MenuItemViewHolder(View view) {
        super(view);

        sectionHeaderNameTextview = (TextView) itemView.findViewById(R.id.textview_section_header_name);
        menuInfoLayout = (RelativeLayout) itemView.findViewById(R.id.rlayout_menu_info);
        menuTitleTextView = (TextView) itemView.findViewById(R.id.textview_menu_title);
        menuInfoTextView = (TextView) itemView.findViewById(R.id.textview_menu_info);


        menuItemNameTextview = (TextView) itemView.findViewById(R.id.textview_menu_item_name);
        menuItemPriceTextview = (TextView) itemView.findViewById(R.id.textview_menu_price);
        menuItemImageview = (ImageView) itemView.findViewById(R.id.imageview_menu_item);
    }
}
