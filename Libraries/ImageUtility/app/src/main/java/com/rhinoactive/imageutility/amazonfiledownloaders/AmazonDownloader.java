package com.rhinoactive.imageutility.amazonfiledownloaders;

import com.rhinoactive.imageutility.DeviceStorageFileUtils;
import com.rhinoactive.imageutility.SeparateThreadRunner;

import java.io.File;

/**
 * Created by hunter on 2018-02-26.
 */

public abstract class AmazonDownloader {

    protected abstract void retrieveS3File(File s3File, String s3FilePath);

    public void downloadFile(final String s3FilePath) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (s3FilePath == null) {
                    return;
                }
                File s3File = DeviceStorageFileUtils.createLocalFileInCache(s3FilePath);
                retrieveS3File(s3File, s3FilePath);
            }
        };
        SeparateThreadRunner.runFromSeparateThread(runnable);
    }
}
