package com.rhinodesktop.facebookutilities.graphrequests;

import android.util.Log;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.rhinodesktop.facebookutilities.models.FbPhoto;

import lombok.Getter;
import timber.log.Timber;

/**
 * Created by rhinodesktop on 2017-01-19.
 */

public abstract class FacebookPhotosForAlbumRequest extends FacebookGraphRequest {

    @Getter
    private String albumName;

    public FacebookPhotosForAlbumRequest(String albumName) {
        this.albumName = albumName;
    }

    @Override
    protected void handleError(Exception ex) {
        Timber.e("Failed to get the user's photos for the album: %s", albumName);
        ex.printStackTrace();
    }

    protected FbPhoto buildFbPhoto(GsonBuilder builder, JsonElement photoElement) {
        return builder.create().fromJson(photoElement, FbPhoto.class);
    }

}