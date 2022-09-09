package com.rhinodesktop.locationutilities.newlocationupdates.services;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import androidx.annotation.Nullable;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.rhinodesktop.locationutilities.newlocationupdates.utils.Constants;
import com.rhinodesktop.locationutilities.newlocationupdates.utils.LocationUpdatesUtils;

import java.util.Locale;

import static com.rhinodesktop.locationutilities.newlocationupdates.utils.Constants.EXTRA_LOCATION;
import static com.rhinodesktop.locationutilities.newlocationupdates.utils.Constants.TAG;

/**
 * Created by sungwook on 2018-03-22.
 */

public class UpdateLocationService extends Service {

    public FusedLocationProviderClient mFusedLocationProviderClient;
    private LocationRequest mLocationRequest;
    public LocationCallback mLocationCallback;
    private Handler mServiceHandler;

    @Override
    public void onCreate() {
        setRequestingLocationUpdatesInSharedPrefs(false);
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                onNewLocation(locationResult.getLastLocation());
            }
        };

        HandlerThread handlerThread = new HandlerThread(TAG);
        handlerThread.start();
        mServiceHandler = new Handler(handlerThread.getLooper());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!LocationUpdatesUtils.requestingLocationUpdates(this)) {
            int updateInterval = intent.getIntExtra(Constants.KEY_UPDATE_LOCATION_INTERVAL,
                    LocationUpdatesUtils.getPreviousUpdateStatus(this) ? 5000 : 30000);

            requestLocationUpdates(updateInterval);
        }
        // Tells the system to not try to recreate the service after it has been killed.
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        removeLocationUpdates();
        mServiceHandler.removeCallbacksAndMessages(null);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void requestLocationUpdates(int updateInterval) {
        createLocationRequest(updateInterval);
        setRequestingLocationUpdatesInSharedPrefs(true);
        try {
            mFusedLocationProviderClient.requestLocationUpdates(mLocationRequest,
                    mLocationCallback, Looper.myLooper());
        } catch (SecurityException unlikely) {
            setRequestingLocationUpdatesInSharedPrefs(false);
        }
    }

    public void removeLocationUpdates() {
        try {
            mFusedLocationProviderClient.removeLocationUpdates(mLocationCallback);
            setRequestingLocationUpdatesInSharedPrefs(false);
            stopSelf();
        } catch (SecurityException unlikely) {
            setRequestingLocationUpdatesInSharedPrefs(true);
        }
    }

    protected void onNewLocation(Location location) {
        // Notify anyone listening for broadcasts about the new location.
        String intentName = String.format(Locale.ENGLISH, "%s%s", getPackageName(), ".NEW_LOCATION_INTENT");
        Intent intent = new Intent(intentName);
        intent.putExtra(EXTRA_LOCATION, location);
        sendBroadcast(intent);
    }

    private void createLocationRequest(int updateInterval) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(updateInterval);
        mLocationRequest.setFastestInterval(updateInterval);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void setRequestingLocationUpdatesInSharedPrefs(boolean requestingLocationUpdates) {
        LocationUpdatesUtils.setRequestingLocationUpdates(this, requestingLocationUpdates);
    }
}
