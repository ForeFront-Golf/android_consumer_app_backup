package com.rhinodesktop.facebookutilities;

import android.os.Bundle;

import com.facebook.GraphRequest;
import com.facebook.HttpMethod;
import com.rhinodesktop.facebookutilities.graphrequests.FacebookGraphRequest;
import com.rhinodesktop.facebookutilities.graphrequests.FacebookWorkGraphRequest;
import com.rhinodesktop.facebookutilities.interfaces.RetrieveWorkExperiencesCallback;

import static com.facebook.AccessToken.getCurrentAccessToken;

/**
 * Created by rhinodesktop on 2017-02-06.
 */

public class FacebookUserInfoManager {

    private static FacebookUserInfoManager facebookUserInfoManager;

    private FacebookUserInfoManager() {}

    public static FacebookUserInfoManager getInstance() {
        if (facebookUserInfoManager == null) {
            facebookUserInfoManager = new FacebookUserInfoManager();
        }
        return facebookUserInfoManager;
    }

    public void getUserWorkInformation(RetrieveWorkExperiencesCallback callback) {
        String graphPath = getCurrentAccessToken().getUserId();
        Bundle parameters = new Bundle();
        parameters.putString("fields", "work");
        FacebookGraphRequest facebookGraphRequest = new FacebookWorkGraphRequest(callback);
        new GraphRequest(getCurrentAccessToken(), graphPath, parameters, HttpMethod.GET, facebookGraphRequest).executeAsync();
    }
}
