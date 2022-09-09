package com.rhinodesktop.foreorder_golf_consumer.parsers;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.rhinoactive.foreorder_library_android.utils.Constants;
import com.rhinoactive.jsonparsercallback.JsonArrayParser;
import com.rhinodesktop.foreorder_golf_consumer.events.PlaceOrderEvent;
import com.rhinodesktop.foreorder_golf_consumer.models.Order;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import timber.log.Timber;

/**
 * Created by sungwook on 2018-04-02.
 */

public class OrderParser extends JsonArrayParser<Order> {
    @Override
    public void handleError(Exception ex) {
        Timber.e("An exception was thrown while placing the order: %s", ex.getMessage());
        broadcastEvent(false);
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
        broadcastEvent(true);
    }

    private void broadcastEvent(boolean wasOrderSuccessful) {
        EventBus.getDefault().post(new PlaceOrderEvent(wasOrderSuccessful));
    }
}
