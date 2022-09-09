package com.rhinodesktop.locationutilities.newlocationupdates.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import androidx.appcompat.app.AppCompatActivity;

import com.rhinodesktop.locationutilities.newlocationupdates.services.UpdateLocationForegroundService;

/**
 * Created by hunter on 2018-03-22.
 */

public class ForegroundLocationServiceActivityBinderUtils {

    private Context context;

    // A reference to the service used to get location updates.
    private UpdateLocationForegroundService service;

    // Tracks the bound state of the service.
    private boolean mBound = false;

    //Interval of the location updates.
    private int updateIntervalInMilliseconds;

    // Monitors the state of the connection to the service.
    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            UpdateLocationForegroundService.LocalBinder binder = (UpdateLocationForegroundService.LocalBinder) service;
            ForegroundLocationServiceActivityBinderUtils.this.service = binder.getService();
            ForegroundLocationServiceActivityBinderUtils.this.service.requestLocationUpdates(updateIntervalInMilliseconds);
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            ForegroundLocationServiceActivityBinderUtils.this.service = null;
            mBound = false;
        }
    };


    /**
     * Initialize this in the {@link AppCompatActivity#onCreate(Bundle)} of your location tracking activity.
     *
     * @param context                      A reference to the location tracking context.
     * @param updateIntervalInMilliseconds Interval of the location updates.
     */
    public ForegroundLocationServiceActivityBinderUtils(Context context, int updateIntervalInMilliseconds) {
        this.context = context;
        this.updateIntervalInMilliseconds = updateIntervalInMilliseconds;

    }

    public void bindService() {
        // Bind to the service. If the service is in foreground mode, this signals to the service
        // that since this activity is in the foreground, the service can exit foreground mode.
        Intent intent = new Intent(context, UpdateLocationForegroundService.class);
        intent.putExtra(Constants.ACTIVITY_CLASS_SERIALIZABLE, context.getClass());
        context.bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    public void unbindService() {
        if (mBound) {
            // Unbind from the service. This signals to the service that this activity is no longer
            // in the foreground, and the service can respond by promoting itself to a foreground
            // service.
            context.unbindService(mServiceConnection);
            mBound = false;
        }
    }

}
