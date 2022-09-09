package com.rhinodesktop.foreorder_golf_consumer.utils;

import android.content.Context;


import androidx.multidex.MultiDexApplication;

import com.rhinodesktop.foreorder_golf_consumer.R;
import com.rhinodesktop.foreorder_golf_consumer.loggers.TimberLogImplementation;
import com.rhinodesktop.locationutilities.newlocationupdates.models.ForegroundNotification;

import net.danlew.android.joda.JodaTimeAndroid;

import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;
import io.realm.Realm;
import io.realm.RealmConfiguration;
//import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by rhinodesktop on 2017-03-15.
 */

public class ForeOrderApp extends MultiDexApplication {

    private static Context appContext;

    public static Context getAppContext() {
        return appContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        JodaTimeAndroid.init(this);
        appContext = getApplicationContext();

        TimberLogImplementation.init();
        initRealm();

        OneSignalUtils.initOneSignal(this);

        setUpForegroundNotification();

        setUpCalligraphy();
    }

    private void initRealm() {
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder().deleteRealmIfMigrationNeeded().build();
        Realm.setDefaultConfiguration(config);
    }

    private void setUpForegroundNotification() {
        ForegroundNotification foregroundNotification = ForegroundNotification.getInstance();
        foregroundNotification.setNotificationTitle(getApplicationInfo().loadLabel(getPackageManager()).toString());
        foregroundNotification.setNotificationText(getString(R.string.notification_text));
        foregroundNotification.setNotificationIcon(R.mipmap.ic_launcher);
    }

    private void setUpCalligraphy() {
//        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
//                .setDefaultFontPath("fonts/Montserrat_Medium.ttf")
//                .setFontAttrId(R.attr.fontPath)
//                .build());

        ViewPump.init(ViewPump.builder()
                .addInterceptor(new CalligraphyInterceptor(
                        new CalligraphyConfig.Builder()

                                .setDefaultFontPath("fonts/Montserrat_Medium.ttf")
                                .setFontAttrId(R.attr.fontPath)
                                .build()))
                .build());
    }
}
