package com.rhinodesktop.foreorder_golf_consumer.events;

import lombok.Getter;

/**
 * Created by Hunter Andrin on 2017-04-10.
 */

public class PlaceOrderEvent {

    @Getter
    private boolean successful;

    public PlaceOrderEvent(boolean successful) {
        this.successful = successful;
    }
}
