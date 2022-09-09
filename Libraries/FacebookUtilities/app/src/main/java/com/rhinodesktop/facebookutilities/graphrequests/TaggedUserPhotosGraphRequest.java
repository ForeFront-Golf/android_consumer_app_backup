package com.rhinodesktop.facebookutilities.graphrequests;

import com.rhinodesktop.facebookutilities.FacebookPhotoManager;
import com.rhinodesktop.facebookutilities.models.FbPager;

import lombok.Getter;

/**
 * Created by rhinodesktop on 2017-01-19.
 */

public class TaggedUserPhotosGraphRequest extends UserPhotosGraphRequest {

    @Getter
    private static boolean isRetrievingTaggedPhotos;

    public TaggedUserPhotosGraphRequest() {
        isRetrievingTaggedPhotos = true;
    }

    @Override
    protected void doneRetrievingPhotos() {
        isRetrievingTaggedPhotos = false;
    }

    @Override
    protected void setFbPager(FbPager fbPager) {
        FacebookPhotoManager.getInstance().getCurrentUserFbPhotosAndPagers().setFbTaggedPager(fbPager);
    }

    @Override
    protected void setHasNoMoreImages() {
        FacebookPhotoManager.getInstance().getCurrentUserFbPhotosAndPagers().setHasMoreTaggedImages(false);
    }
}
