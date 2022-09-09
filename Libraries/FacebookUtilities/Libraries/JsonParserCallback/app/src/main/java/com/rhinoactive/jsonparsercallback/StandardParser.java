package com.rhinoactive.jsonparsercallback;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.MalformedJsonException;
import com.rhinoactive.jsonparsercallback.utils.ParserUtils;

import java.io.IOException;
import java.io.Reader;

import okhttp3.Call;
import timber.log.Timber;

/**
 * Created by rhinodesktop on 2017-01-18.
 */

public abstract class StandardParser {

    public void parseResponse(Reader reader) {
        try {
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(reader);

            if (element.isJsonObject()) {
                JsonObject obj = element.getAsJsonObject();
                GsonBuilder builder = ParserUtils.createBuilder();

                parseJsonData(obj, builder);
            } else {
                throw new MalformedJsonException("Could not parse json data.");
            }
        } catch (Exception ex) {
            errorParsingResponse(ex);
        } finally {
            closeResponse(reader);
        }
    }

    protected void handleFailure(Call call, Exception ex) {
        Timber.e("Callback failed with the following error: %s", ex.toString());
        Timber.e("The callback's parser is of the following class: %s", this.getClass());
        requestFailed(ex);
    }

    protected abstract void requestFailed(Exception ex);

    protected abstract void errorParsingResponse(Exception ex);

    protected abstract void parseJsonData(JsonObject obj, GsonBuilder builder);

    private void closeResponse(Reader reader) {
        try {
            reader.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
