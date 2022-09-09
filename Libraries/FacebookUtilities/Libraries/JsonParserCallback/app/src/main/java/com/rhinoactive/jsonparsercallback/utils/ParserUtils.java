package com.rhinoactive.jsonparsercallback.utils;

import com.google.gson.GsonBuilder;
import com.rhinoactive.jsonparsercallback.DateDeserializer;

import java.util.Date;

/**
 * Created by rhinodesktop on 2017-01-18.
 */

public class ParserUtils {
    public static GsonBuilder createBuilder() {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Date.class, new DateDeserializer());
        builder.serializeNulls();
        return builder;
    }
}
