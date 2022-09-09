package com.rhinodesktop.foreorder_golf_consumer.parsers.loginparsers;

import android.app.Activity;

import com.appsee.Appsee;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.rhinoactive.foreorder_library_android.utils.Constants;
import com.rhinodesktop.facebookutilities.FacebookLoginManager;
import com.rhinodesktop.facebookutilities.parsers.LoginParser;
import com.rhinodesktop.foreorder_golf_consumer.events.LoginEvent;
import com.rhinodesktop.foreorder_golf_consumer.facebook.ForeOrderLogoutHandler;
import com.rhinodesktop.foreorder_golf_consumer.managers.ForeOrderActivityAndAnimateManager;
import com.rhinodesktop.foreorder_golf_consumer.models.Session;
import com.rhinodesktop.foreorder_golf_consumer.models.User;
import com.rhinodesktop.foreorder_golf_consumer.utils.ForeOrderToastUtils;
import com.rhinodesktop.foreorder_golf_consumer.utils.OneSignalUtils;

import org.greenrobot.eventbus.EventBus;

import io.realm.Realm;
import timber.log.Timber;



/**
 * Created by rhinodesktop on 2017-03-07.
 */

public class ForeOrderLoginParser extends LoginParser {

    protected User user;
    protected Activity previousActivity;

    public ForeOrderLoginParser(Activity previousActivity) {
        this.previousActivity = previousActivity;
    }

    public void handleError(Exception ex) {
        Timber.e("Login Failed with the exception: %s", ex.getMessage());
        ForeOrderToastUtils.getInstance().displayToastFromMainThreadLong(Constants.ERROR_OCCURRED);
        broadcastLoginEvent(false);
    }

    @Override
    protected boolean wasLoginSuccessful(JsonObject obj, GsonBuilder builder) {
        JsonObject usrObj = obj.getAsJsonObject(Constants.USER_TABLE);
        user = builder.create().fromJson(usrObj, User.class);
        return user != null;
    }

    @Override
    protected void handleSuccessfulLogin(JsonObject obj, GsonBuilder builder) {
        JsonObject sessionObj = obj.getAsJsonObject(Constants.SESSION_TABLE);
        final Session session = builder.create().fromJson(sessionObj, Session.class);
        try (Realm realm = Realm.getDefaultInstance()) {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.where(User.class).findAll().deleteAllFromRealm();
                    realm.where(Session.class).findAll().deleteAllFromRealm();
                    realm.insert(user);
                    realm.insert(session);
                }
            });
        }
        OneSignalUtils.sendUserEmailToOneSignal(user);
        ForeOrderActivityAndAnimateManager.proceedToAppropriateScreenUponLogin(previousActivity);
        broadcastLoginEvent(true);
        Appsee.setUserId(String.valueOf(user.getUserId()));
    }

    @Override
    protected void handleFailedLogin(JsonObject obj, GsonBuilder builder) {
        Timber.e("Failed to log the user in. JsonObject contains the following data: %s", obj.toString());
        displayFailedMessage();
        broadcastLoginEvent(false);
        FacebookLoginManager.getInstance().logoutOfFacebook(new ForeOrderLogoutHandler());
    }

    @Override
    protected void requestFailed(Exception ex) {
        handleError(ex);
    }

    @Override
    protected void errorParsingResponse(Exception ex) {
        handleError(ex);
    }

    protected void displayFailedMessage() {
        ForeOrderToastUtils.getInstance().displayToastFromMainThreadLong(Constants.LOGIN_FAILED);
    }

    private void broadcastLoginEvent(boolean loginSuccessful) {
        EventBus.getDefault().post(new LoginEvent(loginSuccessful));
    }
}
