package com.rhinodesktop.foreorder_golf_consumer.events;

import com.rhinodesktop.foreorder_golf_consumer.models.Membership;

import java.util.List;

import lombok.Getter;

/**
 * Created by sungwook on 2018-04-05.
 */

public class MembershipListEvent {
    @Getter
    private List<Membership> membershipList;

    public MembershipListEvent(List<Membership> membershipList) {
        this.membershipList = membershipList;
    }
}
