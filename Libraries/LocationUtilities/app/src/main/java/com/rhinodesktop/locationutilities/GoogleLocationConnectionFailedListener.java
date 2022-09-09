package com.rhinodesktop.locationutilities;

import android.content.Context;
import androidx.annotation.NonNull;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import timber.log.Timber;

/**
 * Created by rhinodesktop on 2017-01-24.
 */

public class GoogleLocationConnectionFailedListener implements GoogleApiClient.OnConnectionFailedListener {

    private Context context;

    public GoogleLocationConnectionFailedListener(Context context) {
        this.context = context;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Timber.e("Failed to connect to google location services.");
    }
}
