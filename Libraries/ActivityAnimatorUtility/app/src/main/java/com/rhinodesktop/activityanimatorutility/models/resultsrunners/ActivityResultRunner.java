package com.rhinodesktop.activityanimatorutility.models.resultsrunners;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

import com.rhinodesktop.activityanimatorutility.activityutils.ActivityAnimationUtils;
import com.rhinodesktop.activityanimatorutility.models.AnimationType;

/**
 * Created by hunter on 2018-02-15.
 */

/**
 * Internal class used by this submodule.
 *
 * Used to delegate the start of an external activity with a result from an activity.
 */
public class ActivityResultRunner extends ResultRunner {

    private AppCompatActivity activity;

    public ActivityResultRunner(AppCompatActivity activity) {
        this.activity = activity;
    }

    @Override
    public void runActivityForResult(Intent intent, int requestCode, AnimationType animType) {
        ActivityAnimationUtils.startActivityWithAnimationForResult(intent, requestCode, activity, animType);
    }
}
