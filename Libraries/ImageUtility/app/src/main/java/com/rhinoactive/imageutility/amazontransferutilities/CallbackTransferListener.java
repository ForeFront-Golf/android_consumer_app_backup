package com.rhinoactive.imageutility.amazontransferutilities;

import com.rhinoactive.imageutility.interfaces.CallbackInterface;

import java.io.File;

/**
 * Created by hunter on 2018-02-21.
 */

public class CallbackTransferListener extends StandardTransferListener {

    private CallbackInterface callback;

    public CallbackTransferListener(File s3File, CallbackInterface callback) {
        super(s3File);
        this.callback = callback;
    }

    @Override
    public void transferComplete() {
        super.transferComplete();
        callback.callbackHandlerUploadSuccessful();
    }

    @Override
    public void onError(int id, Exception ex) {
        super.onError(id, ex);
        callback.callbackHandlerUploadFailed();
    }
}
