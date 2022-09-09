package com.rhinodesktop.foreorder_golf_consumer.facebook;

import com.onesignal.OneSignal;
import com.rhinodesktop.facebookutilities.LogoutHandler;
import com.rhinodesktop.foreorder_golf_consumer.managers.apirequestmanagers.AccountApiManager;
import com.rhinodesktop.foreorder_golf_consumer.models.Session;
import com.rhinodesktop.foreorder_golf_consumer.models.User;
import com.rhinodesktop.foreorder_golf_consumer.utils.ForeOrderApp;
import com.rhinodesktop.foreorder_golf_consumer.utils.OneSignalUtils;
import com.rhinodesktop.locationutilities.newlocationupdates.services.UpdateLocationForegroundService;

import io.realm.Realm;

/**
 * Created by rhinodesktop on 2017-03-07.
 */

public class ForeOrderLogoutHandler extends LogoutHandler {

    @Override
    public void logout() {
        super.logout();
        Realm realm = Realm.getDefaultInstance();
        OneSignalUtils.sendLogoutUserToOneSignal();
        User currentUser = realm.where(User.class).findFirst();
        if (currentUser != null) {
            AccountApiManager.invalidateCurrentUserSession(currentUser.getUserId());
            try (Realm realm1 = Realm.getDefaultInstance()) {
                realm1.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.where(User.class).findAll().deleteAllFromRealm();
                        realm.where(Session.class).findAll().deleteAllFromRealm();
                    }
                });
            }
        }
        realm.close();
        OneSignal.logoutEmail();
        UpdateLocationForegroundService.stopService(ForeOrderApp.getAppContext());
    }
}
