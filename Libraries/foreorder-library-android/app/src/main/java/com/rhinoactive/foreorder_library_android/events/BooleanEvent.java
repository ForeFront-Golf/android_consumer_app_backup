package com.rhinoactive.foreorder_library_android.events;

import lombok.Getter;

/**
 * Created by Hunter Andrin on 2017-05-02.
 */

public abstract class BooleanEvent {

    @Getter
    private boolean successful;

    public BooleanEvent(boolean successful) {
        this.successful = successful;
    }
}
