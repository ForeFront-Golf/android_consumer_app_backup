package com.rhinoactive.generalutilities.animations;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.graphics.Point;
import androidx.annotation.RequiresApi;
import android.view.View;
import android.view.ViewAnimationUtils;

import com.rhinoactive.generalutilities.ViewUtils;

/**
 * Created by hunter on 2018-02-06.
 */

/**
 * Utility class that uses {@link Animator} to animate views as they appears/disappears from the screen
 */
public class AnimatorUtils {

    /**
     * Note: This function assumes the view passed has visibility View.INVISIBLE or View.GONE.
     *
     * @param view the view to animate
     * @param animationDurationInMilliseconds The duration in milliseconds that the animation will last
     */
    @RequiresApi(21)
    public void circularRevealAnimationFromPt(View view, int animationDurationInMilliseconds) {
        Point centerPt = ViewUtils.getCenterPointOfView(view);
        float finalRadius = (float) Math.hypot(centerPt.x, centerPt.y);
        Animator animator = ViewAnimationUtils.createCircularReveal(view, centerPt.x, centerPt.y, 0, finalRadius);
        showAnimator(view, animator, animationDurationInMilliseconds);
    }

    /**
     * Note: This function assumes the view passed has visibility View.VISIBLE.
     *
     * @param view the view to animate
     * @param animationDurationInMilliseconds The duration in milliseconds that the animation will last
     */
    @RequiresApi(21)
    public void circularHideAnimationFromPt(final View view, int animationDurationInMilliseconds) {
        Point centerPt = ViewUtils.getCenterPointOfView(view);
        float finalRadius = (float) Math.hypot(centerPt.x, centerPt.y);
        Animator animator = ViewAnimationUtils.createCircularReveal(view, centerPt.x, centerPt.y, finalRadius, 0);
        hideAnimator(view, animator, animationDurationInMilliseconds);
    }

    private void showAnimator(final View view, Animator animator, final int animationDurationInMilliseconds) {
        Animator.AnimatorListener listener = new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                view.setVisibility(View.VISIBLE);
            }
        };
        animate(animator, listener, animationDurationInMilliseconds);
    }

    private void hideAnimator(final View view, Animator animator, final int animationDurationInMilliseconds) {
        Animator.AnimatorListener listener = new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                view.setVisibility(View.GONE);
            }
        };
        animate(animator, listener, animationDurationInMilliseconds);
    }

    private void animate(Animator animator, Animator.AnimatorListener listener, int animationDurationInMilliseconds) {
        animator.setDuration(animationDurationInMilliseconds);
        animator.addListener(listener);
        animator.start();
    }
}
