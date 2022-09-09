package com.rhinoactive.permissionutilities.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rhinoactive.permissionutilities.models.Permission;

/**
 * Created by rhinodesktop on 2017-01-30.
 */

/**
 * A dialog that explains the use of the location permission and requests the necessary
 * permission.
 */

public abstract class RationaleDialog extends android.app.DialogFragment {
    private static final String ARGUMENT_REQUESTED_PERMISSION = "requestedPermission";
    private static final String ARGUMENT_RATIONAL_TITLE = "dialogTitle";
    private static final String ARGUMENT_PERMISSION_RATIONAL = "permissionRational";

    protected Permission requestedPermission;

    protected static Bundle createBundle(Permission requestedPermission, String permissionRational, String rationaleTitle) {
        Bundle arguments = new Bundle();
        arguments.putString(ARGUMENT_REQUESTED_PERMISSION, (new Gson()).toJson(requestedPermission));
        arguments.putString(ARGUMENT_PERMISSION_RATIONAL, permissionRational);
        arguments.putString(ARGUMENT_RATIONAL_TITLE, rationaleTitle);
        return arguments;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        final String requestedPermissionJson = arguments.getString(ARGUMENT_REQUESTED_PERMISSION);
        GsonBuilder builder = new GsonBuilder();
        requestedPermission = builder.create().fromJson(requestedPermissionJson, Permission.class);
        final String permissionRational = arguments.getString(ARGUMENT_PERMISSION_RATIONAL);
        final String rationalTitle = arguments.getString(ARGUMENT_RATIONAL_TITLE);
        return new AlertDialog.Builder(getActivity())
                .setTitle(rationalTitle)
                .setMessage(permissionRational)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onCloseDialog();
                    }
                })
                .create();
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        onCloseDialog();
    }

    private void onCloseDialog() {
        // Once the user closes the dialog, request the permission.
        requestPermission();
    }

    protected abstract void requestPermission();
}