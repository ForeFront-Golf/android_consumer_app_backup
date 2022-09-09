package com.rhinoactive.imageutility;

/**
 * Created by hunter on 2018-03-14.
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
