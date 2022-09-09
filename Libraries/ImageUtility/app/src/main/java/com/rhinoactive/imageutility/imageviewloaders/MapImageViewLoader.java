package com.rhinoactive.imageutility.imageviewloaders;

import android.widget.ImageView;

import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.rhinoactive.imageutility.amazontransferutilities.CallbackDisplayTransferListener;
import com.rhinoactive.imageutility.imagefileloaders.SynchronousImageFileLoader;
import com.rhinoactive.imageutility.interfaces.CallbackInterface;
import com.rhinoactive.imageutility.interfaces.ImageFileLoaderInterface;

import java.io.File;

/**
 * Created by Hunter Andrin on 2017-10-02.
 */

public class MapImageViewLoader extends ImageViewLoader {

    private CallbackInterface callback;

    public MapImageViewLoader(File s3File, ImageView imageView, String photoS3Path, boolean checkCacheForImage, CallbackInterface callback) {
        super(s3File, imageView, photoS3Path, checkCacheForImage);
        this.callback = callback;
    }

    @Override
    protected ImageFileLoaderInterface createImageFileLoader() {
        return new SynchronousImageFileLoader();
    }

    @Override
    protected TransferListener createTransferListener(ImageFileLoaderInterface imageFileLoader) {
        return new CallbackDisplayTransferListener(s3File, imageView, callback, imageFileLoader);
    }
}
