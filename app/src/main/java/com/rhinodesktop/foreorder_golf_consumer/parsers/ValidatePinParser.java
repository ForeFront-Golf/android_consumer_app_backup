package com.rhinodesktop.foreorder_golf_consumer.parsers;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.rhinoactive.foreorder_library_android.utils.Constants;
import com.rhinoactive.jsonparsercallback.JsonObjectParser;
import com.rhinodesktop.foreorder_golf_consumer.events.ValidatePinEvent;
import com.rhinodesktop.foreorder_golf_consumer.models.Session;
import com.rhinodesktop.foreorder_golf_consumer.utils.ForeOrderToastUtils;

import org.greenrobot.eventbus.EventBus;

import io.realm.Realm;
import timber.log.Timber;

/**
 * Created by Hunter Andrin on 2017-04-26.
 */

public class ValidatePinParser extends JsonObjectParser<Session> {

    @Override
    public void handleError(Exception ex) {
        Timber.e("Validate PIN failed with the exception: %s", ex.getMessage());
        ForeOrderToastUtils.getInstance().displayToastFromMainThreadLong(Constants.ERROR_OCCURRED);
    }

    @Override
    protected String getJsonKey() {
        return Constants.SESSION_TABLE;
    }

    @Override
    protected Session handleSuccessfulParse(JsonObject sessionObj, GsonBuilder builder) {
        return builder.create().fromJson(sessionObj, Session.class);
    }

    @Override
    protected void postSuccessfulParsingLogic(final Session session) {
        try (Realm realm = Realm.getDefaultInstance()) {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.where(Session.class).findAll().deleteAllFromRealm();
                    realm.insert(session);
                }
            });
        }

        broadcastValidatePinEvent(true);
    }

    @Override
    protected void handleNoJsonKeyInResponse(RuntimeException ex) {
        broadcastValidatePinEvent(false);
    }

    private void broadcastValidatePinEvent(boolean validateSuccessful) {
        EventBus.getDefault().post(new ValidatePinEvent(validateSuccessful));
    }
}
