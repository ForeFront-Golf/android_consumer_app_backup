package com.rhinodesktop.foreorder_golf_consumer.managers.apirequestmanagers;

import com.rhinoactive.jsonparsercallback.StandardCallback;
import com.rhinodesktop.foreorder_golf_consumer.networking.ApiRequests;
import com.rhinodesktop.foreorder_golf_consumer.parsers.MembershipAddParser;
import com.rhinodesktop.foreorder_golf_consumer.parsers.MembershipListParser;
import com.rhinodesktop.foreorder_golf_consumer.parsers.PrivateClubsListParser;
import com.rhinodesktop.foreorder_golf_consumer.parsers.RemoveMembershipParser;

import okhttp3.Call;

/**
 * Created by sungwook on 2018-04-05.
 */

public class MembershipApiManager {

    public static void getMembershipList(int userId) {
        MembershipListParser parser = new MembershipListParser();
        try {
            StandardCallback callback = new StandardCallback(parser);
            Call call = ApiRequests.getInstance().getMembershipList(userId);
            call.enqueue(callback);
        } catch (Exception ex) {
            parser.handleError(ex);
        }
    }

    public static void addMembership(int clubId, String memberCode, int userId) {
        MembershipAddParser parser = new MembershipAddParser();
        try {
            StandardCallback callback = new StandardCallback(parser);
            Call call = ApiRequests.getInstance().addCourseMembership(clubId, memberCode, userId);
            call.enqueue(callback);
        } catch (Exception ex) {
            parser.handleError(ex);
        }
    }

    public static void getPrivateClubsList() {
        PrivateClubsListParser parser = new PrivateClubsListParser();
        try {
            StandardCallback callback = new StandardCallback(parser);
            Call call = ApiRequests.getInstance().getPrivateClubsList();
            call.enqueue(callback);
        } catch (Exception ex) {
            parser.handleError(ex);
        }
    }

    public static void removeMembership(int membershipId, int userId) {
        RemoveMembershipParser parser = new RemoveMembershipParser();
        try {
            StandardCallback callback = new StandardCallback(parser);
            Call call = ApiRequests.getInstance().removeCourseMembership(membershipId, userId);
            call.enqueue(callback);
        } catch (Exception ex) {
            parser.handleError(ex);
        }
    }
}
