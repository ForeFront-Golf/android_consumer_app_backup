package com.rhinoactive.imageutility.imageviewloaders;

import android.widget.ImageView;

import com.bumptech.glide.request.RequestOptions;
import com.rhinoactive.imageutility.imagefileloaders.AsynchronousImageFileLoader;
import com.rhinoactive.imageutility.interfaces.ImageFileLoaderInterface;

import java.io.File;

/**
 * Created by hunter on 2018-02-27.
 */

public abstract class GlideImageViewLoader extends ImageViewLoader {

    private RequestOptions requestOptions;

    public GlideImageViewLoader(File s3File, ImageView imageView, String photoS3Path, boolean checkCacheForImage, RequestOptions requestOptions) {
        super(s3File, imageView, photoS3Path, checkCacheForImage);
        this.requestOptions = requestOptions;
    }

    @Override
    protected ImageFileLoaderInterface createImageFileLoader() {
        return new AsynchronousImageFileLoader(requestOptions);
    }
}
