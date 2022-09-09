package com.rhinoactive.permissionutilities.dialogs;

/**
 * Created by rhinodesktop on 2017-01-30.
 */

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import android.widget.Toast;

/**
 * A dialog that displays a permission denied message.
 */
public class PermissionDeniedDialog extends DialogFragment {

    private static final String ARGUMENT_FINISH_ACTIVITY = "finish";
    private static final String ARGUMENT_PERMISSION_DENIED = "denied";
    private static final String ARGUMENT_PERMISSION_REQUIRED = "required";

    private boolean mFinishActivity = false;
    private String permissionRequired = "";

    /**
     * Creates a new instance of this dialog and optionally finishes the calling Activity
     * when the 'Ok' button is clicked.
     */
    public static PermissionDeniedDialog newInstance(boolean finishActivity, String deniedMessage, String requiredMessage) {
        Bundle arguments = new Bundle();
        arguments.putBoolean(ARGUMENT_FINISH_ACTIVITY, finishActivity);
        arguments.putString(ARGUMENT_PERMISSION_DENIED, deniedMessage);
        arguments.putString(ARGUMENT_PERMISSION_REQUIRED, requiredMessage);
        PermissionDeniedDialog dialog = new PermissionDeniedDialog();
        dialog.setArguments(arguments);
        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mFinishActivity = getArguments().getBoolean(ARGUMENT_FINISH_ACTIVITY);
        String permissionDenied = getArguments().getString(ARGUMENT_PERMISSION_DENIED);
        permissionRequired = getArguments().getString(ARGUMENT_PERMISSION_REQUIRED);
        return new AlertDialog.Builder(getActivity())
                .setMessage(permissionDenied)
                .setPositiveButton(android.R.string.ok, null)
                .create();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (mFinishActivity) {
            Toast.makeText(getActivity(), permissionRequired, Toast.LENGTH_SHORT).show();
            getActivity().finish();
        }
    }
}