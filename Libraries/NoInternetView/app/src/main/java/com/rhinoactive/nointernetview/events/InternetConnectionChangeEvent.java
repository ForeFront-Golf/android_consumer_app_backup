package com.rhinoactive.nointernetview.events;

/**
 * Created by sungwook on 2018-04-12.
 */

public class InternetConnectionChangeEvent {
    private boolean isConnected;

    public InternetConnectionChangeEvent(boolean isConnected) {
        this.isConnected = isConnected;
    }

    public boolean isConnected() {
        return isConnected;
    }
}
