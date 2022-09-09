package com.rhinoactive.jsonparsercallback;

import com.google.gson.JsonObject;

/**
 * Created by hunter on 2018-01-30.
 */

public abstract class JsonObjectParser<E> extends JsonParser<E, JsonObject> {

    @Override
    protected JsonObject getJsonElement(JsonObject obj, String jsonKey) {
        return obj.getAsJsonObject(jsonKey);
    }
}
