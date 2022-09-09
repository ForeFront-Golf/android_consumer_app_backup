package com.rhinoactive.generalutilities.animations;

import android.view.View;
import android.view.animation.Animation;

/**
 * Created by rhinodesktop on 2017-03-22.
 */

public class HideViewAnimationRunnable implements Runnable {

    private View view;

    public HideViewAnimationRunnable(View view) {
        this.view = view;
    }

    @Override
    public void run() {
        view.setVisibility(View.GONE);
    }
}
