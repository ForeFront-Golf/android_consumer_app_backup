package com.rhinodesktop.foreorder_golf_consumer.managers.apirequestmanagers;

import android.location.Location;

import com.rhinoactive.jsonparsercallback.StandardCallback;
import com.rhinodesktop.foreorder_golf_consumer.callbacks.NoContentCallback;
import com.rhinodesktop.foreorder_golf_consumer.networking.ApiRequests;
import com.rhinodesktop.foreorder_golf_consumer.parsers.ClubsParser;

import okhttp3.Call;

/**
 * Created by Hunter Andrin on 2017-04-04.
 */

public class ClubApiManager {

    public static void getClubsNearCurrentUser(Location location) {
        ClubsParser clubsParser = new ClubsParser();
        try {
            StandardCallback callback = new NoContentCallback(clubsParser);
            Call call = ApiRequests.getInstance().getClubsNearCurrentUser(location);
            call.enqueue(callback);
        } catch (Exception ex) {
            clubsParser.handleError(ex);
        }
    }
}
