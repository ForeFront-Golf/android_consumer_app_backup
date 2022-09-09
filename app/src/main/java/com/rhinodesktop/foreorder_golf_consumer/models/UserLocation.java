package com.rhinodesktop.foreorder_golf_consumer.models;

import io.realm.RealmObject;
import lombok.Getter;

public class UserLocation extends RealmObject {

    public UserLocation() {
    }

    public UserLocation(double lat, double lon, float accuracy) {
        this.lat = lat;
        this.lon = lon;
        this.accuracy = accuracy;
    }

    @Getter
    private double lat;
    @Getter
    private double lon;
    @Getter
    private float accuracy;
}
