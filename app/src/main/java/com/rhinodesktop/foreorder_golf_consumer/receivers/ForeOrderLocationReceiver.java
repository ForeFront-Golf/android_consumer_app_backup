package com.rhinodesktop.foreorder_golf_consumer.receivers;

import android.location.Location;

import com.rhinodesktop.foreorder_golf_consumer.managers.apirequestmanagers.LocationApiManager;
import com.rhinodesktop.foreorder_golf_consumer.managers.apirequestmanagers.OrderApiManager;
import com.rhinodesktop.foreorder_golf_consumer.models.User;
import com.rhinodesktop.foreorder_golf_consumer.models.UserLocation;
import com.rhinodesktop.locationutilities.newlocationupdates.receivers.LocationReceiver;

import io.realm.Realm;
import timber.log.Timber;

/**
 * Created by hunter on 2018-03-23.
 */

public class ForeOrderLocationReceiver extends LocationReceiver {

    @Override
    protected void handleNewLocation(final Location location) {
        Timber.d("Location updated: Lat: %f, Lon: %f", location.getLatitude(), location.getLongitude());
        if (location != null) {
            try (Realm realm = Realm.getDefaultInstance()) {
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.where(UserLocation.class).findAll().deleteAllFromRealm();
                        realm.insert(new UserLocation(location.getLatitude(), location.getLongitude(), location.getAccuracy()));

                    }
                });
                int userId = realm.where(User.class).findFirst().getUserId();
                LocationApiManager.updateCurrentUserLocationOnServer(userId, location);
                OrderApiManager.checkOrderStatus(userId);
            }
        }
    }
}
