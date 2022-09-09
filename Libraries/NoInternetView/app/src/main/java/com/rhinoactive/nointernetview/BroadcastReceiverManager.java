package com.rhinoactive.nointernetview;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;

/**
 * Created by Hunter Andrin on 2017-11-28.
 */

public class BroadcastReceiverManager {

    private BroadcastReceiver broadcastReceiver;
    private Context context;
    private ConnectionChangeListener connectionChangeListener;

    public BroadcastReceiverManager(Context context, ConnectionChangeListener connectionChangeListener) {
        this.context = context;
        this.connectionChangeListener = connectionChangeListener;
    }

    public void installListener() {
        if (broadcastReceiver == null) {
            broadcastReceiver = new NoInternetConnectionReceiver(connectionChangeListener);
            final IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
            context.registerReceiver(broadcastReceiver, intentFilter);
        }
    }

    public void uninstallListener() {
        context.unregisterReceiver(broadcastReceiver);
        broadcastReceiver = null;
    }
}
