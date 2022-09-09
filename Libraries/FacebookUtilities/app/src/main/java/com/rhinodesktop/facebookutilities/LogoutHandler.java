package com.rhinodesktop.facebookutilities;

import com.facebook.login.LoginManager;
import com.rhinodesktop.facebookutilities.models.FbPhotosAndPagers;

/**
 * Created by rhinodesktop on 2017-01-23.
 */

public class LogoutHandler {

    public void logout() {
        LoginManager.getInstance().logOut();
        FacebookPhotoManager.getInstance().setCurrentUserFbPhotosAndPagers(new FbPhotosAndPagers());
    }
}
