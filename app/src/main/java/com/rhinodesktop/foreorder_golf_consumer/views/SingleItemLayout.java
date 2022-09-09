package com.rhinodesktop.foreorder_golf_consumer.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RadioButton;

import com.rhinodesktop.foreorder_golf_consumer.R;

/**
 * Created by Hunter Andrin on 2017-04-11.
 */

public class SingleItemLayout extends ChoiceItemLayout {

    public SingleItemLayout(Context context) {
        super(context);
        initViews();
    }

    public SingleItemLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews();
    }

    public SingleItemLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews();
    }

    @Override
    protected void initViews() {
        inflate(getContext(), R.layout.single_item_view, this);
        compoundOptionButton = (RadioButton) findViewById(R.id.radiobtn_single_option);
        super.initViews();
    }
}
