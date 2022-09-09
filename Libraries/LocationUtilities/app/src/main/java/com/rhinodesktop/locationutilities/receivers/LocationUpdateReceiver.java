package com.rhinodesktop.locationutilities.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.rhinodesktop.locationutilities.services.LocationUpdateService;

import timber.log.Timber;

/**
 * Created by rhinodesktop on 2017-01-24.
 */

public abstract class LocationUpdateReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, final Intent intent) {
        Timber.d("Location update broadcast received.");
        boolean isConnected = hasNetworkConnection(context);
        if (isConnected) {
            Timber.d("Location update receiver connected.");
            Intent serviceIntent = new Intent(context, getServiceImplementationClass());
            context.startService(serviceIntent);
        } else {
            Intent serviceIntent = new Intent(context, getServiceImplementationClass());
            context.stopService(serviceIntent);
        }
    }

    public static boolean hasNetworkConnection(Context context) {
        boolean hasConnection = true;
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            hasConnection = (activeNetwork != null && activeNetwork.isConnected());
        }
        return hasConnection;
    }

    protected abstract Class<? extends LocationUpdateService> getServiceImplementationClass();
}
