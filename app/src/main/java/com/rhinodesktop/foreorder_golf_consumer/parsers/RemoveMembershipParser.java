package com.rhinodesktop.foreorder_golf_consumer.parsers;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.rhinoactive.foreorder_library_android.utils.Constants;
import com.rhinoactive.jsonparsercallback.JsonObjectParser;
import com.rhinodesktop.foreorder_golf_consumer.events.RemoveMembershipEvent;
import com.rhinodesktop.foreorder_golf_consumer.models.Membership;

import org.greenrobot.eventbus.EventBus;

import timber.log.Timber;

/**
 * Created by sungwook on 2018-04-12.
 */

public class RemoveMembershipParser extends JsonObjectParser<Membership> {
    @Override
    public void handleError(Exception ex) {
        Timber.e(ex.getMessage());
        broadcastEvent(false);
    }

    @Override
    protected String getJsonKey() {
        return Constants.MEMBERSHIP_TITLE;
    }

    @Override
    protected Membership handleSuccessfulParse(JsonObject jsonElement, GsonBuilder builder) {
        return builder.create().fromJson(jsonElement, Membership.class);
    }

    @Override
    protected void postSuccessfulParsingLogic(Membership parsedObject) {
        broadcastEvent(true);
    }

    private void broadcastEvent(boolean deletedStatus) {
        EventBus.getDefault().post(new RemoveMembershipEvent(deletedStatus));
    }
}
