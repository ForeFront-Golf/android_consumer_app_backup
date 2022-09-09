package com.rhinodesktop.locationutilities.newlocationupdates.utils;

import com.rhinodesktop.locationutilities.BuildConfig;

/**
 * Created by sungwook on 2018-03-21.
 */

public class Constants {

    private static final String PACKAGE_NAME = BuildConfig.LIBRARY_PACKAGE_NAME;

    public static final String KEY_UPDATE_LOCATION_INTERVAL = "update_location_interval";
    public static final String KEY_REQUESTING_LOCATION_UPDATES = "requesting_locaction_updates";
    public static final String KEY_START_LOCATION_UPDATE_SERVICE_IN_FOREGROUND = "start_location_update_service_in_foreground";
    public static final String KEY_IS_REQUESTING_UPDATES_IN_FOREGROUND = "is_requesting_updates_in_foreground";
    public static final String ACTION_NEW_LOCATION = PACKAGE_NAME + ".new.location.broadcast";
    public static final String EXTRA_LOCATION = PACKAGE_NAME + ".location";
    public static final String TAG = "UpdateLocationForegroundService";
    public static final String CHANNEL_ID = "channel_01";
    public static final String START_FROM_NOTIFICATION = PACKAGE_NAME + ".started_from_notification";
    public static final int NOTIFICATION_ID = 12345678;
    public static final String ACTIVITY_CLASS_SERIALIZABLE = "activity_class_serializable";
    public static final String KEY_PREVIOUS_ORDER_STATUS = "previous_order_status";
    public static final String SHOULD_BIND_ANOTHER_SERVICE = "should_bind_another_service";
}
