package com.rhinodesktop.locationutilities;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

/**
 * Created by sungwook on 2018-03-06.
 */

public class GoogleApiAvailabilityUtils {

    public static void displayErrorIfGooglePlayServicesUnavailable(AppCompatActivity activity) {
        int resultCode = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(activity);
        if (resultCode != ConnectionResult.SUCCESS) {
            int requestCode = 4652; // Request code for 'startActivityForResult()'
            GoogleApiAvailability.getInstance().showErrorDialogFragment(activity, resultCode, requestCode);
        }
    }
}
