package com.rhinoactive.imageutility.amazontransferutilities;

import com.rhinoactive.imageutility.interfaces.FileCallbackInterface;

import java.io.File;

/**
 * Created by hunter on 2018-02-21.
 */

public class FileCallbackTransferListener extends StandardTransferListener {

    private FileCallbackInterface callback;

    public FileCallbackTransferListener(File s3File, FileCallbackInterface callback) {
        super(s3File);
        this.callback = callback;
    }

    @Override
    protected void transferComplete() {
        super.transferComplete();
        callback.callbackHandler(s3File);
    }
}
