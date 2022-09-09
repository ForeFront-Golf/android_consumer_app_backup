package com.rhinodesktop.facebookutilities.graphrequests;

import com.rhinodesktop.facebookutilities.FacebookPhotoManager;
import com.rhinodesktop.facebookutilities.models.FbPager;

import lombok.Getter;

/**
 * Created by rhinodesktop on 2017-01-19.
 */

public class UploadedUserPhotosGraphRequest extends UserPhotosGraphRequest {

    @Getter
    private static boolean isRetrievingUploadedPhotos;

    public UploadedUserPhotosGraphRequest() {
        isRetrievingUploadedPhotos = true;
    }

    @Override
    protected void doneRetrievingPhotos() {
        isRetrievingUploadedPhotos = false;
    }

    @Override
    protected void setFbPager(FbPager fbPager) {
        FacebookPhotoManager.getInstance().getCurrentUserFbPhotosAndPagers().setFbUploadedPager(fbPager);
    }

    @Override
    protected void setHasNoMoreImages() {
        FacebookPhotoManager.getInstance().getCurrentUserFbPhotosAndPagers().setHasMoreUploadedImages(false);
    }
}
