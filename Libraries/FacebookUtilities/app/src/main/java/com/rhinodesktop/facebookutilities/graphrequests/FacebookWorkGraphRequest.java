package com.rhinodesktop.facebookutilities.graphrequests;

import android.util.Log;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.rhinodesktop.facebookutilities.interfaces.RetrieveWorkExperiencesCallback;
import com.rhinodesktop.facebookutilities.models.WorkExperience;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

/**
 * Created by rhinodesktop on 2017-02-06.
 */

public class FacebookWorkGraphRequest extends FacebookGraphRequest {

    private RetrieveWorkExperiencesCallback callback;

    public FacebookWorkGraphRequest(RetrieveWorkExperiencesCallback callback) {
        this.callback = callback;
    }

    @Override
    protected void handleResponse(JsonObject gsonObject, GsonBuilder builder) {
        JsonArray workArray = gsonObject.getAsJsonArray("work");
        if (workArray != null) {
            List<WorkExperience> jobs = new ArrayList<>();
            for (JsonElement workElement : workArray) {
                WorkExperience workExperience = builder.create().fromJson(workElement, WorkExperience.class);
                jobs.add(workExperience);
            }
            callback.handleWorkExperiencesRetrieved(jobs);
        }
    }

    @Override
    protected void handleError(Exception ex) {
        Timber.e("Failed to get the user's work information.");
        ex.printStackTrace();
    }
}
