package com.rhinoactive.imageutility.amazonimageuploaders;

import com.rhinoactive.imageutility.DeviceStorageFileUtils;

import java.io.File;
import java.io.IOException;

/**
 * Created by hunter on 2018-01-10.
 */

public class AmazonImageLinkUploader extends AmazonImageUploader {

    private String userPhotoLink;

    public AmazonImageLinkUploader(String userPhotoLink) {
        this.userPhotoLink = userPhotoLink;
    }

    @Override
    protected File getFile(String s3Path) throws IOException {
        return DeviceStorageFileUtils.downloadFileFromUrl(userPhotoLink, s3Path);
    }
}

