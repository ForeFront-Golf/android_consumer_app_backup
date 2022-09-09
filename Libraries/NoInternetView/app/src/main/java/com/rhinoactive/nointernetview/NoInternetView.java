package com.rhinoactive.nointernetview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hunter Andrin on 2017-11-27.
 */

public class NoInternetView extends RelativeLayout implements ConnectionChangeListener {

    protected List<View> uiViewsToDisableForNoInternet = new ArrayList<>();
    private RelativeLayout rLayoutNoInternet;
    private BroadcastReceiverManager broadcastReceiverManager;
    private Context context;

    public NoInternetView(Context context) {
        super(context);
        initViews(context);
    }

    public NoInternetView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(context);
    }

    public NoInternetView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(context);
    }

    //TODO: This is here for legacy reason and should be removed in the future.
    public void initUIViews(List<View> uiViewsToDisableForNoInternet) {
        this.uiViewsToDisableForNoInternet = uiViewsToDisableForNoInternet;
        boolean isConnected = NetworkUtils.hasNetworkConnection(context);
        enableConnectionViews(isConnected);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        broadcastReceiverManager = new BroadcastReceiverManager(context, this);
        broadcastReceiverManager.installListener();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        broadcastReceiverManager.uninstallListener();
    }

    @Override
    public void connectionChanged(boolean isConnected) {
        enableConnectionViews(isConnected);
    }

    private void initViews(Context context) {
        this.context = context;
        inflate(getContext(), R.layout.no_internet_view, this);
        rLayoutNoInternet = findViewById(R.id.rlayout_no_internet);
        initNoInternetViewHeight();
    }

    private void initNoInternetViewHeight() {
        this.getViewTreeObserver().addOnGlobalLayoutListener (
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        // gets called after layout has been done but before display
                        // in case we need to get the height of the view
                        NoInternetView.this.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        boolean isConnected = NetworkUtils.hasNetworkConnection(context);
                        enableConnectionViews(isConnected);
                    }
                });
    }

    private void enableConnectionViews(boolean isConnected) {
        enableClickableViews(isConnected);
        if (isConnected) {
            rLayoutNoInternet.setVisibility(View.GONE);
        } else {
            rLayoutNoInternet.setVisibility(View.VISIBLE);
        }
    }

    private void enableClickableViews(boolean isConnected) {
        for (View view : uiViewsToDisableForNoInternet) {
            view.setEnabled(isConnected);
            view.setClickable(isConnected);
        }
    }
}
