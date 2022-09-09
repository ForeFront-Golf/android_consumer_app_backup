package com.rhinoactive.foreorder_library_android.views;

import android.content.Context;
import android.content.res.ColorStateList;
import androidx.annotation.ColorInt;
import androidx.core.content.ContextCompat;

import com.rhinoactive.foreorder_library_android.R;

/**
 * Created by Hunter Andrin on 2017-05-04.
 */

public class BlueColorStateList extends ColorStateList {

    private BlueColorStateList(int[][] states, @ColorInt int[] colors) {
        super(states, colors);
    }

    public static BlueColorStateList newInstance(Context context) {
        return new BlueColorStateList(
                new int[][]{
                        new int[]{android.R.attr.state_enabled}
                },
                new int[] {
                        ContextCompat.getColor(context, R.color.fore_order_blue)
                }
        );
    }
}
