package com.rhinoactive.generalutilities;

import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Hunter Andrin on 2017-04-25.
 */

public class ViewEnabler {
    /**
     * Enables/disables the view and all of its child views.
     *
     * @param view The view to enable/disable.
     * @param enabled Whether or not to enable the view and all of its child views.
     */
    public static void enableDisableView(View view, boolean enabled) {
        view.setEnabled(enabled);
        if ( view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup)view;

            for ( int idx = 0 ; idx < group.getChildCount() ; idx++ ) {
                enableDisableView(group.getChildAt(idx), enabled);
            }
        }
    }
}
