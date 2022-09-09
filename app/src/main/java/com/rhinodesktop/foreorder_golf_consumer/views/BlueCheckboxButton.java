package com.rhinodesktop.foreorder_golf_consumer.views;

import android.content.Context;
import androidx.core.widget.CompoundButtonCompat;
import android.util.AttributeSet;

import com.rhinoactive.foreorder_library_android.views.BlueColorStateList;

/**
 * Created by Hunter Andrin on 2017-04-19.
 */

public class BlueCheckboxButton extends androidx.appcompat.widget.AppCompatCheckBox {
    public BlueCheckboxButton(Context context) {
        super(context);
        initViews(context);
    }

    public BlueCheckboxButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(context);
    }

    public BlueCheckboxButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(context);
    }

    private void initViews(Context context) {
        CompoundButtonCompat.setButtonTintList(this, BlueColorStateList.newInstance(context));
    }
}
