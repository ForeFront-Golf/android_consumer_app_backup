package com.rhinoactive.permissionutilities.dialogs;

import androidx.legacy.app.ActivityCompat;

import com.rhinoactive.permissionutilities.models.Permission;

/**
 * Created by Hunter Andrin on 2017-10-03.
 */

public class ActivityRationaleDialog extends RationaleDialog {

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
    public static RationaleDialog newInstance(Permission requestedPermission, String permissionRational, String rationaleTitle) {
        RationaleDialog dialog = new ActivityRationaleDialog();
        dialog.setArguments(createBundle(requestedPermission, permissionRational, rationaleTitle));
        return dialog;
    }

    @Override
    protected void requestPermission() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{requestedPermission.getPermissionName()}, requestedPermission.getRequestCode());
    }
}
