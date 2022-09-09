package com.rhinoactive.generalutilities;

/**
 * Created by Hunter Andrin on 2017-07-27.
 */

public class SeparateThreadRunner {

    /**
     * Executes a runnable on a separate thread
     *
     * @param runnable The runnable to execute
     */
    public static void runFromSeparateThread(Runnable runnable) {
        Thread thread = new Thread(runnable);
        thread.start();
    }
}
