package com.rhinodesktop.foreorder_golf_consumer.activities;

import android.os.Bundle;


import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;


import com.rhinoactive.permissionutilities.ActivityPermissionManager;
import com.rhinodesktop.foreorder_golf_consumer.R;
import com.rhinodesktop.foreorder_golf_consumer.events.PermissionGrantedEvent;
import com.rhinodesktop.foreorder_golf_consumer.models.enums.TimeBetweenLocationUpdates;
import com.rhinodesktop.foreorder_golf_consumer.utils.ForeOrderDialogUtils;
import com.rhinodesktop.foreorder_golf_consumer.utils.ForeOrderResourceUtils;
import com.rhinoactive.generalutilities.GpsUtils;
import com.rhinoactive.permissionutilities.interfaces.PermissionRequestResultCallback;
import com.rhinoactive.permissionutilities.models.Permission;
import com.rhinodesktop.locationutilities.GoogleApiAvailabilityUtils;
import com.rhinodesktop.locationutilities.newlocationupdates.utils.ForegroundLocationServiceActivityBinderUtils;
import com.rhinodesktop.locationutilities.newlocationupdates.utils.LocationUpdatesUtils;
import com.rhinodesktop.locationutilities.newlocationupdates.utils.OrderStatusEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import lombok.NonNull;

/**
 * Created by Hunter Andrin on 2017-04-04.
 */

public abstract class TrackUserLocationActivity extends ToolbarActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    private ForegroundLocationServiceActivityBinderUtils utils;
    private ActivityPermissionManager permissionManager;
    private static final String LOCATION_PERMISSION_RATIONALE = ForeOrderResourceUtils.getInstance().strRes(R.string.permission_rationale_location_consumer);
    private static final String LOCATION_RATIONALE_TITLE = ForeOrderResourceUtils.getInstance().strRes(R.string.location_permission_rationale_title);
    private static final String LOCATION_PERMISSION_DENIED = ForeOrderResourceUtils.getInstance().strRes(R.string.location_permission_denied);
    private static final String LOCATION_PERMISSION_REQUIRED = ForeOrderResourceUtils.getInstance().strRes(R.string.permission_required);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LocationUpdatesUtils.setShouldBindAnotherService(this, true);
        utils = LocationUpdatesUtils.getPreviousUpdateStatus(this) ?
                new ForegroundLocationServiceActivityBinderUtils(this, TimeBetweenLocationUpdates.ORDER_PLACED.getTimeBetweenUpdates())
                : new ForegroundLocationServiceActivityBinderUtils(this, TimeBetweenLocationUpdates.AT_GOLF_COURSE.getTimeBetweenUpdates());
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        GoogleApiAvailabilityUtils.displayErrorIfGooglePlayServicesUnavailable(this);
        permissionManager = new ActivityPermissionManager(Permission.LOCATION_PERMISSION, this);
        boolean locationPermissionGranted = permissionManager.isPermissionGranted();
        if (locationPermissionGranted) {
            if (LocationUpdatesUtils.shouldBindAnotherService(this)) {
                connectLocationTracker();
            }
        } else {
            permissionManager.requestPermissionWithRationalDialog(LOCATION_PERMISSION_RATIONALE, LOCATION_RATIONALE_TITLE);
        }
        LocationUpdatesUtils.setShouldBindAnotherService(this, true);
    }

    @Override
    protected void onStop() {
        utils.unbindService();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionManager.handlePermissionResult(requestCode, permissions, grantResults, new PermissionRequestResultCallback() {
            @Override
            public void permissionGranted() {
                EventBus.getDefault().post(new PermissionGrantedEvent());
                startUserLocationUpdateService();
            }

            @Override
            public void permissionDenied() {
                permissionManager.displayPermissionDeniedDialog(true, LOCATION_PERMISSION_DENIED, LOCATION_PERMISSION_REQUIRED);
            }
        });
    }

    @Subscribe
    public void onOrderStatusEvent(OrderStatusEvent event) {
        utils.unbindService();
        if (event.isDoesOpenOrderExist()) {
            utils = new ForegroundLocationServiceActivityBinderUtils(this, TimeBetweenLocationUpdates.ORDER_PLACED.getTimeBetweenUpdates());
        } else {
            utils = new ForegroundLocationServiceActivityBinderUtils(this, TimeBetweenLocationUpdates.AT_GOLF_COURSE.getTimeBetweenUpdates());
        }
        startUserLocationUpdateService();
        LocationUpdatesUtils.setShouldBindAnotherService(this, false);
    }

    private void connectLocationTracker() {
        boolean isGpsEnabled = GpsUtils.isGpsEnabled(this);
        if (isGpsEnabled) {
            startUserLocationUpdateService();
        } else {
            GpsUtils.buildDialogForNoGps(this, ForeOrderDialogUtils.getInstance(), ForeOrderResourceUtils.getInstance().strRes(R.string.gps_not_enabled_content_consumer));
        }
    }

    private void startUserLocationUpdateService() {
        utils.bindService();
    }
}