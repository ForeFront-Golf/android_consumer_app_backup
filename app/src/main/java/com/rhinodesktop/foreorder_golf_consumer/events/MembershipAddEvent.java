package com.rhinodesktop.foreorder_golf_consumer.events;

import lombok.Getter;

/**
 * Created by sungwook on 2018-04-05.
 */

public class MembershipAddEvent {

    @Getter
    private boolean successful;

    public MembershipAddEvent(boolean successful) {
        this.successful = successful;
    }
}
