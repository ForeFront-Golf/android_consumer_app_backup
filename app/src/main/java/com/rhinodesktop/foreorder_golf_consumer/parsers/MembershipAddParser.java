package com.rhinodesktop.foreorder_golf_consumer.parsers;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.rhinoactive.foreorder_library_android.utils.Constants;
import com.rhinoactive.jsonparsercallback.JsonObjectParser;
import com.rhinodesktop.foreorder_golf_consumer.events.MembershipAddEvent;
import com.rhinodesktop.foreorder_golf_consumer.models.Membership;

import org.greenrobot.eventbus.EventBus;

import timber.log.Timber;

/**
 * Created by sungwook on 2018-04-05.
 */

public class MembershipAddParser extends JsonObjectParser<Membership> {
    @Override
    public void handleError(Exception ex) {
        Timber.e("Error getting clubs: %s", ex.getMessage());
        broadcastEvent(false);
    }

    @Override
    protected String getJsonKey() {
        return Constants.MEMBERSHIP_TITLE;
    }

    @Override
    protected Membership handleSuccessfulParse(JsonObject jsonElement, GsonBuilder builder) {
        return builder.setLenient().create().fromJson(jsonElement, Membership.class);
    }

    @Override
    protected void postSuccessfulParsingLogic(Membership parsedObject) {
        broadcastEvent(parsedObject.getValid());
    }

    private void broadcastEvent(boolean isMembershipAdded) {
        EventBus.getDefault().post(new MembershipAddEvent(isMembershipAdded));
    }
}
