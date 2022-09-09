package com.rhinodesktop.foreorder_golf_consumer.views;

import android.content.Context;

import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.viewpager.widget.ViewPager;

/**
 * Created by rhinodesktop on 2017-03-28.
 */

public class ToggleSwipingPageViewer extends ViewPager {

    private boolean enabled = true;

    public ToggleSwipingPageViewer(Context context) {
        super(context);
    }

    public ToggleSwipingPageViewer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (this.enabled) {
            return super.onTouchEvent(event);
        }

        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (this.enabled) {
            return super.onInterceptTouchEvent(event);
        }

        return false;
    }

    public void setPagingEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
