package com.rhinoactive.jsonparsercallback;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.stream.MalformedJsonException;
import com.rhinoactive.jsonparsercallback.utils.ParserUtils;

import java.io.IOException;
import java.io.Reader;

import okhttp3.Call;
import timber.log.Timber;

public abstract class StandardArrayParser {

    public void parseResponse(Reader reader) {
        try {
            com.google.gson.JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(reader);

            if (element.isJsonArray()) {
                JsonArray array = element.getAsJsonArray();
                GsonBuilder builder = ParserUtils.createBuilder();

                parseJsonData(array, builder);
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

    protected abstract void parseJsonData(JsonArray array, GsonBuilder builder);

    private void closeResponse(Reader reader) {
        try {
            reader.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
