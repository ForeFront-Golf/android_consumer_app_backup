package com.rhinoactive.imageutility.imagefileloaders;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.request.RequestOptions;
import com.rhinoactive.imageutility.GlideImageViewLoader;
import com.rhinoactive.imageutility.interfaces.ImageFileLoaderInterface;

import java.io.File;

import timber.log.Timber;

/**
 * Created by rhinodesktop on 2017-01-12.
 */

public class AsynchronousImageFileLoader implements ImageFileLoaderInterface {

    protected RequestOptions requestOptions;

    public AsynchronousImageFileLoader(RequestOptions requestOptions) {
        this.requestOptions = requestOptions;
    }

    @Override
    public void loadImageIntoImageView(final ImageView imageView, final File imageFile) throws IllegalArgumentException {
        Handler mainHandler = new Handler(Looper.getMainLooper());
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                loadImageUsingGlide(imageView, imageFile);
            }
        });
    }

    private void loadImageUsingGlide(ImageView imageView, File imageFile) {
        try {
            GlideImageViewLoader glideImageViewLoader = new GlideImageViewLoader(imageView);
            glideImageViewLoader.setRequestOptions(requestOptions);
            glideImageViewLoader.loadImageIntoImageView(imageFile);
        } catch (IllegalArgumentException ex) {
            Timber.e("Activity is destroyed, cannot load image into imageview: %s", ex.getMessage());
        }
    }
}
