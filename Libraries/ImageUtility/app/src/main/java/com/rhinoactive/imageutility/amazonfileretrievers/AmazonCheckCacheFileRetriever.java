package com.rhinoactive.imageutility.amazonfileretrievers;

import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.rhinoactive.imageutility.AwsMobileClientUtils;
import com.rhinoactive.imageutility.DeviceStorageFileUtils;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Created by Hunter Andrin on 2017-10-02.
 */

public abstract class AmazonCheckCacheFileRetriever extends AmazonFileRetriever {

    public AmazonCheckCacheFileRetriever(File s3File, String photoS3Path) {
        super(s3File, photoS3Path);
    }

    @Override
    public void downloadFileFromAmazon() {
        try {
            final File amazonFile = getLocalS3File(fileS3Path);
            if (amazonFile != null && amazonFile.exists()) {
                handleFileExistsInCache(amazonFile);
            } else {
                throw new FileNotFoundException("Failed to load file from cache.");
            }
        } catch (FileNotFoundException ex) {
            downloadAndCacheFileFromAmazon(fileS3Path, getTransferListener());
        }
    }

    private static File getLocalS3File(String photoS3Path) throws FileNotFoundException {
        File base = new File(AwsMobileClientUtils.getInstance().getLocalS3FilePath());
        if (!base.exists())
            if (!base.mkdir())
                throw new FileNotFoundException("Failed to create base AWS path: " + base.getAbsolutePath());
        File file = DeviceStorageFileUtils.createLocalFileInCache(photoS3Path);
        if (!file.exists())
            throw new FileNotFoundException("Failed to create AWS file: " + file.getAbsolutePath());
        return file;
    }

    protected abstract void handleFileExistsInCache(File amazonFile);

    protected abstract TransferListener getTransferListener();
}
