package com.rhinoactive.permissionutilities.dialogs;

import android.app.Fragment;
import android.os.Bundle;


import androidx.legacy.app.FragmentCompat;

import com.rhinoactive.permissionutilities.models.Permission;

/**
 * Created by Hunter Andrin on 2017-10-03.
 */

public class FragmentRationaleDialog extends RationaleDialog {

    private static final String ARGUMENT_FRAGMENT_TAG = "FragmentTag";

    /**
     * Creates a new instance of a dialog displaying the rationale for the use of the requested
     * permission.
     * <p>
     * The permission is requested after clicking 'ok'.
     *
     * @param requestedPermission    The permission being requested.
     *
     * @param permissionRational    The message to be displayed when requesting the permission.
     *
     */
    public static RationaleDialog newInstance(Permission requestedPermission, String permissionRational, String rationaleTitle, String fragmentTag) {
        RationaleDialog dialog = new FragmentRationaleDialog();
        Bundle bundle = createBundle(requestedPermission, permissionRational, rationaleTitle);
        bundle.putString(ARGUMENT_FRAGMENT_TAG, fragmentTag);
        dialog.setArguments(bundle);
        return dialog;
    }

    @Override
    protected void requestPermission() {
        Bundle arguments = getArguments();
        String containingFragmentTag = arguments.getString(ARGUMENT_FRAGMENT_TAG);
        // Try to use the containing fragment so that its onRequestPermissionsResult can receive the result.
        // If the containing fragment has been dismissed for whatever reason then use this fragment.
        Fragment fragment = getActivity().getFragmentManager().findFragmentByTag(containingFragmentTag);
        if (fragment == null) {
            fragment = this;
        }
        FragmentCompat.requestPermissions(fragment, new String[]{requestedPermission.getPermissionName()}, requestedPermission.getRequestCode());
    }
}
