package com.rhinodesktop.foreorder_golf_consumer.utils;

import android.content.Context;

import com.rhinoactive.generalutilities.ResourceUtils;

/**
 * Created by rhinodesktop on 2017-03-16.
 */

public class ForeOrderResourceUtils extends ResourceUtils {

    private static ForeOrderResourceUtils foreOrderResourceUtils;

    private ForeOrderResourceUtils() {}

    public static ForeOrderResourceUtils getInstance() {
        if (foreOrderResourceUtils == null) {
            foreOrderResourceUtils = new ForeOrderResourceUtils();
        }
        return foreOrderResourceUtils;
    }

    @Override
    protected Context getAppContext() {
        return ForeOrderApp.getAppContext();
    }
}
