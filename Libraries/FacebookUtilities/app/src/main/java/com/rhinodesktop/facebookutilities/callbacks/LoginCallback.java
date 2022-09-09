package com.rhinodesktop.facebookutilities.callbacks;

import com.rhinodesktop.facebookutilities.parsers.LoginParser;
import com.rhinoactive.jsonparsercallback.StandardCallback;

/**
 * Created by rhinodesktop on 2017-01-18.
 */

public class LoginCallback extends StandardCallback {

    public LoginCallback(LoginParser parser) {
        super(parser);
    }
}
