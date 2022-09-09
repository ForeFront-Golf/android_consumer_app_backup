package com.rhinodesktop.foreorder_golf_consumer.activities;

import android.os.Bundle;

import android.view.ViewTreeObserver;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.rhinoactive.nointernetview.BroadcastReceiverManager;
import com.rhinoactive.nointernetview.ConnectionChangeListener;
import com.rhinoactive.nointernetview.NetworkUtils;
import com.rhinodesktop.foreorder_golf_consumer.R;

/**
 * Created by hunter on 2018-03-26.
 */

public abstract class NoInternetAppBarActivity extends TrackUserLocationActivity implements ConnectionChangeListener {

    private BroadcastReceiverManager broadcastReceiverManager;
    private int noInternetViewHeight = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        broadcastReceiverManager = new BroadcastReceiverManager(this, this);
        broadcastReceiverManager.installListener();
    }

    @Override
    protected void onDestroy() {
        if (broadcastReceiverManager != null) {
            broadcastReceiverManager.uninstallListener();
        }
        super.onDestroy();
    }

    @Override
    public void connectionChanged(boolean isConnected) {
        adjustToolbarHeightForNoInternetView(isConnected);
    }

    @Override
    protected void initToolbarView() {
        super.initToolbarView();
        addHeightIfNotConnected();
    }

    private void addHeightIfNotConnected() {
        toolbar.getViewTreeObserver().addOnGlobalLayoutListener (
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        toolbar.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        boolean isConnected = NetworkUtils.hasNetworkConnection(NoInternetAppBarActivity.this);
                        noInternetViewHeight = findViewById(R.id.no_internet_view).getHeight();
                        if (!isConnected) {
                            adjustHeightForSizeOfNoInternetView(noInternetViewHeight);
                        }
                    }
                });
    }

    private void adjustToolbarHeightForNoInternetView(boolean isConnected) {
        if (isConnected) {
            adjustHeightForSizeOfNoInternetView(-noInternetViewHeight);
        } else {
            adjustHeightForSizeOfNoInternetView(noInternetViewHeight);
        }
    }

    private void adjustHeightForSizeOfNoInternetView(int noInternetViewHeight) {
        CollapsingToolbarLayout.LayoutParams layoutParams = (CollapsingToolbarLayout.LayoutParams) toolbar.getLayoutParams();
        layoutParams.height = layoutParams.height + noInternetViewHeight;
        toolbar.setLayoutParams(layoutParams);
    }
}
