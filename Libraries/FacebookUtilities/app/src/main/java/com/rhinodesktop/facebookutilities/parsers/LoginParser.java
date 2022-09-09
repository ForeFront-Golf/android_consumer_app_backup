package com.rhinodesktop.facebookutilities.parsers;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.rhinoactive.jsonparsercallback.StandardParser;

/**
 * Created by rhinodesktop on 2017-01-19.
 */

public abstract class LoginParser extends StandardParser {

    @Override
    protected void parseJsonData(JsonObject obj, GsonBuilder builder) {
        boolean loginSuccessful = wasLoginSuccessful(obj, builder);
        if (loginSuccessful) {
            handleSuccessfulLogin(obj, builder);
        } else {
            handleFailedLogin(obj, builder);
        }
    }

    protected abstract boolean wasLoginSuccessful(JsonObject obj, GsonBuilder builder);

    protected abstract void handleSuccessfulLogin(JsonObject obj, GsonBuilder builder);

    protected abstract void handleFailedLogin(JsonObject obj, GsonBuilder builder);
}
