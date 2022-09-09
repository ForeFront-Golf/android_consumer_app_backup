package com.rhinodesktop.activityanimatorutility.models;

/**
 * Created by rhinodesktop on 2017-02-01.
 */

import com.rhinodesktop.activityanimatorutility.R;

/**
 * The animations that can be used to animate an activity transition.
 */
public enum AnimationType {
    SLIDE_LEFT(R.anim.push_left_enter, R.anim.push_left_exit),
    SLIDE_RIGHT(R.anim.push_right_enter, R.anim.push_right_exit),
    SLIDE_UP(R.anim.push_up_enter, R.anim.push_up_exit),
    SLIDE_DOWN(R.anim.push_down_enter, R.anim.push_down_exit),
    SLIDE_UP_OVER(R.anim.push_up_enter, R.anim.fade_out),
    SLIDE_DOWN_UNDER(R.anim.fade_in, R.anim.push_down_exit),
    SLIDE_UP_UNDER(R.anim.fade_in, R.anim.push_up_exit),
    SLIDE_DOWN_OVER(R.anim.push_down_enter, R.anim.fade_out),
    FADE_IN(R.anim.fade_in, R.anim.fade_out),
    NONE(0, 0);

    private int enterAnim;
    private int exitAnim;

    AnimationType(int enterAnim, int exitAnim) {
        this.enterAnim = enterAnim;
        this.exitAnim = exitAnim;
    }

    public int getEnterAnim() {
        return enterAnim;
    }

    public int getExitAnim() {
        return exitAnim;
    }
}
