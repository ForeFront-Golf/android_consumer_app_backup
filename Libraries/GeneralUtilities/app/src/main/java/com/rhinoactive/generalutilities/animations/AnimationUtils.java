package com.rhinoactive.generalutilities.animations;

import androidx.annotation.RequiresApi;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;

import com.rhinoactive.generalutilities.MainThreadRunner;
import com.rhinoactive.generalutilities.callbacks.HideAnimationCallback;
import com.rhinoactive.generalutilities.models.HideViewAnimation;
import com.rhinoactive.generalutilities.models.ShowViewAnimation;

/**
 * Created by rhinodesktop on 2017-03-22.
 */

/**
 * Utility class that uses {@link Animation} to animate views as they appears/disappears from the screen
 */
public class AnimationUtils {

    private static AnimationUtils animationUtils = null;

    private AnimationUtils() {}

    public static AnimationUtils getInstance() {
        if (animationUtils == null) {
            animationUtils = new AnimationUtils();
        }
        return animationUtils;
    }

    /**
     *
     * @param view The view to animate
     * @param showViewAnimation The type of animation that will be used to animate the view
     * @param animationDurationInMilliseconds The duration in milliseconds that the animation will last
     */
    public void showAnimation(final View view, final ShowViewAnimation showViewAnimation, final int animationDurationInMilliseconds) {
        MainThreadRunner.runFromUiThread(new Runnable() {
            @Override
            public void run() {
                view.setVisibility(View.VISIBLE);
            }
        });
        animate(view, showViewAnimation.getAnimation(), animationDurationInMilliseconds);
    }

    /**
     *
     * @param view The view to animate
     * @param hideViewAnimation The type of animation that will be used to animate the view
     * @param animationDurationInMilliseconds The duration in milliseconds that the animation will last
     */
    public void hideAnimation(final View view, final HideViewAnimation hideViewAnimation, final int animationDurationInMilliseconds) {
        HideViewAnimationRunnable runnable = new HideViewAnimationRunnable(view);
        hideAnimation(view, hideViewAnimation, animationDurationInMilliseconds, runnable);
    }

    /**
     *
     * @param view The view to animate
     * @param hideViewAnimation The type of animation that will be used to animate the view
     * @param animationDurationInMilliseconds The duration in milliseconds that the animation will last
     * @param callback Logic that will get executed when the animation finishes
     */
    public void hideAnimationWithCallback(final View view, final HideViewAnimation hideViewAnimation, final int animationDurationInMilliseconds, HideAnimationCallback callback) {
        HideViewAnimationRunnable runnable = new HideViewAnimationWithCallbackRunnable(view, callback);
        hideAnimation(view, hideViewAnimation, animationDurationInMilliseconds, runnable);
    }

    private void hideAnimation(final View view, final HideViewAnimation hideViewAnimation, final int animationDurationInMilliseconds, HideViewAnimationRunnable runnable) {
        Animation anim = hideViewAnimation.getAnimation();
        new android.os.Handler().postDelayed(runnable, anim.getDuration());
        animate(view, anim, animationDurationInMilliseconds);
    }

    private void animate(final View view, final Animation anim, final int animationDurationInMilliseconds) {
        anim.setDuration(animationDurationInMilliseconds);
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        MainThreadRunner.runFromUiThread(new Runnable() {
            @Override
            public void run() {
                view.startAnimation(anim);
            }
        });
    }
}
