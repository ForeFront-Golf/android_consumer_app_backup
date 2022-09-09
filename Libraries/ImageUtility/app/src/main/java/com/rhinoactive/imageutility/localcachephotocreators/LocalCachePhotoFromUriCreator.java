package com.rhinoactive.imageutility.localcachephotocreators;

import android.content.Context;
import android.net.Uri;

import com.rhinoactive.imageutility.DeviceStorageFileUtils;

import java.io.File;
import java.io.IOException;

/**
 * Created by rhinodesktop on 2017-02-08.
 */

public class LocalCachePhotoFromUriCreator extends LocalCachePhotoCreator {

    private Uri imageUri;
    private Context context;

    /**
     *
     * @param imageUri: The location of the image of the content provider "https://developer.android.com/guide/topics/providers/content-provider-basics.html"
     */
    public LocalCachePhotoFromUriCreator(Context context, Uri imageUri) {
        this.context = context;
        this.imageUri = imageUri;
    }

    @Override
    public File createImageFileInLocalCache(String s3Path) throws IOException {
        return DeviceStorageFileUtils.moveImageFileToAppLocalCache(context, imageUri, s3Path);
    }
}
