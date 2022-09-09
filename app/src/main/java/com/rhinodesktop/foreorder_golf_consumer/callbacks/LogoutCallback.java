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
 * Created by rhinodesktop on 2017-03-31.
 */

public class LogoutCallback implements Callback {

    public void handleFailure(Exception ex) {
        ForeOrderToastUtils.getInstance().displayToastFromMainThreadLong(Constants.ERROR_OCCURRED);
        Timber.e("Logout failed with the exception: %s", ex.getMessage());
    }

    @Override
    public void onFailure(@NonNull Call call, @NonNull IOException ex) {
        handleFailure(ex);
    }

    @Override
    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
        if (response.code() == 200) {

        }
        response.close();
    }
}
