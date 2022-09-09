package com.rhinoactive.imageutility.amazonfiledownloaders;

import android.widget.ImageView;

import com.rhinoactive.imageutility.imageviewloaders.ImageViewLoader;

import java.io.File;

/**
 * Created by hunter on 2018-02-23.
 */

// These methods check the app's cache for the photo before downloading them from amazon. If the file exists in
// the cache then the file will not be re-downloaded from amazon. These methods however will not download newer versions
// of the image if a newer one exists on amazon. The advantage of these methods is that these methods are much
// faster if the image already exists in the cache.

public abstract class AmazonImageDownloader extends AmazonDownloader {

    protected abstract ImageViewLoader createRegularImageViewLoader(File s3File, String photoS3Path);

    protected ImageView imageView;
    protected boolean checkCacheForFile = true;

    public AmazonImageDownloader(ImageView imageView) {
        this.imageView = imageView;
    }

    /**
     * Call this method if you do not want to check if the amazon s3 file exists in this cache before
     * attempting to download the file from amazon. In most cases you want to check if the file
     * is in the cache as that reduces image loading time, and data usage. An example use case for this
     * functionality is if an image file is modified and you need to download the updated version from
     * amazon.
     */
    public void doNotCheckCacheForFile() {
        checkCacheForFile = false;
    }

    @Override
    protected void retrieveS3File(File s3File, String s3FilePath) {
        ImageViewLoader imageViewLoader = createRegularImageViewLoader(s3File, s3FilePath);
        imageViewLoader.loadImage();
    }
}
