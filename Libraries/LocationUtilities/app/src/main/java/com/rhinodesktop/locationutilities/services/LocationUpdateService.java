package com.rhinodesktop.locationutilities.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import androidx.annotation.Nullable;

/**
 * Created by rhinodesktop on 2017-01-24.
 */

public abstract class LocationUpdateService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        if (isUserSignedIn()) {
            startLocationUpdates();
        }
    }

    protected void startLocationUpdates() {
        final Context context = this;
        startTracking(context);
    }

    protected abstract boolean isUserSignedIn();

    protected abstract void startTracking(Context context);
}
