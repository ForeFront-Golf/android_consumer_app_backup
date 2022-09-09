package com.rhinoactive.imageutility.amazontransferutilities;

import android.widget.ImageView;

import com.rhinoactive.imageutility.interfaces.CallbackInterface;
import com.rhinoactive.imageutility.interfaces.ImageFileLoaderInterface;

import java.io.File;

/**
 * Created by hunter on 2018-02-21.
 */

public class CallbackDisplayTransferListener extends DisplayImageTransferListener {

    private CallbackInterface callback;

    public CallbackDisplayTransferListener(File s3File, ImageView profileImg, CallbackInterface callback,
                                           ImageFileLoaderInterface imageFileLoader) {
        super(s3File, profileImg, imageFileLoader);
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
