package com.rhinodesktop.locationutilities.newlocationupdates.services;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.location.Location;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import androidx.core.app.NotificationCompat;

import com.rhinodesktop.locationutilities.R;
import com.rhinodesktop.locationutilities.newlocationupdates.models.ForegroundNotification;
import com.rhinodesktop.locationutilities.newlocationupdates.utils.Constants;
import com.rhinodesktop.locationutilities.newlocationupdates.utils.LocationUpdatesUtils;

import static com.rhinodesktop.locationutilities.newlocationupdates.utils.Constants.CHANNEL_ID;
import static com.rhinodesktop.locationutilities.newlocationupdates.utils.Constants.NOTIFICATION_ID;
import static com.rhinodesktop.locationutilities.newlocationupdates.utils.Constants.START_FROM_NOTIFICATION;

/**
 * Created by sungwook on 2018-03-19.
 */

public class UpdateLocationForegroundService extends UpdateLocationService {

    private final IBinder mBinder = new LocalBinder();

    private NotificationManager mNotificationManager;
    private Class mActivityClass;
    private boolean mChangingConfiguration = false;

    @Override
    public void onCreate() {
        super.onCreate();

        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // Android O requires a Notification Channel - MUST.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.app_name);
            // Create the channel for the notification
            NotificationChannel mChannel =
                    new NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_DEFAULT);

            // Set the Notification Channel for the Notification Manager.
            mNotificationManager.createNotificationChannel(mChannel);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        boolean startedFromNotification = intent.getBooleanExtra(START_FROM_NOTIFICATION,
                false);

        // We got here because the user decided to remove location updates from the notification.
        if (startedFromNotification) {
            stopSelf();
        }
        return START_NOT_STICKY;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mChangingConfiguration = true;
    }

    @SuppressWarnings("unchecked")
    @Override
    public IBinder onBind(Intent intent) {
        stopForegroundService();
        mChangingConfiguration = false;
        assert intent.getExtras() != null;
        mActivityClass = (Class<Activity>) intent.getExtras().getSerializable(Constants.ACTIVITY_CLASS_SERIALIZABLE);
        return mBinder;
    }

    @Override
    public void onRebind(Intent intent) {
        stopForegroundService();
        mChangingConfiguration = false;
        super.onRebind(intent);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        if (!mChangingConfiguration
                && LocationUpdatesUtils.requestingLocationUpdates(this)
                && LocationUpdatesUtils.shouldStartLocationUpdateServiceInForeground(this)) {
            LocationUpdatesUtils.setRequestingUpdatesInForeground(this, true);
            startForeground(NOTIFICATION_ID, createForegroundNotification());
        }
        return true; // Returning true ensures onRebind() is called when a client re-binds.
    }

    public class LocalBinder extends Binder {
        public UpdateLocationForegroundService getService() {
            return UpdateLocationForegroundService.this;
        }
    }

    public void requestLocationUpdates(int updateInterval) {
        startService(new Intent(getApplicationContext(), UpdateLocationForegroundService.class));
        super.requestLocationUpdates(updateInterval);
    }

    public static void stopService(Context context) {
        LocationUpdatesUtils.setRequestLocationUpdatesInForeground(context, false);
        Intent intent = new Intent(context, UpdateLocationForegroundService.class);
        context.stopService(intent);
    }

    protected void onNewLocation(Location location) {
        super.onNewLocation(location);

        // Update notification content if running as a foreground service.
        if (serviceIsRunningInForeground(this)) {
            mNotificationManager.notify(NOTIFICATION_ID, createForegroundNotification());
        }

    }

    private void stopForegroundService() {
        stopForeground(true);
        LocationUpdatesUtils.setRequestingUpdatesInForeground(this, false);
    }

    private Notification createForegroundNotification() {
        Intent intent = new Intent(getApplicationContext(), UpdateLocationForegroundService.class);
        // Extra to help us figure out if we arrived in onStartCommand via the notification or not.
        intent.putExtra(START_FROM_NOTIFICATION, true);

        // The PendingIntent that leads to a call to onStartCommand() in this service.
        PendingIntent servicePendingIntent = PendingIntent.getService(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        // The PendingIntent to launch activity.
        PendingIntent activityPendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, mActivityClass), 0);

        return buildNotification(servicePendingIntent, activityPendingIntent);
    }

    private Notification buildNotification(PendingIntent servicePendingIntent, PendingIntent activityPendingIntent) {
        ForegroundNotification foregroundNotification = ForegroundNotification.getInstance();
        CharSequence notificationText = foregroundNotification.getNotificationText();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .addAction(foregroundNotification.getLaunchIcon(), foregroundNotification.getLaunchButtonText(),
                        activityPendingIntent)
                .addAction(foregroundNotification.getRemoveIcon(), foregroundNotification.getRemoveButtonText(),
                        servicePendingIntent)
                .setContentText(notificationText)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(notificationText))
                .setContentTitle(foregroundNotification.getNotificationTitle())
                .setOngoing(true)
                .setPriority(Notification.PRIORITY_HIGH)
                .setSmallIcon(foregroundNotification.getNotificationIcon())
                .setTicker(notificationText)
                .setWhen(System.currentTimeMillis());

        return builder.build();
    }

    private boolean serviceIsRunningInForeground(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(
                Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(
                Integer.MAX_VALUE)) {
            if (getClass().getName().equals(service.service.getClassName())) {
                if (service.foreground) {
                    return true;
                }
            }
        }
        return false;
    }
}
