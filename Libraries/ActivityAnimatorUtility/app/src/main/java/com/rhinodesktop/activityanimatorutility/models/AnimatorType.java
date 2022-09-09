package com.rhinodesktop.activityanimatorutility.models;

/**
 * Created by blakevanberlo on 2017-06-23.
 */

import com.rhinodesktop.activityanimatorutility.R;

/**
 * The animations that can be used to animate a fragment transition.
 */
public enum AnimatorType {
    SLIDE_LEFT(R.animator.push_left_enter, R.animator.push_left_exit),
    SLIDE_RIGHT(R.animator.push_right_enter, R.animator.push_right_exit),
    FADE_IN(R.animator.fade_in, R.animator.fade_out),
    NONE(0, 0);

    private int enterAnimator;
    private int exitAnimator;

    AnimatorType(int enterAnimator, int exitAnimator) {
        this.enterAnimator = enterAnimator;
        this.exitAnimator = exitAnimator;
    }

    public int getEnterAnimator() {
        return enterAnimator;
    }

    public int getExitAnimator() {
        return exitAnimator;
    }
}
