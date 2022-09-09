package com.rhinoactive.generalutilities.animations;

import android.view.View;

import com.rhinoactive.generalutilities.callbacks.HideAnimationCallback;

/**
 * Created by andri on 2017-12-18.
 */

public class HideViewAnimationWithCallbackRunnable extends HideViewAnimationRunnable {

    private HideAnimationCallback callback;

    public HideViewAnimationWithCallbackRunnable(View view, HideAnimationCallback callback) {
        super(view);
        this.callback = callback;
    }

    @Override
    public void run() {
        super.run();
        callback.animationFinished();
    }
}
