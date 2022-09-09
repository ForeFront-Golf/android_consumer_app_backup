package com.rhinodesktop.activityanimatorutility.models.resultsrunners;



import android.app.Fragment;
import android.content.Intent;

import com.rhinodesktop.activityanimatorutility.activityutils.ActivityAnimationUtils;
import com.rhinodesktop.activityanimatorutility.models.AnimationType;

/**
 * Created by hunter on 2018-02-15.
 */

/**
 * Internal class used by this submodule.
 *
 * Used to delegate the start of an external activity with a result from a fragment.
 */
public class FragmentResultRunner extends ResultRunner {

    private Fragment fragment;

    public FragmentResultRunner(Fragment fragment) {
        this.fragment = fragment;
    }

    @Override
    public void runActivityForResult(Intent intent, int requestCode, AnimationType animType) {
        ActivityAnimationUtils.startActivityWithAnimationForResult(intent, requestCode, fragment, animType);
    }
}
