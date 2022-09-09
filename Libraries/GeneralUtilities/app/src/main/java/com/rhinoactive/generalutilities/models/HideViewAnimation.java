package com.rhinoactive.generalutilities.models;

import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

/**
 * Created by hunter on 2018-02-05.
 */

public enum HideViewAnimation {

    SHRINK_TO_CENTER(new ScaleAnimation(1, 0, 1, 0, Animation.RELATIVE_TO_SELF , 0.5f, Animation.RELATIVE_TO_SELF , 0.5f)),
    SHRINK_TO_BOTTOM_RIGHT(new ScaleAnimation(1, 0, 1, 0, Animation.RELATIVE_TO_SELF , 1, Animation.RELATIVE_TO_SELF , 1)),
    FADE_OUT(new AlphaAnimation(1.0f, 0.0f)),
    SLIDE_DOWN(new TranslateAnimation(Animation.RELATIVE_TO_SELF,0, Animation.RELATIVE_TO_SELF,0, Animation.RELATIVE_TO_SELF,0, Animation.RELATIVE_TO_SELF,1)),
    SLIDE_LEFT(new TranslateAnimation(Animation.RELATIVE_TO_SELF,0, Animation.RELATIVE_TO_SELF,-1, Animation.RELATIVE_TO_SELF,0, Animation.RELATIVE_TO_SELF,0)),
    SLIDE_RIGHT(new TranslateAnimation(Animation.RELATIVE_TO_SELF,0, Animation.RELATIVE_TO_SELF,1, Animation.RELATIVE_TO_SELF,0, Animation.RELATIVE_TO_SELF,0)),
    SLIDE_UP(new TranslateAnimation(Animation.RELATIVE_TO_SELF,0, Animation.RELATIVE_TO_SELF,0, Animation.RELATIVE_TO_SELF,0, Animation.RELATIVE_TO_SELF,-1));

    private Animation animation;

    HideViewAnimation(Animation animation) {
        this.animation = animation;
    }

    public Animation getAnimation() {
        return animation;
    }
}
