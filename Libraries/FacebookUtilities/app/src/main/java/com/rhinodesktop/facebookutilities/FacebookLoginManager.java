package com.rhinodesktop.facebookutilities;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.widget.LoginButton;
import com.rhinodesktop.facebookutilities.callbacks.FacebookLoginButtonCallback;
import com.rhinodesktop.facebookutilities.callbacks.LoginCallback;
import com.rhinodesktop.facebookutilities.interfaces.AccessTokenInvalidInterface;

import java.util.List;

import static com.facebook.AccessToken.getCurrentAccessToken;

/**
 * Created by rhinodesktop on 2017-01-17.
 */

public class FacebookLoginManager {

    private static FacebookLoginManager facebookLoginManager = new FacebookLoginManager();

    private CallbackManager callbackManager = CallbackManager.Factory.create();
    private AccessTokenTracker accessTokenTracker;
    private AccessTokenInvalidInterface invalidAccessTokenHandler;

    private FacebookLoginManager() {}

    public static FacebookLoginManager getInstance() {
        return facebookLoginManager;
    }

    public boolean isLoggedIn() {
        return getCurrentAccessToken() != null && Profile.getCurrentProfile() != null;
    }

    public void logUserIn(String loginUrl, LoginCallback loginCallback) {
        LoginFacebookUserManager.loginFacebookUser(loginUrl, loginCallback);
    }

    public void logoutOfFacebook(LogoutHandler logoutHandler) {
        logoutHandler.logout();
    }

    //TODO: This logic is incomplete. This was intended to log the user out if the fb access token became invalid while the user was using the app.
    public void trackAccessToken(AccessTokenInvalidInterface invalidAccessTokenHandler) {
        this.invalidAccessTokenHandler = invalidAccessTokenHandler;
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken newAccessToken) {
                logoutIfTokenInvalid(newAccessToken);
            }
        };
    }

    public void handleInvalidAccessToken() {
        accessTokenTracker.stopTracking();
        accessTokenTracker = null;
        if (invalidAccessTokenHandler != null) {
            invalidAccessTokenHandler.handleInvalidAccessToken();
        }
    }

    public boolean hasPermissions(List<FacebookPermission> permissions) {
        boolean hasPermissions = true;
        for (FacebookPermission permission : permissions) {
            if (!permission.hasPermission()) {
                hasPermissions = false;
                break;
            }
        }
        return hasPermissions;
    }

    public void registerLoginButton(LoginButton loginButton, FacebookLoginButtonCallback loginButtonCallback) {
        loginButton.registerCallback(callbackManager, loginButtonCallback);
    }

    public void registerCustomLoginButton(final Activity activity, final View loginView,
                                          final FacebookLoginButtonCallback loginButtonCallback, final List<String> requestedPermissions) {
        LoginManager.getInstance().registerCallback(callbackManager, loginButtonCallback);
        loginView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().logInWithReadPermissions(activity, requestedPermissions);
            }
        });
    }

    // This method should be called from the Activity's or Fragment's onActivityResult method.
    public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
        return callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void logoutIfTokenInvalid(AccessToken newAccessToken) {
        if (newAccessToken == null) {
            accessTokenTracker.stopTracking();
            accessTokenTracker = null;
            if (invalidAccessTokenHandler != null) {
                invalidAccessTokenHandler.handleInvalidAccessToken();
            }
        }
    }
}
