package com.rhinoactive.generalutilities.models;

import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

import com.rhinoactive.generalutilities.R;

/**
 * Created by hunter on 2018-02-05.
 */

public enum ShowViewAnimation {

    GROW_HORIZONTAL_FROM_LEFT(new ScaleAnimation(0, 1, 1, 1, Animation.RELATIVE_TO_SELF , 0, Animation.RELATIVE_TO_SELF , 0)),
    GROW_FROM_CENTER(new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF , 0.5f, Animation.RELATIVE_TO_SELF , 0.5f)),
    GROW_FROM_BOTTOM_RIGHT(new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF , 1, Animation.RELATIVE_TO_SELF , 1)),
    FADE_IN(new AlphaAnimation(0.0f, 1.0f)),
    SLIDE_UP(new TranslateAnimation(Animation.RELATIVE_TO_SELF,0, Animation.RELATIVE_TO_SELF,0, Animation.RELATIVE_TO_SELF,1, Animation.RELATIVE_TO_SELF,0)),
    SLIDE_LEFT(new TranslateAnimation(Animation.RELATIVE_TO_SELF,1, Animation.RELATIVE_TO_SELF,0, Animation.RELATIVE_TO_SELF,0, Animation.RELATIVE_TO_SELF,0)),
    SLIDE_RIGHT(new TranslateAnimation(Animation.RELATIVE_TO_SELF,-1, Animation.RELATIVE_TO_SELF,0, Animation.RELATIVE_TO_SELF,0, Animation.RELATIVE_TO_SELF,0)),
    SLIDE_DOWN(new TranslateAnimation(Animation.RELATIVE_TO_SELF,0, Animation.RELATIVE_TO_SELF,0, Animation.RELATIVE_TO_SELF,-1, Animation.RELATIVE_TO_SELF,0));

    private Animation animation;

    ShowViewAnimation(Animation animation) {
        this.animation = animation;
    }

    public Animation getAnimation() {
        return animation;
    }
}
