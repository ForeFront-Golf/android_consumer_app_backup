package com.rhinodesktop.foreorder_golf_consumer.parsers;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.rhinoactive.foreorder_library_android.utils.Constants;
import com.rhinoactive.jsonparsercallback.JsonArrayParser;
import com.rhinodesktop.foreorder_golf_consumer.events.ClubsUpdatedEvent;
import com.rhinodesktop.foreorder_golf_consumer.models.Club;
import com.rhinodesktop.foreorder_golf_consumer.utils.ForeOrderToastUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import io.realm.Realm;
import timber.log.Timber;

/**
 * Created by Hunter Andrin on 2017-04-04.
 */

public class ClubsParser extends JsonArrayParser<Club> {

    @Override
    public void handleError(Exception ex) {
        Timber.e("Error getting clubs: %s", ex.getMessage());
        ForeOrderToastUtils.getInstance().displayToastFromMainThreadLong(Constants.ERROR_OCCURRED_GETTING_CLUBS);
    }

    @Override
    protected String getJsonKey() {
        return Constants.CLUB_TABLE;
    }

    @Override
    protected Club parseSingleElement(JsonElement clubElement, GsonBuilder builder) {
        Club club = builder.create().fromJson(clubElement, Club.class);
        club.setDist(club.getDist()); // Convert M to KM
        return club;
    }

    @Override
    protected void postSuccessfulParsingLogic(final List<Club> parsedObject) {
        try (Realm realm = Realm.getDefaultInstance()) {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.insertOrUpdate(parsedObject);
                }
            });
        }
        EventBus.getDefault().post(new ClubsUpdatedEvent());
    }
}
