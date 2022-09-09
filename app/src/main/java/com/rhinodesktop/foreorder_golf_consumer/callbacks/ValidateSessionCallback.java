package com.rhinodesktop.foreorder_golf_consumer.callbacks;


import com.rhinoactive.foreorder_library_android.events.SessionValidCheckEvent;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

import lombok.NonNull;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import timber.log.Timber;

/**
 * Created by Hunter Andrin on 2017-04-03.
 */

public class ValidateSessionCallback implements Callback {

    public void handleFailure(Exception ex) {
        Timber.e("Validate session endpoint failed with the following exception: %s", ex.getMessage());
        broadcastEvent(false);
    }

    @Override
    public void onFailure(@NonNull Call call, @NonNull IOException e) {
        handleFailure(e);
    }

    @Override
    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
        if (response.code() == 200) {
            broadcastEvent(true);
        } else {
            broadcastEvent(false);
        }
        response.close();
    }

    private void broadcastEvent(boolean isSessionValid) {
        EventBus.getDefault().post(new SessionValidCheckEvent(isSessionValid));
    }
}
