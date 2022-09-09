package com.rhinodesktop.facebookutilities.graphrequests;

import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.rhinoactive.jsonparsercallback.utils.ParserUtils;

import org.json.JSONObject;

/**
 * Created by rhinodesktop on 2017-01-19.
 */

public abstract class FacebookGraphRequest implements GraphRequest.Callback {

    @Override
    public void onCompleted(GraphResponse response) {
        try {
            JSONObject jsonObject = response.getJSONObject();
            JsonParser jsonParser = new JsonParser();
            JsonObject gsonObject = (JsonObject) jsonParser.parse(jsonObject.toString());
            GsonBuilder builder = ParserUtils.createBuilder();

            handleResponse(gsonObject, builder);

        } catch (Exception ex) {
            handleError(ex);
        }
    }

    protected abstract void handleResponse(JsonObject gsonObject, GsonBuilder builder);

    protected abstract void handleError(Exception ex);

}
