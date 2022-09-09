package com.rhinodesktop.activityanimatorutility.models.resultsrunners;

import android.app.Activity;
import android.content.Intent;

import com.rhinodesktop.activityanimatorutility.models.AnimationType;

/**
 * Created by hunter on 2018-02-15.
 */

/**
 * Internal class used by this submodule.
 *
 * Used to delegate the start of an external activity with a result from either an activity,
 * a fragment, or a support fragment.
 */
public abstract class ResultRunner {

    public abstract void runActivityForResult(Intent intent, int requestCode, AnimationType animType);
}
