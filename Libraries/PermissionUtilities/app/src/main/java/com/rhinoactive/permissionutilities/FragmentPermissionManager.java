package com.rhinoactive.permissionutilities;

import android.app.Fragment;
import android.content.Context;
import androidx.legacy.app.FragmentCompat;


import com.rhinoactive.permissionutilities.dialogs.FragmentRationaleDialog;
import com.rhinoactive.permissionutilities.dialogs.RationaleDialog;
import com.rhinoactive.permissionutilities.models.Permission;

/**
 * Created by Hunter Andrin on 2017-06-29.
 */

/**
  * The fragment should implement
  * {@link FragmentCompat.OnRequestPermissionsResultCallback}
  * to handle permit or denial of this permission request.
*/
public class FragmentPermissionManager extends PermissionManager {

    private Fragment fragment;

    public FragmentPermissionManager(Permission requestedPermission, Fragment fragment) {
        super(requestedPermission, fragment.getFragmentManager());
        this.fragment = fragment;
    }

    @Override
    protected Context getContext() {
        return fragment.getActivity();
    }

    @Override
    protected void handleRequestPermission() {
        FragmentCompat.requestPermissions(fragment, new String[]{requestedPermission.getPermissionName()}, requestedPermission.getRequestCode());
    }

    @Override
    protected RationaleDialog createRationaleDialog(Permission requestedPermission, String permissionRationale, String rationaleTitle) {
        return FragmentRationaleDialog.newInstance(requestedPermission, permissionRationale, rationaleTitle, fragment.getTag());
    }
}


