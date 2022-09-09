package com.rhinodesktop.locationutilities;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;

import timber.log.Timber;

/**
 * Created by rhinodesktop on 2017-01-24.
 */


// It is strongly recommended that the class that extends this class is a singleton. Doing so will
// prevent you from creating multiple instances of the same location tracker.
// You can use the helper methods in GoogleLocationTrackerUtils to help instantiate and manager
// those singletons.

public abstract class GoogleLocationTracker implements GoogleApiClient.ConnectionCallbacks {

    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    protected Context context;

    protected GoogleLocationTracker(Context context) {
        // getApplicationContext() is important, it keeps you from leaking the
        // Activity or BroadcastReceiver if someone passes one in.
        this.context = context.getApplicationContext();
        init();
    }

    public GoogleApiClient getGoogleApiClient() {
        return mGoogleApiClient;
    }

    @Override
    public void onConnectionSuspended(int i) {
        Timber.d("Location connection suspended.");
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        try {
            if (mGoogleApiClient != null) {
                Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                if (lastLocation == null) {
                    //a null location will be returned on a lack of permission
                    //OR when GPS hardware is disabled
                    if (ContextCompat.checkSelfPermission(context,
                            android.Manifest.permission.ACCESS_FINE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {
                        throw new SecurityException();
                    }
                    return;
                }
                updateUserLocations(lastLocation);

                LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest);
                builder.setAlwaysShow(true); //Suppresses the "never" option on location permission dialog
                final PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());

                final LocationListener locationListener = createLocationListener();
                result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                    @Override
                    public void onResult(@NonNull LocationSettingsResult locationSettingsResult) throws SecurityException {
                        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, locationListener);
                    }
                });
            }
        } catch (SecurityException ex) {
            userMissingLocationPermission(ex);
            disconnect();
        } catch (Exception ex) {
            Timber.e("Error while connecting to location services.");
            ex.printStackTrace();
            disconnect();
        }
    }

    protected void connect() {
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    protected void disconnect() {
        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }
    }

    private void init() {
        // Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null) {
            GoogleLocationConnectionFailedListener googleLocationConnectionFailedListener = new GoogleLocationConnectionFailedListener(context);
            mGoogleApiClient = new GoogleApiClient.Builder(context)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(googleLocationConnectionFailedListener)
                    .addApi(LocationServices.API)
                    .build();
            long timeBetweenUpdatesInMilliseconds = getTimeBetweenUpdatesInMilliseconds();
            mLocationRequest = new LocationRequest()
                    .setInterval(timeBetweenUpdatesInMilliseconds)
                    .setFastestInterval(timeBetweenUpdatesInMilliseconds)
                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        }
    }

    protected abstract long getTimeBetweenUpdatesInMilliseconds();

    protected abstract void  userMissingLocationPermission(SecurityException ex);

    protected abstract void updateUserLocations(Location location);

    protected abstract LocationListener createLocationListener();
}
