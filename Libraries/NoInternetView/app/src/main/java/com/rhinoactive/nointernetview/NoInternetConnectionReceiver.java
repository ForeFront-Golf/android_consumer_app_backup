package com.rhinoactive.nointernetview;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.rhinoactive.nointernetview.events.InternetConnectionChangeEvent;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Hunter Andrin on 2017-11-27.
 */

public class NoInternetConnectionReceiver extends BroadcastReceiver {

    Boolean previousConnection = null;

    private ConnectionChangeListener connectionChangeListener;

    public NoInternetConnectionReceiver(ConnectionChangeListener connectionChangeListener) {
        this.connectionChangeListener = connectionChangeListener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Boolean isConnected = NetworkUtils.hasNetworkConnection(context);
         /*
        The CONNECTIVITY_ACTION broadcast is sticky on some devices. This means that when you register
        the receiver it will immediately call onReceive() with the most recently sent broadcast.
        We only want to handle direct broadcasts.
         */
        if (!isInitialStickyBroadcast() && !isConnected.equals(previousConnection)) {
            /*
            The receiver will intercept 3 events: disconnected, connecting, and connected.
            Connecting will be interpreted as connected. This check ensures that connection change
            event only gets called once when connecting to the internet.
             */
            previousConnection = isConnected;
            connectionChangeListener.connectionChanged(isConnected);
            EventBus.getDefault().post(new InternetConnectionChangeEvent(isConnected));
        }
    }
}
