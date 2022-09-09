package com.rhinodesktop.foreorder_golf_consumer.events;

import com.rhinodesktop.foreorder_golf_consumer.models.Club;

import java.util.List;

import lombok.Getter;

/**
 * Created by sungwook on 2018-04-12.
 */

public class PrivateClubsListEvent {

    @Getter
    private List<Club> privateClubsList;
    @Getter
    private boolean privateClubExist;

    public PrivateClubsListEvent(List<Club> privateClubsList, boolean privateClubExist) {
        this.privateClubsList = privateClubsList;
        this.privateClubExist = privateClubExist;
    }
}
