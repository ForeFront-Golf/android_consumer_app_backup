package com.rhinodesktop.activityanimatorutility.activityutils;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcelable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.rhinodesktop.activityanimatorutility.models.AnimationType;

import java.io.Serializable;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;
import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * Created by rhinodesktop on 2017-02-01.
 */

/**
 * Class that handles constructing the activity transition and starting the new activity.
 */
public class ActivityAndAnimateManager {

    /**
     * Private constructor, use the {@link Builder} class to start the new activity and animate
     * the transition.
     *
     * @param builder Builder for the ActivityAndAnimateManager.
     */
    private ActivityAndAnimateManager(Builder builder) {
        proceedToActivity(builder);
    }

    private void proceedToActivity(Builder builder) {
        Handler handler = new Handler(Looper.getMainLooper());
        Runnable activityRunnable = createActivityTransitionRunnable(builder);
        int transitionDelay = builder.transitionDelay;
        if (transitionDelay > 0) {
            handler.postDelayed(activityRunnable, transitionDelay);
        } else {
            handler.post(activityRunnable);
        }
    }

    private Runnable createActivityTransitionRunnable(final Builder builder) {
        return new Runnable() {
            @Override
            public void run() {
                executeActivityTransition(builder);
            }
        };
    }

    private void executeActivityTransition(Builder builder) {
        if (builder.animType.equals(AnimationType.NONE)) {
            builder.previousActivity.startActivity(builder.intent);
        } else {
            ActivityAnimationUtils.startActivityWithAnimation(builder.intent,
                    builder.previousActivity, builder.animType);
        }

        if (builder.closePreviousActivity) {
            builder.previousActivity.finish();
        }
    }

    /**
     * This class is used to build the activity transition and start the new activity.
     */
    public static class Builder {

        private AppCompatActivity previousActivity;
        private Intent intent;
        private AnimationType animType = AnimationType.NONE;
        private boolean closePreviousActivity = false;
        private int transitionDelay = 0;

        /**
         * @param previousActivity A reference to the calling activity.
         * @param cls The new class to transition to.
         * @param animType The type of animation that will occur when transitioning between activities.
         */
        public Builder(AppCompatActivity previousActivity, Class<?> cls, AnimationType animType) {
            this.previousActivity = previousActivity;
            this.intent = new Intent(previousActivity, cls);
            this.animType = animType;
        }

        /**
         * @param transitionDelay The time delay in milliseconds before navigating to the new activity.
         */
        public Builder transitionDelay(int transitionDelay) {
            this.transitionDelay = transitionDelay;
            return this;
        }

        /**
         * Calls {@link AppCompatActivity#finish()} on the previous activity after navigating to the new activity.
         */
        public Builder closePreviousActivity() {
            closePreviousActivity = true;
            return this;
        }

        /**
         * @param flags The new flags to set.
         */
        public Builder addFlags(int... flags) {
            for(int flag : flags) {
                intent.addFlags(flag);
            }
            return this;
        }

        /**
         * Clear any existing Activities from the stack. The new Activity becomes the root activity of the application.
         */
        public Builder clearStack() {
            addFlags(FLAG_ACTIVITY_CLEAR_TOP, FLAG_ACTIVITY_CLEAR_TASK, FLAG_ACTIVITY_NEW_TASK);
            return this;
        }

        /**
         * @param keyName The name of the extra data.
         * @param value The integer data value.
         */
        public Builder intExtra(String keyName, int value) {
            intent.putExtra(keyName, value);
            return this;
        }

        /**
         * @param keyName The name of the extra data.
         * @param value The String data value.
         */
        public Builder stringExtra(String keyName, String value) {
            intent.putExtra(keyName, value);
            return this;
        }

        /**
         * @param keyName The name of the extra data.
         * @param value The Serializable data value.
         */
        public Builder serializableExtra(String keyName, Serializable value) {
            intent.putExtra(keyName, value);
            return this;
        }

        /**
         * @param keyName The name of the extra data.
         * @param value The Parcelable data value.
         */
        public Builder parcelableExtra(String keyName, Parcelable value) {
            intent.putExtra(keyName, value);
            return this;
        }

        /**
         * @param keyName The name of the extra data.
         * @param customObject A custom object to be passed to the new activity.
         */
        public Builder objectExtra(String keyName, Object customObject) {
            String objectJson = new Gson().toJson(customObject);
            intent.putExtra(keyName, objectJson);
            return this;
        }

        /**
         * Starts the new Activity and performs the desired transition animation.
         */
        public void buildActivityAndTransition() {
            new ActivityAndAnimateManager(this);
        }
    }
}
