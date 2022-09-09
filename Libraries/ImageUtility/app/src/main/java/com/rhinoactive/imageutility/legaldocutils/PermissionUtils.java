package com.rhinoactive.imageutility.legaldocutils;

import android.app.Activity;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

/**
 * Created by hunter on 2017-09-12.
 */

public class PermissionUtils {

    private Activity activity;
    private String permissionName;
    private int requestCode;

    public PermissionUtils(Activity activity, String permissionName, int requestCode) {
        this.activity = activity;
        this.permissionName = permissionName;
        this.requestCode = requestCode;
    }

    public boolean confirmPermissionGranted() {
        boolean permissionGranted = isPermissionGranted();
        if (!permissionGranted) {
            requestPermission();
        }
        return permissionGranted;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(activity, new String[]
                {permissionName}, requestCode);
    }

    private boolean isPermissionGranted() {
        return ContextCompat.checkSelfPermission(activity, permissionName) == PackageManager.PERMISSION_GRANTED;
    }
}
