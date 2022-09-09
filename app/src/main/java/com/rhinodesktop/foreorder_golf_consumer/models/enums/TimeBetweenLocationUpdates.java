package com.rhinodesktop.foreorder_golf_consumer.models.enums;

import lombok.Getter;

/**
 * Created by Hunter Andrin on 2017-04-04.
 */

public enum TimeBetweenLocationUpdates {

    AT_GOLF_COURSE(300000), //300000 - 5 min
    ORDER_PLACED(30000), // 30000 - 30 sec
    NOT_AT_GOLF_COURSE(1800000);

    @Getter
    private final int timeBetweenUpdates;

    TimeBetweenLocationUpdates(int timeBetweenUpdates) {
        this.timeBetweenUpdates = timeBetweenUpdates;
    }
}
