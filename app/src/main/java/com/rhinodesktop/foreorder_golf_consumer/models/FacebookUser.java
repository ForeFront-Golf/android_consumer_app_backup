package com.rhinodesktop.foreorder_golf_consumer.models;

import com.google.gson.annotations.SerializedName;
import com.rhinoactive.foreorder_library_android.utils.Constants;

import lombok.Getter;

/**
 * Created by Hunter Andrin on 2017-04-05.
 */

public class FacebookUser {

    @Getter
    @SerializedName(Constants.PROFILE_IMAGE_URL)
    private String profileImageUrl;
}
