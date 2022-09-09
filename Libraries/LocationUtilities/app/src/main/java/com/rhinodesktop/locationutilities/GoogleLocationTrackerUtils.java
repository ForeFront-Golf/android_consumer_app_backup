package com.rhinodesktop.locationutilities;

/**
 * Created by rhinodesktop on 2017-01-26.
 */

public class GoogleLocationTrackerUtils {

    public static boolean isInstanceNull(GoogleLocationTracker googleLocationTracker) {
        return (googleLocationTracker == null || googleLocationTracker.getGoogleApiClient() == null);
    }

    public static void connectIfNotAlreadyStarted(GoogleLocationTracker googleLocationTracker) {
        if (!googleLocationTracker.getGoogleApiClient().isConnected()) {
            googleLocationTracker.connect();
        }
    }

    public static synchronized void disconnectGoogleApiClient(GoogleLocationTracker googleLocationTracker) {
        if (googleLocationTracker != null) {
            googleLocationTracker.disconnect();
        }
    }
}
