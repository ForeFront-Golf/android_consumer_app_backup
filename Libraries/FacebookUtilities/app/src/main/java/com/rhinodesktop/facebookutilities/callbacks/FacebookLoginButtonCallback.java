package com.rhinodesktop.facebookutilities.callbacks;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.rhinodesktop.facebookutilities.FacebookLoginManager;
import com.rhinodesktop.facebookutilities.FacebookPermission;
import com.rhinodesktop.facebookutilities.LogoutHandler;

import java.util.List;

import timber.log.Timber;

/**
 * Created by rhinodesktop on 2017-01-20.
 */

public abstract class FacebookLoginButtonCallback implements FacebookCallback<LoginResult> {

    private String loginUrl;
    private View loginView;
    private Activity activity;
    private List<FacebookPermission> requiredPermissions;
    private LoginCallback loginCallback;
    private LogoutHandler logoutHandler;

    public FacebookLoginButtonCallback(Activity activity, String loginUrl, View loginView, List<FacebookPermission> requiredPermissions,
                                       LoginCallback loginCallback, LogoutHandler logoutHandler) {
        this.activity = activity;
        this.loginUrl = loginUrl;
        this.loginView = loginView;
        this.requiredPermissions = requiredPermissions;
        this.loginCallback = loginCallback;
        this.logoutHandler = logoutHandler;
    }

    @Override
    public void onSuccess(LoginResult loginResult) {
        if (FacebookLoginManager.getInstance().hasPermissions(requiredPermissions)) {
            loginView.setVisibility(View.INVISIBLE);
            Timber.d("Login Result Success");
            FacebookLoginManager.getInstance().logUserIn(loginUrl, loginCallback);
        } else {
            didNotGrantRequiredPermissions(activity);
        }
    }

    @Override
    public void onCancel() {
        Timber.d("cancel");
    }

    @Override
    public void onError(FacebookException error) {
        Timber.d("error: %s", error.toString());
        Toast.makeText(activity, "An error occurred. Login Failed.", Toast.LENGTH_SHORT).show();
        //Logout in case the error was because another user is still logged in
        FacebookLoginManager.getInstance().logoutOfFacebook(logoutHandler);
    }

    protected abstract String requiredPermissionNotGrantedToastMessage();

    private void didNotGrantRequiredPermissions(Context context) {
        Toast.makeText(context, requiredPermissionNotGrantedToastMessage(), Toast.LENGTH_LONG).show();
        FacebookLoginManager.getInstance().logoutOfFacebook(logoutHandler);
    }
}
