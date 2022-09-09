package com.rhinodesktop.locationutilities.newlocationupdates.utils;

import lombok.Getter;

/**
 * Created by sungwook on 2018-04-03.
 */

public class OrderStatusEvent {
    @Getter
    private boolean doesOpenOrderExist;

    public OrderStatusEvent(boolean doesOpenOrderExist) {
        this.doesOpenOrderExist = doesOpenOrderExist;
    }
}
