package com.rhinodesktop.facebookutilities.graphrequests;

import android.util.Log;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.rhinodesktop.facebookutilities.FacebookPhotoManager;
import com.rhinodesktop.facebookutilities.models.Album;
import com.rhinodesktop.facebookutilities.models.FbPager;

import lombok.Getter;
import timber.log.Timber;

/**
 * Created by rhinodesktop on 2017-01-19.
 */

public abstract class FacebookAlbumGraphRequest extends FacebookGraphRequest {

    private FacebookPhotosForAlbumRequest request;
    @Getter
    private FbPager albumPager;

    public FacebookAlbumGraphRequest(FacebookPhotosForAlbumRequest request) {
        this.request = request;
    }

    @Override
    protected void handleResponse(JsonObject gsonObject, GsonBuilder builder) {
        JsonObject pagingObject = gsonObject.getAsJsonObject("paging");
        JsonArray albumsArray = gsonObject.getAsJsonArray("data");

        Album photoAlbum = null;
        if (albumsArray != null && albumsArray.size() > 0) {
            for (JsonElement albumElement : albumsArray) {
                Album album = builder.create().fromJson(albumElement, Album.class);
                if (album.getName().equals(request.getAlbumName())) {
                    photoAlbum = album;
                    break;
                }
            }
            if (photoAlbum != null) {
                FacebookPhotoManager.getInstance().getAlbumPictures(request, photoAlbum.getId());
            } else {
                albumPager = builder.create().fromJson(pagingObject, FbPager.class);
                FacebookPhotoManager.getInstance().getFbAlbum(this);
            }
        } else {
            userDoesNotHaveAlbum();
        }
    }

    @Override
    protected void handleError(Exception ex) {
        Timber.e("Failed to get the user's Facebook albums");
        ex.printStackTrace();
        userDoesNotHaveAlbum();
    }

    protected abstract void userDoesNotHaveAlbum();
}