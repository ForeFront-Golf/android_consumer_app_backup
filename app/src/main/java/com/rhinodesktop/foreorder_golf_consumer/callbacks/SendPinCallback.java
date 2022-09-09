package com.rhinodesktop.foreorder_golf_consumer.callbacks;


import com.rhinoactive.foreorder_library_android.utils.Constants;
import com.rhinodesktop.foreorder_golf_consumer.utils.ForeOrderToastUtils;

import java.io.IOException;

import lombok.NonNull;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import timber.log.Timber;

/**
 * Created by Hunter Andrin on 2017-04-26.
 */

public class SendPinCallback implements Callback {

    public void displayPinErrorMessage(Exception ex) {
        Timber.e("Send pin endpoint failed with the following exception: %s", ex.getMessage());
        ForeOrderToastUtils.getInstance().displayToastFromMainThreadLong(Constants.ERROR_OCCURRED_SENDING_PIN);
    }

    @Override
    public void onFailure(@NonNull Call call, @NonNull IOException ex) {
        displayPinErrorMessage(ex);
    }

    @Override
    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
        if (response.code() != 200) {
            displayPinErrorMessage(new Exception("Send PIN failed. Status code was not 200."));
        }
        response.close();
    }
}
