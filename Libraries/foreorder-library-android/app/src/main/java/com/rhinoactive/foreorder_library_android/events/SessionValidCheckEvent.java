package com.rhinoactive.foreorder_library_android.events;

/**
 * Created by Hunter Andrin on 2017-05-02.
 */

public class SessionValidCheckEvent extends BooleanEvent {

    public SessionValidCheckEvent(boolean successful) {
        super(successful);
    }
}
