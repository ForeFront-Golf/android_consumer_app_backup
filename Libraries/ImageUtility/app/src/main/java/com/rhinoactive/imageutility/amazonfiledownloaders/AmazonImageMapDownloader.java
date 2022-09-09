package com.rhinoactive.imageutility.amazonfiledownloaders;

import android.widget.ImageView;

import com.rhinoactive.imageutility.imageviewloaders.ImageViewLoader;
import com.rhinoactive.imageutility.imageviewloaders.MapImageViewLoader;
import com.rhinoactive.imageutility.interfaces.CallbackInterface;

import java.io.File;

/**
 * Created by hunter on 2018-02-23.
 */

public class AmazonImageMapDownloader extends AmazonImageDownloader {

    private CallbackInterface callback;

    public AmazonImageMapDownloader(ImageView imageView, CallbackInterface callback) {
        super(imageView);
        this.callback = callback;
    }

    @Override
    protected ImageViewLoader createRegularImageViewLoader(File s3File, String photoS3Path) {
        return new MapImageViewLoader(s3File, imageView, photoS3Path, checkCacheForFile, callback);
    }
}
