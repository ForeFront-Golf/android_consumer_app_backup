package com.rhinodesktop.foreorder_golf_consumer.events;

import lombok.Getter;

/**
 * Created by rhinodesktop on 2017-03-31.
 */

public class LoginEvent {

    @Getter
    private boolean successful;

    public LoginEvent(boolean successful) {
        this.successful = successful;
    }
}
