package com.rhinodesktop.foreorder_golf_consumer.listeners;

import android.app.Fragment;
import android.view.animation.Animation;

import com.rhinodesktop.activityanimatorutility.fragmentutils.SupportFragmentAnimateManager;

import timber.log.Timber;

/**
 * Created by hunter on 2018-03-19.
 */

public class HideFragmentAnimationListener implements Animation.AnimationListener {

    private Fragment fragment;

    public HideFragmentAnimationListener(Fragment fragment) {
        this.fragment = fragment;
    }

    @Override
    public void onAnimationStart(Animation animation) {}

    @Override
    public void onAnimationEnd(Animation animation) {
        try {
            SupportFragmentAnimateManager.getInstance().removeFragment(fragment);
        } catch (Exception e) {
            Timber.e(e.getMessage());
        }
    }

    @Override
    public void onAnimationRepeat(Animation animation) {}
}