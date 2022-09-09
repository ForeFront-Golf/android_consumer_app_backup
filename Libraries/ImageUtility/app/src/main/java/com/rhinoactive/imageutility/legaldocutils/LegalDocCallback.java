package com.rhinoactive.imageutility.legaldocutils;

import android.app.Activity;

import com.rhinoactive.imageutility.Constants;
import com.rhinoactive.imageutility.amazonfiledownloaders.AmazonFileDownloader;
import com.rhinoactive.imageutility.interfaces.FileCallbackInterface;
import com.rhinoactive.imageutility.models.ToastContextWrapper;

/**
 * Created by hunter on 2017-09-12.
 */

public class LegalDocCallback {

    private Activity activity;
    private String docS3Path;
    private String fileProviderAuth;

    public LegalDocCallback(Activity activity, String docS3Path, String fileProviderAuth) {
        this.activity = activity;
        this.docS3Path = docS3Path;
        this.fileProviderAuth = fileProviderAuth;
    }

    public void getAndDisplayLegalDoc() {
        FileCallbackInterface callback = new AmazonFileCallback(activity, docS3Path, fileProviderAuth);
        AmazonFileDownloader amazonFileDownloader = new AmazonFileDownloader(callback, new ToastContextWrapper(activity, Constants.DOWNLOADING));
        amazonFileDownloader.downloadFile(docS3Path);
    }
}
