package com.rhinoactive.generalutilities;

import android.os.Handler;
import android.os.Looper;

/**
 * Created by rhinodesktop on 2017-03-23.
 */

public class MainThreadRunner {

    /**
     * Executes a runnable on the main thread
     *
     * @param runnable The runnable to execute
     */
    public static void runFromUiThread(Runnable runnable) {
        Handler mainHandler = new Handler(Looper.getMainLooper());
        mainHandler.post(runnable);
    }

    /**
     * Executes a runnable on the main thread with a delay
     *
     * @param runnable The runnable to execute
     * @param delayMillis The time in milliseconds to wait before the runnable gets executed
     */
    public static void runFromUiThreadWithDelay(Runnable runnable, long delayMillis) {
        Handler mainHandler = new Handler(Looper.getMainLooper());
        mainHandler.postDelayed(runnable, delayMillis);
    }
}
