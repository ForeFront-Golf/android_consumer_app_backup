package com.rhinoactive.jsonparsercallback;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.Date;

/**
 * Created by rhinodesktop on 2017-01-18.
 */

public class DateDeserializer implements JsonDeserializer<Date> {
    @Override
    public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        long seconds = json.getAsJsonPrimitive().getAsLong();
        long milliseconds = seconds * 1000;
        return new Date(milliseconds);
    }
}