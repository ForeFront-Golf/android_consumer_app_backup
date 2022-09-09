package com.rhinodesktop.foreorder_golf_consumer.models;

import android.content.Context;
import android.preference.PreferenceManager;

import com.google.gson.annotations.SerializedName;
import com.rhinoactive.foreorder_library_android.utils.Constants;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import lombok.Getter;

import static com.rhinoactive.foreorder_library_android.utils.Constants.KEY_CHECK_PREVIOUS_SCREEN;

/**
 * Created by sungwook on 2018-04-06.
 */

public class Membership extends RealmObject {

    public Membership() {}

    @PrimaryKey
    @SerializedName(Constants.CLUB_ID)
    @Getter
    private Integer clubId;
    @SerializedName(Constants.CLUB_TABLE)
    @Getter
    private Club club;
    @SerializedName(Constants.FIRST_NAME)
    @Getter
    private String firstName;
    @SerializedName(Constants.LAST_NAME)
    @Getter
    private String lastName;
    @SerializedName(Constants.MEMBER_CODE)
    @Getter
    private String memberCode;
    @SerializedName(Constants.MEMBERSHIP_ID)
    @Getter
    private Integer membershipId;
    @Getter
    private String notes;
    @SerializedName(Constants.PHONE_NUMBER)
    @Getter
    private String phoneNumber;
    @SerializedName(Constants.USER_ID)
    @Getter
    private Integer userId;
    @Getter
    private Boolean valid;

    public static boolean cameFromPINScreen(Context context) {
        return getBooleanFromSharedPrefs(context, KEY_CHECK_PREVIOUS_SCREEN);
    }

    public static void gotHereFromPINfragment(Context context, boolean isFromPINFragment) {
        saveBooleanToSharedPrefs(context, KEY_CHECK_PREVIOUS_SCREEN, isFromPINFragment);
    }

    private static boolean getBooleanFromSharedPrefs(Context context, String key) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(key, false);
    }

    private static void saveBooleanToSharedPrefs(Context context, String key, boolean startLocationUpdateServiceInForeground) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(key, startLocationUpdateServiceInForeground)
                .apply();
    }
}

