package com.rhinoactive.imageutility.localcachephotocreators;

import com.rhinoactive.imageutility.DeviceStorageFileUtils;

import java.io.File;
import java.io.IOException;

/**
 * Created by rhinodesktop on 2017-02-08.
 */

public class LocalCachePhotoFromLinkCreator extends LocalCachePhotoCreator {

    private String imgLink;

    public LocalCachePhotoFromLinkCreator(String imgLink) {
        this.imgLink = imgLink;
    }

    @Override
    public File createImageFileInLocalCache(String s3Path) throws IOException {
        return DeviceStorageFileUtils.downloadFileFromUrl(imgLink, s3Path);
    }
}
