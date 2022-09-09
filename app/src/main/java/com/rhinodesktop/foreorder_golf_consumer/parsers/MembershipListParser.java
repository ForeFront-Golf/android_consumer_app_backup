package com.rhinodesktop.foreorder_golf_consumer.parsers;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.rhinoactive.foreorder_library_android.utils.Constants;
import com.rhinoactive.jsonparsercallback.JsonArrayParser;
import com.rhinodesktop.foreorder_golf_consumer.events.MembershipListEvent;
import com.rhinodesktop.foreorder_golf_consumer.models.Membership;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

/**
 * Created by sungwook on 2018-04-05.
 */

public class MembershipListParser extends JsonArrayParser<Membership> {
    @Override
    public void handleError(Exception ex) {
        Timber.e("Error getting clubs: %s", ex.getMessage());
    }

    @Override
    protected String getJsonKey() {
        return Constants.MEMBERSHIP_TITLE;
    }

    @Override
    protected Membership parseSingleElement(JsonElement singleJsonElement, GsonBuilder builder) {
        return builder.setLenient().create().fromJson(singleJsonElement, Membership.class);
    }

    @Override
    protected void postSuccessfulParsingLogic(List<Membership> parsedObject) {
        EventBus.getDefault().post(new MembershipListEvent(getOnlyValidMembershipList(parsedObject)));
    }

    private List<Membership> getOnlyValidMembershipList(List<Membership> parsedObject) {
        List<Membership> validList = new ArrayList<>();
        for(int i = 0; i < parsedObject.size(); i++) {
            if (parsedObject.get(i).getValid()) {
                boolean flag = parsedObject.get(i).getValid();
                validList.add(parsedObject.get(i));
            }
        }
        return validList;
    }
}
