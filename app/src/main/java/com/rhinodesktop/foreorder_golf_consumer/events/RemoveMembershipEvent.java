package com.rhinodesktop.foreorder_golf_consumer.events;

import lombok.Getter;

/**
 * Created by sungwook on 2018-04-12.
 */

public class RemoveMembershipEvent {
    @Getter
    private boolean deleted;

    public RemoveMembershipEvent(boolean deleted) {
        this.deleted = deleted;
    }
}
