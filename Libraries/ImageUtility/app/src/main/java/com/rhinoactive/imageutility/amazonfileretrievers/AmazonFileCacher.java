package com.rhinoactive.imageutility.amazonfileretrievers;

import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.rhinoactive.imageutility.amazontransferutilities.StandardTransferListener;

import java.io.File;

/**
 * Created by Hunter Andrin on 2017-10-02.
 */

public class AmazonFileCacher extends AmazonCheckCacheFileRetriever {

    public AmazonFileCacher(File s3File, String photoS3Path) {
        super(s3File, photoS3Path);
    }

    @Override
    protected void handleFileExistsInCache(File amazonFile) {
        //Do nothing as the file already exists in the cache.
    }

    @Override
    protected TransferListener getTransferListener() {
        return new StandardTransferListener(s3File);
    }
}
