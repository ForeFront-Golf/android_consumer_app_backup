package com.rhinodesktop.foreorder_golf_consumer.events;

import lombok.Getter;

/**
 * Created by Hunter Andrin on 2017-05-17.
 */

public class PasswordResetEvent {

    public enum PasswordResetCallbackCode {
        SUCCESS,
        FAILURE,
        FAILURE_THREE_TIMES
    }

    @Getter
    private PasswordResetCallbackCode callbackCode;

    public PasswordResetEvent(PasswordResetCallbackCode callbackCode) {
        this.callbackCode = callbackCode;
    }
}
