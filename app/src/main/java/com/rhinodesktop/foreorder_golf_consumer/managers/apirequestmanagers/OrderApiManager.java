package com.rhinodesktop.foreorder_golf_consumer.managers.apirequestmanagers;

import com.rhinoactive.jsonparsercallback.StandardCallback;
import com.rhinodesktop.foreorder_golf_consumer.models.UserLocation;
import com.rhinodesktop.foreorder_golf_consumer.networking.ApiRequests;
import com.rhinodesktop.foreorder_golf_consumer.parsers.OrderParser;
import com.rhinodesktop.foreorder_golf_consumer.parsers.OrderStatusParser;

import okhttp3.Call;

/**
 * Created by Hunter Andrin on 2017-04-10.
 */

public class OrderApiManager {

    public static void placeOrder(int userId, int clubId, UserLocation location) {
        OrderParser orderParser = new OrderParser();
        try {
            StandardCallback callback = new StandardCallback(orderParser);
            Call call = ApiRequests.getInstance().placeOrder(userId, clubId, location);
            call.enqueue(callback);
        } catch (Exception ex) {
            orderParser.handleError(ex);
        }
    }

    public static void checkOrderStatus(int userId) {
        OrderStatusParser orderStatusParser = new OrderStatusParser();
        try {
            StandardCallback callback = new StandardCallback(orderStatusParser);
            Call call = ApiRequests.getInstance().checkOrderStatus(userId);
            call.enqueue(callback);
        } catch (Exception ex) {
            orderStatusParser.handleError(ex);
        }
    }
}
