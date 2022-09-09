package com.rhinodesktop.foreorder_golf_consumer.callbacks;


import com.rhinoactive.foreorder_library_android.utils.Constants;
import com.rhinodesktop.foreorder_golf_consumer.events.PasswordResetEvent;
import com.rhinodesktop.foreorder_golf_consumer.utils.ForeOrderToastUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

import lombok.NonNull;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import timber.log.Timber;

/**
 * Created by Hunter Andrin on 2017-05-17.
 */

public class ResetPasswordCallback implements Callback {

    public void handleError(Exception ex) {
        Timber.e("Reset password failed with the following exception: %s", ex.getMessage());
        ForeOrderToastUtils.getInstance().displayToastFromMainThreadLong(Constants.ERROR_OCCURRED);
    }

    @Override
    public void onFailure(@NonNull Call call, @NonNull IOException ex) {
        handleError(ex);
    }

    @Override
    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
        if (response.code() == 200) {
            broadcastEvent(PasswordResetEvent.PasswordResetCallbackCode.SUCCESS);
        } else if (response.code() == 429) {
            broadcastEvent(PasswordResetEvent.PasswordResetCallbackCode.FAILURE_THREE_TIMES);
        } else {
            broadcastEvent(PasswordResetEvent.PasswordResetCallbackCode.FAILURE);
        }
        response.close();
    }

    private void broadcastEvent(PasswordResetEvent.PasswordResetCallbackCode callbackCode) {
        EventBus.getDefault().post(new PasswordResetEvent(callbackCode));
    }
}