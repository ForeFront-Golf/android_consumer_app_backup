package com.rhinodesktop.facebookutilities.graphrequests;

import android.util.Log;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.rhinodesktop.facebookutilities.FacebookPhotoManager;
import com.rhinodesktop.facebookutilities.events.FacebookImagesDownloadEvent;
import com.rhinodesktop.facebookutilities.models.FbPager;
import com.rhinodesktop.facebookutilities.models.FbPhoto;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import timber.log.Timber;

/**
 * Created by rhinodesktop on 2017-01-19.
 */

public abstract class UserPhotosGraphRequest extends FacebookGraphRequest {

    @Override
    protected void handleResponse(JsonObject gsonObject, GsonBuilder builder) {
        JsonObject pagingObject = gsonObject.getAsJsonObject("paging");
        FbPager fbPager = builder.create().fromJson(pagingObject, FbPager.class);
        JsonArray photosArray = gsonObject.getAsJsonArray("data");
        if (photosArray != null && photosArray.size() > 0) {
            for (JsonElement photoElement : photosArray) {
                FbPhoto fbPhoto = builder.create().fromJson(photoElement, FbPhoto.class);
                addIfDoesNotAlreadyExist(FacebookPhotoManager.getInstance().getCurrentUserFbPhotosAndPagers().getFbPhotos(), fbPhoto);
            }
        } else {
            setHasNoMoreImages();
        }
        setFbPager(fbPager);
        doneRetrievingPhotos();
        notifyObservers();
    }

    @Override
    protected void handleError(Exception ex) {
        doneRetrievingPhotos();
        Timber.e("Failed to get the user's Facebook albums");
        ex.printStackTrace();
    }

    private void addIfDoesNotAlreadyExist(List<FbPhoto> fbPhotos, FbPhoto userPhotoToAdd) {
        boolean addPhoto = true;
        for (FbPhoto fbPhoto : fbPhotos) {
            if (fbPhoto.getId().equals(userPhotoToAdd.getId())) {
                addPhoto = false;
                break;
            }
        }
        if (addPhoto) {
            fbPhotos.add(userPhotoToAdd);
        }
    }

    private void notifyObservers() {
        EventBus.getDefault().post(new FacebookImagesDownloadEvent());
    }

    protected abstract void doneRetrievingPhotos();

    protected abstract void setFbPager(FbPager fbPager);

    protected abstract void setHasNoMoreImages();
}
