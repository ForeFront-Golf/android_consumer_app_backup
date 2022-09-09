package com.rhinodesktop.foreorder_golf_consumer.listeners;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by Hunter Andrin on 2017-04-20.
 */

public class TitleImageOnOffsetChangedListener extends TitleOnOffsetChangedListener {

    private RelativeLayout courseTitleExpandedLayout;
    private boolean mIsTheTitleContainerVisible = true;

    public TitleImageOnOffsetChangedListener(TextView titleTextView, RelativeLayout courseTitleExpandedLayout) {
        super(titleTextView);
        this.courseTitleExpandedLayout = courseTitleExpandedLayout;
    }

    @Override
    protected void handleToolbarTitleVisibility(float percentage) {
        super.handleToolbarTitleVisibility(percentage);
        handleAlphaOnTitle(percentage);
    }

    private void handleAlphaOnTitle(float percentage) {
        if (percentage >= PERCENTAGE_TO_HIDE_TITLE_DETAILS) {
            if(mIsTheTitleContainerVisible) {
                startAlphaAnimation(courseTitleExpandedLayout, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleContainerVisible = false;
            }

        } else {

            if (!mIsTheTitleContainerVisible) {
                startAlphaAnimation(courseTitleExpandedLayout, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleContainerVisible = true;
            }
        }
    }
}
