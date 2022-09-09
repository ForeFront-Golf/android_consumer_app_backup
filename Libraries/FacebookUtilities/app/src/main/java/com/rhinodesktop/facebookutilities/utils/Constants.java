package com.rhinodesktop.facebookutilities.utils;

/**
 * Created by rhinodesktop on 2017-01-18.
 */

public class Constants {

    public enum LOGIN_STATE {
        SUCCESS, FAILURE, NEW_ACCOUNT, FB_ACT_NOT_FOUND, LOGIN_TEMP_PW,
        PW_RESET_REQUEST, PW_RESET_SUCCESS, PW_RESET_FAILURE, PW_CHANGE_SUCCESS, PW_CHANGE_ERROR
    }

    public static final String PROFILE_PICTURES = "Profile Pictures";

    // Facebook photo types
    public static final String TYPE_TAGGED = "tagged";
    public static final String TYPE_UPLOADED = "uploaded";
}
