package com.rhinodesktop.foreorder_golf_consumer.parsers.loginparsers;

import android.app.Activity;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.rhinoactive.imageutility.amazonimageuploaders.AmazonImageLinkUploader;
import com.rhinoactive.imageutility.amazonimageuploaders.AmazonImageUploader;
import com.rhinodesktop.foreorder_golf_consumer.managers.apirequestmanagers.ForeOrderImageUploader;
import com.rhinodesktop.foreorder_golf_consumer.models.FacebookUser;
import com.rhinoactive.foreorder_library_android.utils.Constants;

/**
 * Created by Hunter Andrin on 2017-04-05.
 */

public class FacebookLoginParser extends ForeOrderLoginParser {

    public FacebookLoginParser(Activity previousActivity) {
        super(previousActivity);
    }

    @Override
    protected void handleSuccessfulLogin(JsonObject obj, GsonBuilder builder) {
        super.handleSuccessfulLogin(obj, builder);
        if (user.getProfilePhotoUrl() == null) {
            uploadFacebookProfilePhoto(obj, builder);
        }
    }

    private void uploadFacebookProfilePhoto(JsonObject obj, GsonBuilder builder) {
        JsonObject fbUsrObj = obj.getAsJsonObject(Constants.USER_TABLE).getAsJsonObject(Constants.FACEBOOK_USER);
        FacebookUser facebookUser = builder.create().fromJson(fbUsrObj, FacebookUser.class);
        String imgLink = facebookUser.getProfileImageUrl();
        AmazonImageUploader amazonImageUploader = new AmazonImageLinkUploader(imgLink);
        ForeOrderImageUploader.uploadImageToAmazon(amazonImageUploader);
    }
}
