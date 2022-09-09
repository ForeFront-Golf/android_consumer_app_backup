package com.rhinoactive.imageutility.models;

import android.content.Context;

import com.rhinoactive.imageutility.Constants;

/**
 * Created by hunter on 2018-02-23.
 */

/**
 * Internal library class
 *
 * Wrapper class for a toast message and context
 */
public class ToastContextWrapper {

    private Context context;

    private String toastMessage;

    public ToastContextWrapper(Context context) {
        this.context = context;
        this.toastMessage = Constants.DOWNLOADING;
    }

    public ToastContextWrapper(Context context, String toastMessage) {
        this.context = context;
        this.toastMessage = toastMessage;
    }

    public Context getContext() {
        return context;
    }

    public String getToastMessage() {
        return toastMessage;
    }
}
