package com.rhinoactive.nointernetview;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Hunter Andrin on 2017-11-27.
 */

public class NetworkUtils {

    public static boolean hasNetworkConnection(Context context) {
        boolean hasConnection = true;
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            hasConnection = (activeNetwork != null && activeNetwork.isConnected());
        }
        return hasConnection;
    }
}
