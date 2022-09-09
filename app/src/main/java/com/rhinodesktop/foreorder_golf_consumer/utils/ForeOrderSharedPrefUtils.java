package com.rhinodesktop.foreorder_golf_consumer.utils;

import android.content.Context;
import android.preference.PreferenceManager;

import com.rhinoactive.foreorder_library_android.utils.Constants;

/**
 * Created by sungwook on 2018-04-26.
 */

public class ForeOrderSharedPrefUtils {

    public static Integer getCurrentClubId(Context context) {
        return getIntegerFromSharedPrefs(context, Constants.KEY_CURRENT_CLUB_ID);
    }

    public static void setCurrentClubId(Context context, int clubId) {
        saveIntegerToSharedPrefs(context, Constants.KEY_CURRENT_CLUB_ID, clubId);
    }

    public static int getViewCartLayoutHeight(Context context) {
        return getIntegerFromSharedPrefs(context, Constants.GET_VIEW_CART_LAYOUT_HEIGHT);
    }

    public static void setViewCartLayoutHeight(Context context, int height) {
        saveIntegerToSharedPrefs(context, Constants.GET_VIEW_CART_LAYOUT_HEIGHT, height);
    }

    private static int getIntegerFromSharedPrefs(Context context, String key) {
        return PreferenceManager.getDefaultSharedPreferences(context).getInt(key, 0);
    }

    private static void saveIntegerToSharedPrefs(Context context, String key, int value) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putInt(key, value)
                .apply();
    }
}
