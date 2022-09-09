package com.rhinoactive.jsonparsercallback;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * Created by hunter on 2018-01-30.
 */

public abstract class JsonParser<E, T extends JsonElement> extends StandardParser {

    public abstract void handleError(Exception ex);

    protected abstract T getJsonElement(JsonObject obj, String jsonKey);

    protected abstract String getJsonKey();

    protected abstract E handleSuccessfulParse(T jsonElement, GsonBuilder builder);

    protected abstract void postSuccessfulParsingLogic(E parsedObject);

    @Override
    protected void parseJsonData(JsonObject obj, GsonBuilder builder) {
        try {
            T jsonElement = getJsonElement(obj, getJsonKey());
            if (jsonElement == null) {
                throw new RuntimeException("Error parsing response, the json key: " + getJsonKey() + " was not in the response.");
            } else {
                E parsedObject = handleSuccessfulParse(jsonElement, builder);
                postSuccessfulParsingLogic(parsedObject);
            }
        } catch(RuntimeException ex) {
            handleNoJsonKeyInResponse(ex);
        }
    }

    @Override
    protected void requestFailed(Exception ex) {
        handleError(ex);
    }

    @Override
    protected void errorParsingResponse(Exception ex) {
        handleError(ex);
    }

    protected void handleNoJsonKeyInResponse(RuntimeException ex) {
        handleError(ex);
    }
}
