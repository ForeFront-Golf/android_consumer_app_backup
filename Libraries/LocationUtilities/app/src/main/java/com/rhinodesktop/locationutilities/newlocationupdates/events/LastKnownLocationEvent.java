package com.rhinodesktop.locationutilities.newlocationupdates.events;

import android.location.Location;

import lombok.Getter;

/**
 * Created by hunter on 2018-03-22.
 */

public class LastKnownLocationEvent {

    @Getter
    private Location location;

    public LastKnownLocationEvent(Location location) {
        this.location = location;
    }
}
