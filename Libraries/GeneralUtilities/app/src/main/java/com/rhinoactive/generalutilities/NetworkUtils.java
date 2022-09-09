package com.rhinoactive.generalutilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by rhinodesktop on 2017-03-02.
 */

public class NetworkUtils {

    /**
     *
     * If you are using this method then make sure to include the ACCESS_NETWORK_STATE permission in
     * your AndroidManifest.xml
     *
     * @param context A reference to a context
     * @return Whether or not the device has an active network connection
     */
    public static boolean hasNetworkConnection(Context context) {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnected();
    }
}
