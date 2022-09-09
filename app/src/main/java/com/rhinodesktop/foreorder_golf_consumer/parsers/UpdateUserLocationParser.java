package com.rhinodesktop.foreorder_golf_consumer.parsers;

import android.content.Context;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.rhinoactive.jsonparsercallback.JsonObjectParser;
import com.rhinodesktop.foreorder_golf_consumer.events.ClubInRangeChangeEvent;
import com.rhinodesktop.foreorder_golf_consumer.managers.CurrentOrder;
import com.rhinodesktop.foreorder_golf_consumer.models.Club;
import com.rhinoactive.foreorder_library_android.utils.Constants;
import com.rhinodesktop.foreorder_golf_consumer.utils.ForeOrderApp;
import com.rhinodesktop.foreorder_golf_consumer.utils.ForeOrderSharedPrefUtils;
import com.rhinodesktop.locationutilities.newlocationupdates.services.UpdateLocationForegroundService;
import com.rhinodesktop.locationutilities.newlocationupdates.utils.LocationUpdatesUtils;

import org.greenrobot.eventbus.EventBus;

import io.realm.Realm;
import timber.log.Timber;

/**
 * Created by Hunter Andrin on 2017-04-06.
 */

public class UpdateUserLocationParser extends JsonObjectParser<Club> {

    @Override
    public void handleError(Exception ex) {
        Timber.e("An error occurred when trying to update the user's location :%s", ex.getMessage());
    }

    @Override
    protected String getJsonKey() {
        return Constants.CLUB_TABLE;
    }

    @Override
    protected Club handleSuccessfulParse(JsonObject clubObject, GsonBuilder builder) {
        return builder.create().fromJson(clubObject, Club.class);
    }

    @Override
    protected void postSuccessfulParsingLogic(Club club) {
        userIsAtClub(club);
    }

    @Override
    protected void handleNoJsonKeyInResponse(RuntimeException ex) {
        userIsNotAtClub();
    }

    private void userIsNotAtClub() {
        Timber.d("User is not at a club");
        Context context = ForeOrderApp.getAppContext();
        if (ForeOrderSharedPrefUtils.getCurrentClubId(context) != 0) {
            ForeOrderSharedPrefUtils.setCurrentClubId(context, 0);
            CurrentOrder.getInstance().getOrder().clearOrder();
            if (LocationUpdatesUtils.isRequestingUpdatesInForeground(context)) {
                UpdateLocationForegroundService.stopService(context);
            }
            notifyObservers(false);
        }
    }

    private void userIsAtClub(final Club club) {
        Timber.d("User is at a club");
        try (Realm realm = Realm.getDefaultInstance()) {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.insertOrUpdate(club);
                }
            });
        }

        Context context = ForeOrderApp.getAppContext();
        int currentClubId = ForeOrderSharedPrefUtils.getCurrentClubId(context);
        if (currentClubId == 0 || currentClubId != club.getClubId()) {
            ForeOrderSharedPrefUtils.setCurrentClubId(context, club.getClubId());
            notifyObservers(true);
        }
    }

    private void notifyObservers(boolean atClub) {
        LocationUpdatesUtils.setRequestLocationUpdatesInForeground(ForeOrderApp.getAppContext(), atClub);
        EventBus.getDefault().post(new ClubInRangeChangeEvent(atClub));
    }
}
