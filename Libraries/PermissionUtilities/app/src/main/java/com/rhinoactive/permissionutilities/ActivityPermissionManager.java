package com.rhinoactive.permissionutilities;

import android.content.Context;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.rhinoactive.permissionutilities.dialogs.ActivityRationaleDialog;
import com.rhinoactive.permissionutilities.dialogs.RationaleDialog;
import com.rhinoactive.permissionutilities.models.Permission;

/**
 * Created by Hunter Andrin on 2017-06-29.
 */

/**
 * The activity should implement
 * {@link ActivityCompat.OnRequestPermissionsResultCallback}
 * to handle permit or denial of this permission request.
 */
public class ActivityPermissionManager extends PermissionManager {

    private AppCompatActivity activity;


    public ActivityPermissionManager(Permission requestedPermission, AppCompatActivity activity) {
        super(requestedPermission, activity.getFragmentManager());
        this.activity = activity;
    }

    @Override
    protected Context getContext() {
        return activity;
    }

    @Override
    protected void handleRequestPermission() {
        ActivityCompat.requestPermissions(activity, new String[]{requestedPermission.getPermissionName()}, requestedPermission.getRequestCode());
    }

    @Override
    protected RationaleDialog createRationaleDialog(Permission requestedPermission, String permissionRationale, String rationaleTitle) {
        return ActivityRationaleDialog.newInstance(requestedPermission, permissionRationale, rationaleTitle);
    }
}
