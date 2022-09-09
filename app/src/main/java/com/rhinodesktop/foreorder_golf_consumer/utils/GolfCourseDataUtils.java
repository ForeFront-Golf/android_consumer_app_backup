package com.rhinodesktop.foreorder_golf_consumer.utils;

import com.rhinodesktop.foreorder_golf_consumer.events.ClubsUpdatedEvent;
import com.rhinodesktop.foreorder_golf_consumer.models.Club;
import com.rhinoactive.generalutilities.DistanceUtils;
import com.rhinodesktop.foreorder_golf_consumer.models.UserLocation;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import io.realm.Realm;

/**
 * Created by Hunter Andrin on 2017-04-06.
 */

public class GolfCourseDataUtils {

    public static void setClubDistanceFromCurrentUser() {
        Realm realm = Realm.getDefaultInstance();
        UserLocation location = realm.where(UserLocation.class).findFirst();
        List<Club> clubs = realm.where(Club.class).findAll();
        realm.close();

        for (Club club : clubs) {
            club.setDist(DistanceUtils.distanceBetween(club.getLat(), club.getLon(),
                   location.getLat(), location.getLon()) / 1000); // Convert M to KM
        }
        EventBus.getDefault().post(new ClubsUpdatedEvent());
    }
}
