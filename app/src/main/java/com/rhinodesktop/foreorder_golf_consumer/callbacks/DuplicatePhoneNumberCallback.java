package com.rhinodesktop.foreorder_golf_consumer.callbacks;

import com.rhinoactive.jsonparsercallback.StandardParser;
import com.rhinodesktop.foreorder_golf_consumer.events.DuplicatePhoneNumberEvent;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Hunter Andrin on 2017-04-28.
 */

public class DuplicatePhoneNumberCallback extends DuplicateKeyCallback {

    public DuplicatePhoneNumberCallback(StandardParser parser) {
        super(parser);
    }

    @Override
    protected void broadcastDuplicateKetEvent() {
        EventBus.getDefault().post(new DuplicatePhoneNumberEvent());
    }
}
