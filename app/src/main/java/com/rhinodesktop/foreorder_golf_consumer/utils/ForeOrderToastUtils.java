package com.rhinodesktop.foreorder_golf_consumer.utils;

import android.content.Context;

import com.rhinoactive.generalutilities.ToastUtils;

/**
 * Created by rhinodesktop on 2017-03-30.
 */

public class ForeOrderToastUtils extends ToastUtils {

    private static ForeOrderToastUtils foreOrderToastUtils;

    private ForeOrderToastUtils() {}

    public static ForeOrderToastUtils getInstance() {
        if (foreOrderToastUtils == null) {
            foreOrderToastUtils = new ForeOrderToastUtils();
        }
        return foreOrderToastUtils;
    }

    @Override
    protected Context getAppContext() {
        return ForeOrderApp.getAppContext();
    }
}
