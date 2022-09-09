package com.rhinoactive.imageutility.imageviewloaders;

import android.widget.ImageView;

import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.rhinoactive.imageutility.amazonfileretrievers.AmazonFileRetriever;
import com.rhinoactive.imageutility.amazonfileretrievers.AmazonImageViewPhotoRetriever;
import com.rhinoactive.imageutility.amazonfileretrievers.AmazonNoCacheCheckFileRetriever;
import com.rhinoactive.imageutility.interfaces.ImageFileLoaderInterface;

import java.io.File;

/**
 * Created by Hunter Andrin on 2017-10-02.
 */

public abstract class ImageViewLoader {

    protected File s3File;
    protected ImageView imageView;
    protected String photoS3Path;
    protected boolean checkCacheForImage;

    public ImageViewLoader(File s3File, ImageView imageView, String photoS3Path, boolean checkCacheForImage) {
        this.s3File = s3File;
        this.imageView = imageView;
        this.photoS3Path = photoS3Path;
        this.checkCacheForImage = checkCacheForImage;
    }

    public void loadImage() {
        ImageFileLoaderInterface imageFileLoader = createImageFileLoader();
        final TransferListener transferListener = createTransferListener(imageFileLoader);
        AmazonFileRetriever amazonFileRetriever = createAmazonFileRetriever(imageFileLoader, transferListener);
        amazonFileRetriever.downloadFileFromAmazon();
    }

    private AmazonFileRetriever createAmazonFileRetriever(ImageFileLoaderInterface imageFileLoader, TransferListener transferListener) {
        AmazonFileRetriever amazonFileRetriever;
        if (checkCacheForImage) {
            amazonFileRetriever = new AmazonImageViewPhotoRetriever(s3File, photoS3Path, imageView, transferListener, imageFileLoader);
        } else {
            amazonFileRetriever = new AmazonNoCacheCheckFileRetriever(s3File, photoS3Path, transferListener);
        }
        return amazonFileRetriever;
    }

    protected abstract ImageFileLoaderInterface createImageFileLoader();

    protected abstract TransferListener createTransferListener(ImageFileLoaderInterface imageFileLoader);
}
