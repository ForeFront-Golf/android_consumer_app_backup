package com.rhinodesktop.foreorder_golf_consumer.events;

import lombok.Getter;

/**
 * Created by Hunter Andrin on 2017-04-27.
 */

public class ValidatePinEvent {

    @Getter
    private boolean validateSuccessful;

    public ValidatePinEvent(boolean validateSuccessful) {
        this.validateSuccessful = validateSuccessful;
    }
}
