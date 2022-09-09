package com.rhinoactive.generalutilities;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

/**
 * Created by rhinodesktop on 2017-02-02.
 */

/**
 * Provides utility methods to display a toast message from any class.
 *
 * To use, extend this class and return an instance of the app content
 * in the {@link #getAppContext()} method.
 */
public abstract class ToastUtils {

    /**
     * Displays a toast message for long amount of time.
     *
     * @param toastText The test to display in the toast.
     */
    public void displayToastFromMainThreadLong(final String toastText) {
        displayToastFromMainThread(toastText, Toast.LENGTH_LONG);
    }

    /**
     * Displays a toast message for short amount of time.
     *
     * @param toastText The test to display in the toast.
     */
    public void displayToastFromMainThreadShort(final String toastText) {
        displayToastFromMainThread(toastText, Toast.LENGTH_SHORT);
    }

    private void displayToastFromMainThread(final String toastText, final int toastLength) {
        final Context context = getAppContext();
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run () {
                Toast.makeText(context, toastText, toastLength).show();
            }
        });
    }

    /**
     *
     * @return A reference to the app context.
     */
    protected abstract Context getAppContext();
}
