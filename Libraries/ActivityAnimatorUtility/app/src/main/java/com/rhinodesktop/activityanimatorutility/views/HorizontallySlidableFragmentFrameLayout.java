package com.rhinodesktop.activityanimatorutility.views;

import android.content.Context;
import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Created by blakevanberlo on 2017-06-23.
 */

/**
 * In order to animate the transition of a Fragment by sliding it horizontally,
 * this view must be used as the containing view for that fragment.
 *
 * @see <a href="https://stackoverflow.com/a/4936159">https://stackoverflow.com/a/4936159</a>
 * for more detail.
 */
public class HorizontallySlidableFragmentFrameLayout extends FrameLayout {

    public HorizontallySlidableFragmentFrameLayout(@NonNull Context context) {
        super(context);
    }

    public HorizontallySlidableFragmentFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public HorizontallySlidableFragmentFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public float getXFraction() {
        return getX() / getWidth(); // TODO: guard divide-by-zero
    }

    public void setXFraction(float xFraction) {
        // TODO: cache width
        final int width = getWidth();
        setX((width > 0) ? (xFraction * width) : -9999);
    }
}
