package com.rhinodesktop.foreorder_golf_consumer.facebook;

import android.app.Activity;
import android.widget.Button;

import com.rhinodesktop.facebookutilities.FacebookPermission;
import com.rhinodesktop.facebookutilities.LogoutHandler;
import com.rhinodesktop.facebookutilities.callbacks.FacebookLoginButtonCallback;
import com.rhinodesktop.facebookutilities.callbacks.LoginCallback;

import java.util.List;

/**
 * Created by rhinodesktop on 2017-03-07.
 */

public class ForeOrderFacebookLoginButtonCallback extends FacebookLoginButtonCallback {

    public ForeOrderFacebookLoginButtonCallback(Activity activity, String loginUrl, Button loginButton, List<FacebookPermission>
            requiredPermissions, LoginCallback loginCallback, LogoutHandler logoutHandler) {
        super(activity, loginUrl, loginButton, requiredPermissions, loginCallback, logoutHandler);
    }

    @Override
    protected String requiredPermissionNotGrantedToastMessage() {
        return "You did not grant the required permissions.";
    }
}
