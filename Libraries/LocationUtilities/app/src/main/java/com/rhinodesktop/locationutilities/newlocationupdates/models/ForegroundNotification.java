package com.rhinodesktop.locationutilities.newlocationupdates.models;

import com.rhinodesktop.locationutilities.R;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by hunter on 2018-03-22.
 */

public class ForegroundNotification {

    public static ForegroundNotification foregroundNotification;

    private ForegroundNotification() {}

    public static ForegroundNotification getInstance() {
        if (foregroundNotification == null) {
            foregroundNotification = new ForegroundNotification();
        }
        return foregroundNotification;
    }

    @Getter
    @Setter
    private String notificationText = "Text";
    @Getter
    @Setter
    private String notificationTitle = "Title";
    @Getter
    @Setter
    private String launchButtonText = "Launch App";
    @Getter
    @Setter
    private String removeButtonText = "Remove Location Updates";
    @Getter
    @Setter
    private int notificationIcon = R.mipmap.ic_launcher;
    @Getter
    @Setter
    private int launchIcon = R.mipmap.ic_launcher;
    @Getter
    @Setter
    private int removeIcon = R.mipmap.ic_launcher;
}
