package com.rhinodesktop.foreorder_golf_consumer.utils;

import android.content.Context;

import com.onesignal.OneSignal;
import com.rhinoactive.foreorder_library_android.utils.Constants;
import com.rhinodesktop.foreorder_golf_consumer.models.Club;
import com.rhinodesktop.foreorder_golf_consumer.models.User;

import java.util.Locale;

/**
 * Created by sungwook on 2018-04-23.
 */

public class OneSignalUtils {

    public static void initOneSignal(Context appContext) {
        OneSignal.startInit(appContext)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();
    }

    public static void sendUserEmailToOneSignal(User user) {
        // Call syncHashedEmail anywhere in your app if you have the user's email.
        // This improves the effectiveness of OneSignal's "best-time" notification scheduling feature.
        OneSignal.setEmail(user.getEmail());
        OneSignal.sendTag(Constants.EMAIL, user.getEmail());
        OneSignal.sendTag(Constants.CONSUMER, "true");
        OneSignal.sendTag(Constants.USER_ID, String.valueOf(user.getUserId()));
    }

    public static void sendClubToOneSignal(Club club) {
        OneSignal.sendTag(Constants.CLUB_ID, String.format(Locale.ENGLISH, "%d", club.getClubId()));
    }

    public static void sendLogoutUserToOneSignal() {
        OneSignal.sendTag(Constants.EMAIL, "");
    }
}
