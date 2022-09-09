package com.rhinoactive.imageutility.amazonfileretrievers;

import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;

import java.io.File;

/**
 * Created by Hunter Andrin on 2017-09-29.
 */

public class AmazonNoCacheCheckFileRetriever extends AmazonFileRetriever {

    private TransferListener transferListener;

    public AmazonNoCacheCheckFileRetriever(File s3File, String photoS3Path, TransferListener transferListener) {
        super(s3File, photoS3Path);
        this.transferListener = transferListener;
    }

    @Override
    public void downloadFileFromAmazon() {
        // If a newer image exists it will download the image and then call the transferListener's onSuccess method.
        // If a newer image does not exist then it will just call the transferListener's onSuccess method.
        downloadAndCacheFileFromAmazon(fileS3Path, transferListener);
    }
}
