package com.rhinoactive.imageutility.amazonfileretrievers;

import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.rhinoactive.imageutility.AwsMobileClientUtils;

import java.io.File;

/**
 * Created by Hunter Andrin on 2017-09-29.
 */

public abstract class AmazonFileRetriever {

    protected File s3File;
    protected String fileS3Path;

    public AmazonFileRetriever(File s3File, String fileS3Path) {
        this.s3File = s3File;
        this.fileS3Path = fileS3Path;
    }

    public abstract void downloadFileFromAmazon();

    protected void downloadAndCacheFileFromAmazon(String fileS3Path, TransferListener transferListener) {
        TransferUtility transferUtility = AwsMobileClientUtils.getInstance().getTransferUtility();
        TransferObserver transferObserver = transferUtility.download(fileS3Path, s3File);
        transferObserver.setTransferListener(transferListener);
    }
}
