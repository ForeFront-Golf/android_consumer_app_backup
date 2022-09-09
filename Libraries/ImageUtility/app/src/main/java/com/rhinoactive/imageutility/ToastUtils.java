package com.rhinoactive.imageutility;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

/**
 * Created by Hunter Andrin on 2017-10-26.
 */

/**
 * Internal library class
 *
 * Displays various toast messages
 */
public class ToastUtils {

    public static void displayToastFromMainThreadLong(final Context context, final String toastText) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run () {
                Toast.makeText(context, toastText, Toast.LENGTH_LONG).show();
            }
        });
    }

    public static void displayToastFromMainThreadShort(final Context context, final String toastText) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run () {
                Toast.makeText(context, toastText, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
