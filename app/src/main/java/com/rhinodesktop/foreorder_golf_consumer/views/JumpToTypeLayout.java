package com.rhinodesktop.foreorder_golf_consumer.views;

import android.content.Context;

import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.rhinodesktop.foreorder_golf_consumer.R;

/**
 * Created by Hunter Andrin on 2017-04-10.
 */

public class JumpToTypeLayout extends LinearLayout {

    private TextView itemTypeNameTextView;

    public JumpToTypeLayout(Context context) {
        super(context);
        initView();
    }

    public JumpToTypeLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public JumpToTypeLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        inflate(getContext(), R.layout.jump_to_type_view, this);
        this.itemTypeNameTextView = (TextView) findViewById(R.id.textview_jump_to_item_type_name);
    }

    public void setItemTypeNameTextView(String itemTypeName) {
        itemTypeNameTextView.setText(itemTypeName);
    }
}
