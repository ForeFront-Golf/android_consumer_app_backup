package com.rhinodesktop.locationutilities.newlocationupdates.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.rhinodesktop.locationutilities.newlocationupdates.services.UpdateLocationService;

/**
 * Created by hunter on 2018-03-22.
 */

public class LocationServiceUtils {

    private static LocationServiceUtils locationServiceUtils;

    private LocationServiceUtils() {}

    public static LocationServiceUtils getInstance() {
        if (locationServiceUtils == null) {
            locationServiceUtils = new LocationServiceUtils();
        }
        return locationServiceUtils;
    }

    /**
     * Starts the user location tracking service.
     *
     * @param context A reference to a context.
     * @param updateIntervalInMilliseconds Interval of the location updates.
     */
    public void startService(Context context, int updateIntervalInMilliseconds) {
        Intent intent = createLocationUpdateIntent(context);
        intent.putExtra(Constants.KEY_UPDATE_LOCATION_INTERVAL, updateIntervalInMilliseconds);
        context.startService(intent);
    }

    public void stopService(Context context) {
        Intent intent = createLocationUpdateIntent(context);
        context.stopService(intent);
    }

    private Intent createLocationUpdateIntent(Context context) {
        return new Intent(context, UpdateLocationService.class);
    }
}
