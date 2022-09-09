package com.rhinodesktop.foreorder_golf_consumer.managers;

import android.app.Activity;

import androidx.appcompat.app.AppCompatActivity;

import com.rhinoactive.generalutilities.MainThreadRunner;
import com.rhinodesktop.activityanimatorutility.activityutils.ActivityAndAnimateManager;
import com.rhinodesktop.activityanimatorutility.models.AnimationType;
import com.rhinodesktop.foreorder_golf_consumer.activities.MainActivity;
import com.rhinodesktop.foreorder_golf_consumer.events.InitVerifyPinEvent;
import com.rhinodesktop.foreorder_golf_consumer.events.ConfirmInformationEvent;
import com.rhinodesktop.foreorder_golf_consumer.models.User;

import org.greenrobot.eventbus.EventBus;

import io.realm.Realm;

/**
 * Created by Hunter Andrin on 2017-04-03.
 */

public class ForeOrderActivityAndAnimateManager {

    public static void proceedToMainActivity(Activity previousActivity) {
        ActivityAndAnimateManager.Builder builder = new ActivityAndAnimateManager.Builder((AppCompatActivity) previousActivity, MainActivity.class, AnimationType.FADE_IN);
        builder.clearStack();
        builder.buildActivityAndTransition();
    }

    public static void proceedToAppropriateScreenUponLogin(final Activity previousActivity) {
        Realm realm = Realm.getDefaultInstance();
        User currentUser = realm.where(User.class).findFirst();
        if (currentUser.getPhoneValid()) {
            if (currentUser.getDob() != null) {
                proceedToMainActivityWithDelay(previousActivity);
            } else {
                EventBus.getDefault().post(new ConfirmInformationEvent());
            }
        } else {
            if (currentUser.getPhoneNumber() == null) {
                EventBus.getDefault().post(new ConfirmInformationEvent());
            } else {
                EventBus.getDefault().post(new InitVerifyPinEvent());
            }
        }
        realm.close();
    }

    private static void proceedToMainActivityWithDelay(final Activity previousActivity) {
        MainThreadRunner.runFromUiThread(new Runnable() {
            @Override
            public void run() {
                ActivityAndAnimateManager.Builder builder = new ActivityAndAnimateManager.Builder((AppCompatActivity) previousActivity, MainActivity.class, AnimationType.FADE_IN);
                builder.clearStack();
                builder.buildActivityAndTransition();
            }
        });
    }
}
