package com.rhinodesktop.foreorder_golf_consumer.callbacks;

import android.content.Intent;

import com.rhinoactive.foreorder_library_android.utils.Constants;
import com.rhinoactive.jsonparsercallback.StandardCallback;
import com.rhinoactive.jsonparsercallback.StandardParser;
import com.rhinodesktop.foreorder_golf_consumer.activities.LoginActivity;
import com.rhinodesktop.foreorder_golf_consumer.facebook.ForeOrderLogoutHandler;
import com.rhinodesktop.foreorder_golf_consumer.utils.ForeOrderApp;
import com.rhinodesktop.foreorder_golf_consumer.utils.ForeOrderToastUtils;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;
import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * Created by Hunter Andrin on 2017-04-06.
 */

public class UnauthorizedCallback extends StandardCallback {

    public UnauthorizedCallback(StandardParser parser) {
        super(parser);
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        if (response.code() == Constants.UNAUTHORIZED) {
            logOut();
        } else {
            super.onResponse(call, response);
        }
        response.body().close();
    }

    // A 401 error code will be returned if the user's session ID is invalid
    private void logOut() {
        ForeOrderLogoutHandler logoutHandler = new ForeOrderLogoutHandler();
        logoutHandler.logout();

        ForeOrderToastUtils.getInstance().displayToastFromMainThreadLong(Constants.SESSION_EXPIRED);

        Intent intent = new Intent(ForeOrderApp.getAppContext(), LoginActivity.class);
        intent.addFlags(FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
        ForeOrderApp.getAppContext().startActivity(intent);
    }
}
