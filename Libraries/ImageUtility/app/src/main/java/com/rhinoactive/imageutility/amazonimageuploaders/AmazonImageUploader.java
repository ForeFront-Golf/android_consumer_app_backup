package com.rhinoactive.imageutility.amazonimageuploaders;

import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.rhinoactive.imageutility.AwsMobileClientUtils;
import com.rhinoactive.imageutility.amazontransferutilities.CallbackTransferListener;
import com.rhinoactive.imageutility.interfaces.CallbackInterface;

import java.io.File;
import java.io.IOException;

/**
 * Created by hunter on 2018-01-10.
 */

public abstract class AmazonImageUploader {

    protected abstract File getFile(String s3Path) throws IOException;

    public void uploadUserImageToAmazon(String s3Path, CallbackInterface callback) {
        try {
            File file = getFile(s3Path);
            uploadFileToAmazon(file, s3Path, callback);
        } catch (IOException e) {
            callback.callbackHandlerUploadFailed();
        }
    }

    private void uploadFileToAmazon(File file, String fileS3Path, CallbackInterface callback) {
        if (!file.exists() || fileS3Path == null) {
            return;
        }
        TransferListener transferListener = new CallbackTransferListener(file, callback);
        TransferUtility transferUtility = AwsMobileClientUtils.getInstance().getTransferUtility();
        TransferObserver transferObserver = transferUtility.upload(fileS3Path, file);
        transferObserver.setTransferListener(transferListener);
    }
}
