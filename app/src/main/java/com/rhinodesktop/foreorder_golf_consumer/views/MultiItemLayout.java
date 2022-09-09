package com.rhinodesktop.foreorder_golf_consumer.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.CheckBox;

import com.rhinodesktop.foreorder_golf_consumer.R;

/**
 * Created by Hunter Andrin on 2017-04-11.
 */

public class MultiItemLayout extends ChoiceItemLayout {

    public MultiItemLayout(Context context) {
        super(context);
        initViews();
    }

    public MultiItemLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews();
    }

    public MultiItemLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews();
    }

    @Override
    protected void initViews() {
        inflate(getContext(), R.layout.multi_item_view, this);
        compoundOptionButton = (CheckBox) findViewById(R.id.checkbox_multi_option);
        super.initViews();
    }
}
