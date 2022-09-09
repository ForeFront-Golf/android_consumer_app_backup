package com.rhinoactive.imageutility.amazontransferutilities;

import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.rhinoactive.imageutility.DeviceStorageFileUtils;
import com.rhinoactive.imageutility.SeparateThreadRunner;

import java.io.File;

/**
 * Created by hunter on 2018-02-21.
 */

public class StandardTransferListener implements TransferListener {

    protected void transferComplete() {}

    protected File s3File;

    public StandardTransferListener(File s3File) {
        this.s3File = s3File;
    }

    public File getS3File() {
        return s3File;
    }

    @Override
    public void onStateChanged(int id, final TransferState state) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (TransferState.COMPLETED == state) {
                    transferComplete();
                }
            }
        };
        SeparateThreadRunner.runFromSeparateThread(runnable);
    }

    @Override
    public void onError(int id, Exception ex) {
        s3File.delete();
    }

    @Override
    public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {}
}
