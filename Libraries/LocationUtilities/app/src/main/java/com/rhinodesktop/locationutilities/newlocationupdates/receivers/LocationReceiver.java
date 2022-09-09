package com.rhinodesktop.locationutilities.newlocationupdates.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;

import com.rhinodesktop.locationutilities.newlocationupdates.utils.Constants;

/**
 * Created by hunter on 2018-03-22.
 */

public abstract class LocationReceiver extends BroadcastReceiver {

    protected abstract void handleNewLocation(Location location);

    @Override
    public void onReceive(Context context, Intent intent) {
        Location location = intent.getParcelableExtra(Constants.EXTRA_LOCATION);
        if (location != null) {
            handleNewLocation(location);
        }
    }
}