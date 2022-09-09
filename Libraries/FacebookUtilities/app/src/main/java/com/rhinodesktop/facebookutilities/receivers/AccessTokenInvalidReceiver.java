package com.rhinodesktop.facebookutilities.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.rhinodesktop.facebookutilities.FacebookLoginManager;

/**
 * Created by rhinodesktop on 2017-01-18.
 */

public class AccessTokenInvalidReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(final Context context, Intent intent) {
        FacebookLoginManager.getInstance().handleInvalidAccessToken();
    }
}