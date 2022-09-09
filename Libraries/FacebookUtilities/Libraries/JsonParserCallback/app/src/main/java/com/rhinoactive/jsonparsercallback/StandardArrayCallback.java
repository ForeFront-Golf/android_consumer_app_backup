package com.rhinoactive.jsonparsercallback;



import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class StandardArrayCallback implements Callback {

    protected StandardArrayParser parser;

    public StandardArrayCallback(StandardArrayParser parser) {
        this.parser = parser;
    }


    @Override
    public void onFailure(Call call, IOException e) {
        parser.handleFailure(call, e);
    }

    @Override
    public void onResponse(Call call, Response response) {
        parser.parseResponse(response.body().charStream());
    }
}
