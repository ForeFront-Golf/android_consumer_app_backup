package com.rhinoactive.imageutility.amazonfileretrievers;

import androidx.annotation.Nullable;

import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.rhinoactive.imageutility.ToastUtils;
import com.rhinoactive.imageutility.amazontransferutilities.FileCallbackTransferListener;
import com.rhinoactive.imageutility.interfaces.FileCallbackInterface;
import com.rhinoactive.imageutility.models.ToastContextWrapper;

import java.io.File;

/**
 * Created by Hunter Andrin on 2017-10-02.
 */

public class AmazonGetAndCacheFileRetriever extends AmazonCheckCacheFileRetriever {

    private FileCallbackInterface fileCallback;
    @Nullable
    private ToastContextWrapper toastContextWrapper;

    public AmazonGetAndCacheFileRetriever(File s3File, String fileS3Path, FileCallbackInterface fileCallback, @Nullable ToastContextWrapper toastContextWrapper) {
        super(s3File, fileS3Path);
        this.fileCallback = fileCallback;
        this.toastContextWrapper = toastContextWrapper;
    }

    @Override
    protected void handleFileExistsInCache(File amazonFile) {
        fileCallback.callbackHandler(amazonFile);
    }

    @Override
    protected TransferListener getTransferListener() {
        return new FileCallbackTransferListener(s3File, fileCallback);
    }

    @Override
    protected void downloadAndCacheFileFromAmazon(final String fileS3Path, final TransferListener transferListener) {
        if (toastContextWrapper != null) {
            ToastUtils.displayToastFromMainThreadShort(toastContextWrapper.getContext(), toastContextWrapper.getToastMessage());
        }
        super.downloadAndCacheFileFromAmazon(fileS3Path, transferListener);
    }
}
