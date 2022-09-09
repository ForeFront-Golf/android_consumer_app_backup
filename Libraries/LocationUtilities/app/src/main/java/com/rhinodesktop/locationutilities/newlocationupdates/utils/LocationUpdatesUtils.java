package com.rhinodesktop.locationutilities.newlocationupdates.utils;

import android.content.Context;
import android.preference.PreferenceManager;

import static com.rhinodesktop.locationutilities.newlocationupdates.utils.Constants.KEY_IS_REQUESTING_UPDATES_IN_FOREGROUND;
import static com.rhinodesktop.locationutilities.newlocationupdates.utils.Constants.KEY_PREVIOUS_ORDER_STATUS;
import static com.rhinodesktop.locationutilities.newlocationupdates.utils.Constants.KEY_REQUESTING_LOCATION_UPDATES;
import static com.rhinodesktop.locationutilities.newlocationupdates.utils.Constants.KEY_START_LOCATION_UPDATE_SERVICE_IN_FOREGROUND;
import static com.rhinodesktop.locationutilities.newlocationupdates.utils.Constants.SHOULD_BIND_ANOTHER_SERVICE;

/**
 * Created by sungwook on 2018-03-19.
 */

public class LocationUpdatesUtils {

    public static boolean isRequestingUpdatesInForeground(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(KEY_IS_REQUESTING_UPDATES_IN_FOREGROUND, false);
    }

    public static void setRequestingUpdatesInForeground(Context context, boolean isRequestingLocationUpdatesInForeground) {
        saveBooleanToSharedPrefs(context, KEY_IS_REQUESTING_UPDATES_IN_FOREGROUND, isRequestingLocationUpdatesInForeground);
    }

    /**
     * Returns true if the location update service should run in the foreground,
     * otherwise returns false.
     *
     * @param context The {@link Context}.
     */
    public static boolean shouldStartLocationUpdateServiceInForeground(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(KEY_START_LOCATION_UPDATE_SERVICE_IN_FOREGROUND, false);
    }

    /**
     * @param startLocationUpdateServiceInForeground Determines whether the app should start the location
     *                                           service in the foreground.
     */
    public static void setRequestLocationUpdatesInForeground(Context context, boolean startLocationUpdateServiceInForeground) {
        saveBooleanToSharedPrefs(context, KEY_START_LOCATION_UPDATE_SERVICE_IN_FOREGROUND, startLocationUpdateServiceInForeground);
    }

    /**
     * Returns true if requesting location updates, otherwise returns false.
     *
     * @param context The {@link Context}.
     */
    public static boolean requestingLocationUpdates(Context context) {
        return getBooleanFromSharedPrefs(context, KEY_REQUESTING_LOCATION_UPDATES);
    }

    /**
     * Stores the location updates state in SharedPreferences.
     *
     * @param requestingLocationUpdates The location updates state.
     */
    public static void setRequestingLocationUpdates(Context context, boolean requestingLocationUpdates) {
        saveBooleanToSharedPrefs(context, KEY_REQUESTING_LOCATION_UPDATES, requestingLocationUpdates);
    }

    // TODO: docs
    // these are to check the previous status to determine if an update in frequency is required
    public static boolean getPreviousUpdateStatus(Context context) {
        return getBooleanFromSharedPrefs(context, KEY_PREVIOUS_ORDER_STATUS);
    }

    public static void setPreviousUpdateStatus(Context context, boolean previousUpdateStatus) {
        saveBooleanToSharedPrefs(context, KEY_PREVIOUS_ORDER_STATUS, previousUpdateStatus);
    }


    public static boolean shouldBindAnotherService(Context context) {
        return getBooleanFromSharedPrefs(context, SHOULD_BIND_ANOTHER_SERVICE);
    }

    public static void setShouldBindAnotherService(Context context, boolean temp) {
        saveBooleanToSharedPrefs(context, SHOULD_BIND_ANOTHER_SERVICE, temp);
    }

    private static boolean getBooleanFromSharedPrefs(Context context, String key) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(key, false);
    }

    private static void saveBooleanToSharedPrefs(Context context, String key, boolean startLocationUpdateServiceInForeground) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(key, startLocationUpdateServiceInForeground)
                .apply();
    }
}
