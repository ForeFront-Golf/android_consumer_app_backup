package com.rhinodesktop.activityanimatorutility.activityutils;

import android.app.Fragment;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;


import com.rhinodesktop.activityanimatorutility.models.AnimationType;

/**
 * Created by hunter on 2018-02-13.
 */

/**
 * Class that provides methods to start a new activity and animate the transition.
 */
public class ActivityAnimationUtils {

    /**
     * Animates the transition when switching between two activities. Should be called after
     * starting a new activity or after onBackPressed() or an equivalent function when navigating
     * back from another activity.
     *
     * @param previousActivity A reference to the calling activity.
     * @param animType The type of animation that will occur when transitioning between activities.
     */
    public static void animateActivityTransition(AppCompatActivity previousActivity, AnimationType animType) {
        previousActivity.overridePendingTransition(animType.getEnterAnim(), animType.getExitAnim());
    }

    /**
     *
     * @param intent The intent to start.
     * @param previousActivity A reference to the calling activity.
     * @param animType The type of animation that will occur when transitioning between activities.
     */
    public static void startActivityWithAnimation(Intent intent, AppCompatActivity previousActivity, AnimationType animType) {
        previousActivity.startActivity(intent);
        animateActivityTransition(previousActivity, animType);
    }

    /**
     *
     * @param intent The intent to start.
     * @param requestCode If >= 0, this code will be returned in the activity's
     *                     when the activity exits.
     * @param previousActivity A reference to the calling activity.
     * @param animType The type of animation that will occur when transitioning between activities.
     */
    public static void startActivityWithAnimationForResult(Intent intent, int requestCode, AppCompatActivity previousActivity, AnimationType animType) {
        previousActivity.startActivityForResult(intent, requestCode);
        animateActivityTransition(previousActivity, animType);
    }

    /**
     *
     * @param intent The intent to start.
     * @param requestCode If >= 0, this code will be returned in the fragment's
     *                    {@link Fragment#onActivityResult(int, int, Intent)} when the activity exits.
     * @param fragment A reference to the calling fragment.
     * @param animType The type of animation that will occur when transitioning between the new
     *                 activity and the fragment's containing activity.
     */
    public static void startActivityWithAnimationForResult(Intent intent, int requestCode, Fragment fragment, AnimationType animType) {
        fragment.startActivityForResult(intent, requestCode);
        animateActivityTransition((AppCompatActivity) fragment.getActivity(), animType);
    }

    /**
     *
     * @param intent The intent to start.
     * @param requestCode If >= 0, this code will be returned in the support fragment's
     *                    {@link androidx.core.app.Fragment#onActivityResult(int, int, Intent)}
     *                    when the activity exits.
     * @param fragment A reference to the calling support fragment.
     * @param animType The type of animation that will occur when transitioning between the new
     *                 activity and the fragment's containing activity.
     */
//    public static void startActivityWithAnimationForResult(Intent intent, int requestCode, Fragment fragment, AnimationType animType) {
//        fragment.startActivityForResult(intent, requestCode);
//        animateActivityTransition(fragment.getActivity(), animType);
//    }
}
