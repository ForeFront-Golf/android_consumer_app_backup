package com.rhinoactive.imageutility.amazonimageuploaders;

import android.content.Context;
import android.net.Uri;

import com.rhinoactive.imageutility.DeviceStorageFileUtils;

import java.io.File;
import java.io.IOException;

/**
 * Created by hunter on 2018-01-10.
 */

public class AmazonImageUriUploader extends AmazonImageUploader {

    private Context context;
    private Uri userPhoto;

    public AmazonImageUriUploader(Context context, Uri userPhoto) {
        this.context = context;
        this.userPhoto = userPhoto;
    }

    @Override
    protected File getFile(String s3Path) throws IOException {
        return DeviceStorageFileUtils.moveImageFileToAppLocalCache(context, userPhoto, s3Path);
    }
}
