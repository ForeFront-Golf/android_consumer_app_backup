package com.rhinodesktop.foreorder_golf_consumer.callbacks;

import com.rhinoactive.jsonparsercallback.StandardCallback;
import com.rhinoactive.jsonparsercallback.StandardParser;
import com.rhinodesktop.foreorder_golf_consumer.events.DuplicateKeyEvent;
import com.rhinoactive.foreorder_library_android.utils.Constants;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Hunter Andrin on 2017-04-28.
 */

public class DuplicateKeyCallback extends StandardCallback {

    public DuplicateKeyCallback(StandardParser parser) {
        super(parser);
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        if (response.code() == Constants.CONFLICT) {
            broadcastDuplicateKetEvent();
        } else {
            super.onResponse(call, response);
        }
        response.body().close();
    }

    // A 409 error code will be returned if the user attempts to use an email or phone number that is already in use
    protected void broadcastDuplicateKetEvent() {
        EventBus.getDefault().post(new DuplicateKeyEvent());
    }
}
