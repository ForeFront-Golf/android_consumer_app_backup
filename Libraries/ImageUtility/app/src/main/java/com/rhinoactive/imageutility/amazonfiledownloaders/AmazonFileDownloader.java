package com.rhinoactive.imageutility.amazonfiledownloaders;

import androidx.annotation.Nullable;

import com.rhinoactive.imageutility.amazonfileretrievers.AmazonFileRetriever;
import com.rhinoactive.imageutility.amazonfileretrievers.AmazonGetAndCacheFileRetriever;
import com.rhinoactive.imageutility.interfaces.FileCallbackInterface;
import com.rhinoactive.imageutility.models.ToastContextWrapper;

import java.io.File;

/**
 * Created by hunter on 2018-02-23.
 */

// Displays a toast with the message "downloading" when it has to download the file from amazon. Useful for when you need
// to download a pdf or some other large file that might take some time to download.
public class AmazonFileDownloader extends AmazonDownloader {

    private FileCallbackInterface fileCallback;
    @Nullable
    private ToastContextWrapper toastContextWrapper;

    public AmazonFileDownloader(FileCallbackInterface fileCallback, @Nullable ToastContextWrapper toastContextWrapper) {
        this.fileCallback = fileCallback;
        this.toastContextWrapper = toastContextWrapper;
    }

    @Override
    protected void retrieveS3File(File s3File, String s3FilePath) {
        AmazonFileRetriever amazonFileRetriever = new AmazonGetAndCacheFileRetriever(s3File, s3FilePath, fileCallback, toastContextWrapper);
        amazonFileRetriever.downloadFileFromAmazon();
    }
}
