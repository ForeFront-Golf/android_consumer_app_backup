package com.rhinoactive.imageutility.amazonfileretrievers;

import android.widget.ImageView;

import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.rhinoactive.imageutility.interfaces.ImageFileLoaderInterface;

import java.io.File;

/**
 * Created by Hunter Andrin on 2017-10-02.
 */

public class AmazonImageViewPhotoRetriever extends AmazonCheckCacheFileRetriever {

    private ImageView imageView;
    private TransferListener transferListener;
    private ImageFileLoaderInterface imageFileLoader;

    public AmazonImageViewPhotoRetriever(File s3File, String photoS3Path, ImageView imageView,
                                         TransferListener transferListener, ImageFileLoaderInterface imageFileLoader) {
        super(s3File, photoS3Path);
        this.imageView = imageView;
        this.transferListener = transferListener;
        this.imageFileLoader = imageFileLoader;
    }

    @Override
    protected void handleFileExistsInCache(File amazonFile) {
        imageFileLoader.loadImageIntoImageView(imageView, amazonFile);
    }

    @Override
    protected TransferListener getTransferListener() {
        return this.transferListener;
    }
}
