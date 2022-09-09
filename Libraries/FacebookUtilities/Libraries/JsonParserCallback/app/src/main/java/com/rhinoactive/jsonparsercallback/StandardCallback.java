package com.rhinoactive.jsonparsercallback;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by rhinodesktop on 2017-01-18.
 */

public class StandardCallback implements Callback {

    protected StandardParser parser;

    public StandardCallback(StandardParser parser) {
        this.parser = parser;
    }

    @Override
    public void onFailure(Call call, IOException e) {
        parser.handleFailure(call, e);
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        parser.parseResponse(response.body().charStream());
    }
}
