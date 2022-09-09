package com.rhinoactive.imageutility.amazonfiledownloaders;

import android.widget.ImageView;

import com.bumptech.glide.request.RequestOptions;
import com.rhinoactive.imageutility.imageviewloaders.ImageViewLoader;
import com.rhinoactive.imageutility.imageviewloaders.RegularImageViewLoader;

import java.io.File;

/**
 * Created by hunter on 2018-02-23.
 */

public class AmazonImageViewDownloader extends AmazonImageGlideDownloader {


    public AmazonImageViewDownloader(ImageView imageView) {
        super(imageView);
    }

    public AmazonImageViewDownloader(ImageView imageView, RequestOptions requestOptions) {
        super(imageView, requestOptions);
    }

    @Override
    protected ImageViewLoader createRegularImageViewLoader(File s3File, String photoS3Path) {
        return new RegularImageViewLoader(s3File, imageView, photoS3Path, checkCacheForFile, requestOptions);
    }
}
