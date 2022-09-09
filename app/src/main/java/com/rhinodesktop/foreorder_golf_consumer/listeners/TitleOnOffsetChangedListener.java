package com.rhinodesktop.foreorder_golf_consumer.listeners;

import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;

import com.google.android.material.appbar.AppBarLayout;

/**
 * Created by Hunter Andrin on 2017-04-20.
 */

public class TitleOnOffsetChangedListener implements AppBarLayout.OnOffsetChangedListener {

    protected static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR  = 0.9f;
    protected static final float PERCENTAGE_TO_HIDE_TITLE_DETAILS     = 0.2f;
    protected static final int ALPHA_ANIMATIONS_DURATION              = 200;
    private boolean mIsTheTitleVisible          = false;

    private TextView titleTextView;

    public TitleOnOffsetChangedListener(TextView titleTextView) {
        this.titleTextView = titleTextView;
        startAlphaAnimation(this.titleTextView, 0, View.GONE);
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        int maxScroll = appBarLayout.getTotalScrollRange();
        float percentage = (float) Math.abs(verticalOffset) / (float) maxScroll;
        handleToolbarTitleVisibility(percentage);
    }

    protected void handleToolbarTitleVisibility(float percentage) {
        if (percentage >= PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR) {

            if(!mIsTheTitleVisible) {
                startAlphaAnimation(titleTextView, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleVisible = true;
            }

        } else {

            if (mIsTheTitleVisible) {
                startAlphaAnimation(titleTextView, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleVisible = false;
            }
        }
    }

    protected void startAlphaAnimation (View v, long duration, int visibility) {
        AlphaAnimation alphaAnimation = (visibility == View.VISIBLE)
                ? new AlphaAnimation(0f, 1f)
                : new AlphaAnimation(1f, 0f);

        alphaAnimation.setDuration(duration);
        alphaAnimation.setFillAfter(true);
        v.startAnimation(alphaAnimation);
    }
}
