package com.rhinoactive.imageutility.imageviewloaders;

import android.widget.ImageView;

import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.bumptech.glide.request.RequestOptions;
import com.rhinoactive.imageutility.amazontransferutilities.DisplayImageTransferListener;
import com.rhinoactive.imageutility.interfaces.ImageFileLoaderInterface;

import java.io.File;

/**
 * Created by Hunter Andrin on 2017-10-02.
 */

public class RegularImageViewLoader extends GlideImageViewLoader {

    public RegularImageViewLoader(File s3File, ImageView imageView, String photoS3Path, boolean checkCacheForImage, RequestOptions requestOptions) {
        super(s3File, imageView, photoS3Path, checkCacheForImage, requestOptions);
    }

    @Override
    protected TransferListener createTransferListener(ImageFileLoaderInterface imageFileLoader) {
        return new DisplayImageTransferListener(s3File, imageView, imageFileLoader);
    }
}
