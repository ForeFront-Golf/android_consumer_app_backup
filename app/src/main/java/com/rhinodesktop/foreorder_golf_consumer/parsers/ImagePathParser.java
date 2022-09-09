package com.rhinodesktop.foreorder_golf_consumer.parsers;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.rhinoactive.foreorder_library_android.utils.Constants;
import com.rhinoactive.jsonparsercallback.JsonObjectParser;
import com.rhinodesktop.foreorder_golf_consumer.events.UserImageUpdatedEvent;
import com.rhinodesktop.foreorder_golf_consumer.models.User;
import com.rhinodesktop.foreorder_golf_consumer.utils.ForeOrderToastUtils;

import org.greenrobot.eventbus.EventBus;

import io.realm.Realm;
import timber.log.Timber;

/**
 * Created by Hunter Andrin on 2017-04-03.
 */

public class ImagePathParser extends JsonObjectParser<User> {

    @Override
    public void handleError(Exception ex) {
        Timber.e("Error uploading image: %s", ex.getMessage());
        ForeOrderToastUtils.getInstance().displayToastFromMainThreadLong(Constants.ERROR_UPLOADING_PHOTO);
    }

    @Override
    protected String getJsonKey() {
        return Constants.USER_TABLE;
    }

    @Override
    protected User handleSuccessfulParse(JsonObject userJson, GsonBuilder builder) {
        final User user = builder.create().fromJson(userJson, User.class);
        try (Realm realm = Realm.getDefaultInstance()) {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    User currentUser = realm.where(User.class).findFirst();
                    currentUser.setProfilePhotoUrl(user.getProfilePhotoUrl());
                }
            });
        }
        return user;
    }

    @Override
    protected void postSuccessfulParsingLogic(User user) {
        EventBus.getDefault().post(new UserImageUpdatedEvent());
    }
}
