package com.rhinoactive.foreorder_library_android.views;

import android.content.Context;
import androidx.core.widget.CompoundButtonCompat;
import android.util.AttributeSet;

/**
 * Created by Hunter Andrin on 2017-05-04.
 */

public class BlueRadioButton extends androidx.appcompat.widget.AppCompatRadioButton {

    public BlueRadioButton(Context context) {
        super(context);
        initViews(context);
    }

    public BlueRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(context);
    }

    public BlueRadioButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(context);
    }

    private void initViews(Context context) {
        CompoundButtonCompat.setButtonTintList(this, BlueColorStateList.newInstance(context));
    }
}