package com.rhinodesktop.facebookutilities;

import java.util.Set;

import static com.facebook.AccessToken.getCurrentAccessToken;

/**
 * Created by rhinodesktop on 2017-01-17.
 */

public enum FacebookPermission {
    PUBLIC_PROFILE("public_profile"),
    EMAIL_PERMISSION("email"),
    BIRTHDAY_PERMISSION("user_birthday"),
    PHOTOS_PERMISSION("user_photos"),
    USER_LIKES_PERMISSION("user_likes"),
    WORK_HISTORY_PERMISSION("user_work_history"),
    ABOUT_ME_PERMISSION("user_about_me");

    private final String name;

    FacebookPermission(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public boolean hasPermission() {
        boolean hasPermission = false;
        if (getCurrentAccessToken() != null) {
            Set<String> declinedPermissions = getCurrentAccessToken().getDeclinedPermissions();
            if (!declinedPermissions.contains(this.getName())) {
                hasPermission = true;
            }
        }
        return hasPermission;
    }

    @Override
    public String toString() {
        return getName();
    }
}
