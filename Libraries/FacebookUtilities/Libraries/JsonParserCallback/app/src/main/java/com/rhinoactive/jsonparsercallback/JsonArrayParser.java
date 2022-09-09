package com.rhinoactive.jsonparsercallback;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hunter on 2018-01-30.
 */

public abstract class JsonArrayParser<E> extends JsonParser<List<E>, JsonArray> {

    private List<E> parsedObjects = new ArrayList<>();

    protected abstract E parseSingleElement(JsonElement singleJsonElement, GsonBuilder builder);

    @Override
    protected JsonArray getJsonElement(JsonObject obj, String jsonKey) {
        return obj.getAsJsonArray(jsonKey);
    }

    @Override
    protected List<E> handleSuccessfulParse(JsonArray jsonElement, GsonBuilder builder) {
        for (JsonElement singleJsonElement : jsonElement) {
            parsedObjects.add(parseSingleElement(singleJsonElement, builder));
        }
        return parsedObjects;
    }
}
