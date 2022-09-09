package com.rhinoactive.permissionutilities.models;

import android.Manifest;

/**
 * Created by rhinodesktop on 2017-01-31.
 */

public enum Permission {
    LOCATION_PERMISSION (Manifest.permission.ACCESS_FINE_LOCATION, 1),
    READ_EXTERNAL_STORAGE_PERMISSION (Manifest.permission.READ_EXTERNAL_STORAGE, 2),
    WRITE_EXTERNAL_STORAGE_PERMISSION (Manifest.permission.WRITE_EXTERNAL_STORAGE, 3);

    private final String permissionName;
    private final int requestCode;

    Permission(String permissionName, int requestCode) {
        this.permissionName = permissionName;
        this.requestCode = requestCode;
    }

    public String getPermissionName() {
        return permissionName;
    }

    public int getRequestCode() {
        return requestCode;
    }
}
