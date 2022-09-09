package com.rhinodesktop.foreorder_golf_consumer.events;

import lombok.Getter;

/**
 * Created by Hunter Andrin on 2017-04-07.
 */

public class MenusUpdatedEvent {

    @Getter
    private boolean errorInMenu;

    public MenusUpdatedEvent(boolean errorInMenu) {
        this.errorInMenu = errorInMenu;
    }
}
