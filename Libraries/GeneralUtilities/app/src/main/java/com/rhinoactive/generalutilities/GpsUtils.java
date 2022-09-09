package com.rhinoactive.generalutilities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.location.LocationManager;
import android.provider.Settings;
import androidx.appcompat.app.AppCompatActivity;

import androidx.annotation.NonNull;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.rhinoactive.generalutilities.dialogutils.DialogUtils;

/**
 * Created by rhinodesktop on 2017-01-27.
 */

public class GpsUtils {

    /**
     *
     * @param activity A reference to an activity
     * @return Whether or not the user's device has its gps enabled
     */
    public static boolean isGpsEnabled(Activity activity) {
        boolean gpsEnabled;
        final LocationManager manager = (LocationManager) activity.getSystemService( Context.LOCATION_SERVICE );
        boolean providerEnabled = manager.isProviderEnabled( LocationManager.GPS_PROVIDER );
        boolean batterySavingModeEnable;
        try {
            int locationMode = Settings.Secure.getInt(activity.getContentResolver(), Settings.Secure.LOCATION_MODE);
            batterySavingModeEnable = locationMode == Settings.Secure.LOCATION_MODE_BATTERY_SAVING;
        } catch (Settings.SettingNotFoundException ex) {
            batterySavingModeEnable = false;
        }
        gpsEnabled = providerEnabled || batterySavingModeEnable;
        return gpsEnabled;
    }

    /**
     *
     * @param activity A reference to an activity.
     * @param dialogUtils A dialogUtils object that will create a dialog prompting the user to enable their gps.
     * @param contentText The message used to inform users that they need to enable their gps.
     */
    public static void buildDialogForNoGps(final AppCompatActivity activity, DialogUtils dialogUtils, String contentText) {
        Resources resources = activity.getResources();
        dialogUtils.buildDialog(activity, resources.getString( R.string.gps_not_enabled_title ))
                .content(contentText)
                .negativeText(R.string.cancel)
                .positiveText(R.string.open_settings)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        activity.startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .show();
    }
}
