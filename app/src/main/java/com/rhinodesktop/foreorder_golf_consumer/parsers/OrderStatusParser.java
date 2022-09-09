package com.rhinodesktop.foreorder_golf_consumer.parsers;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.rhinoactive.foreorder_library_android.utils.Constants;
import com.rhinoactive.jsonparsercallback.JsonArrayParser;
import com.rhinodesktop.foreorder_golf_consumer.models.Order;
import com.rhinodesktop.foreorder_golf_consumer.utils.ForeOrderApp;
import com.rhinodesktop.locationutilities.newlocationupdates.utils.LocationUpdatesUtils;
import com.rhinodesktop.locationutilities.newlocationupdates.utils.OrderStatusEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by sungwook on 2018-04-02.
 */

public class OrderStatusParser extends JsonArrayParser<Order> {

    private boolean mPreviousStatus;
    @Override
    public void handleError(Exception ex) {
        // no active orders?
//        Timber.e(ex.getMessage());
        notifyObservers(false);
    }

    @Override
    protected String getJsonKey() {
        return Constants.ORDER_TABLE;
    }

    @Override
    protected Order parseSingleElement(JsonElement orderElement, GsonBuilder builder) {
        return builder.create().fromJson(orderElement, Order.class);
    }

    @Override
    protected void postSuccessfulParsingLogic(List<Order> orders) {
        // There is an active order
        notifyObservers(true);
    }

    private void notifyObservers(boolean doesOpenOrderExist) {
        mPreviousStatus = LocationUpdatesUtils.getPreviousUpdateStatus(ForeOrderApp.getAppContext());
        if (doesOpenOrderExist != mPreviousStatus) {
            EventBus.getDefault().post(new OrderStatusEvent(doesOpenOrderExist));
        }
        LocationUpdatesUtils.setPreviousUpdateStatus(ForeOrderApp.getAppContext(), doesOpenOrderExist);
    }
}
