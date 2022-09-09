package com.rhinoactive.permissionutilities;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.pm.PackageManager;
import androidx.annotation.NonNull;
import androidx.legacy.app.ActivityCompat;
import androidx.legacy.app.FragmentCompat;


import com.rhinoactive.permissionutilities.dialogs.PermissionDeniedDialog;
import com.rhinoactive.permissionutilities.dialogs.RationaleDialog;
import com.rhinoactive.permissionutilities.interfaces.PermissionRequestResultCallback;
import com.rhinoactive.permissionutilities.models.Permission;

/**
 * Created by rhinodesktop on 2017-01-27.
 */

public abstract class PermissionManager {

    protected Permission requestedPermission;
    private FragmentManager fragmentManager;

    public PermissionManager(Permission requestedPermission, FragmentManager fragmentManager) {
        this.requestedPermission = requestedPermission;
        this.fragmentManager = fragmentManager;
    }



    public void requestPermission() {
        handleRequestPermission();
    }

    public void requestPermissionWithRationalDialog(String permissionRationale, String rationaleTitle) {
        RationaleDialog tmp = createRationaleDialog(requestedPermission, permissionRationale, rationaleTitle);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(tmp, "dialog");
        transaction.commitAllowingStateLoss();
    }

    public void displayPermissionDeniedDialog(boolean finishActivity, String deniedMessage, String requiredMessage) {
        PermissionDeniedDialog tmp = PermissionDeniedDialog.newInstance(finishActivity, deniedMessage, requiredMessage);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(tmp, "dialog");
        transaction.commitAllowingStateLoss();
    }

    public boolean isPermissionGranted() {
        return ActivityCompat.checkSelfPermission(getContext(), requestedPermission.getPermissionName()) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Call this method in the
     * {@link ActivityCompat.OnRequestPermissionsResultCallback}.
     * or in the
     * {@link FragmentCompat.OnRequestPermissionsResultCallback}
     */
    public void handlePermissionResult(int requestCode, @NonNull String[] grantedPermissions, @NonNull int[] grantResults,
                                       PermissionRequestResultCallback callback) {
        if (requestCode != requestedPermission.getRequestCode()) {
            return;
        }
        if (isPermissionGranted(grantedPermissions, grantResults, requestedPermission.getPermissionName())) {
            callback.permissionGranted();
        } else {
            callback.permissionDenied();
        }
    }

    /**
     * Checks if the result contains a {@link PackageManager#PERMISSION_GRANTED} result for a
     * permission from a runtime permissions request.
     *
     * @see ActivityCompat.OnRequestPermissionsResultCallback
     */
    private boolean isPermissionGranted(String[] grantedPermissions, int[] grantResults,
                                              String permission) {
        for (int i = 0; i < grantedPermissions.length; i++) {
            if (permission.equals(grantedPermissions[i])) {
                return grantResults[i] == PackageManager.PERMISSION_GRANTED;
            }
        }
        return false;
    }

    protected abstract Context getContext();

    protected abstract void handleRequestPermission();

    protected abstract RationaleDialog createRationaleDialog(Permission requestedPermission, String permissionRationale, String rationaleTitle);
}
