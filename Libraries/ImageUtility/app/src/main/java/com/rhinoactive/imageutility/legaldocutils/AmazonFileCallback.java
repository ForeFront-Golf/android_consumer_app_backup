package com.rhinoactive.imageutility.legaldocutils;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;

import androidx.core.content.FileProvider;

import com.rhinoactive.imageutility.DeviceStorageFileUtils;
import com.rhinoactive.imageutility.ToastUtils;
import com.rhinoactive.imageutility.interfaces.FileCallbackInterface;
import com.rhinoactive.imageutility.thirdpartycode.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * Created by hunter on 2017-09-12.
 */

public class AmazonFileCallback implements FileCallbackInterface {

    public final static String PDF_MIME_TYPE = "application/pdf";
    public final static String RESOURCE_ERROR = "An error occurred. The type of that resource is unknown.";
    public final static String OPEN_PDF_FAILURE = "Failed to open pdf resource.";
    public final static String NO_PDF_READER = "Your device has no pdf reader installed. Could not open pdf.";
    private final static int WRITE_PERMISSION_REQUEST_CODE = 2354;

    private Activity activity;
    private String s3Path;
    private String fileProviderAuth;
    private PermissionUtils permissionUtils;

    public AmazonFileCallback(Activity activity, String s3Path, String fileProviderAuth) {
        this.activity = activity;
        this.s3Path = s3Path;
        this.fileProviderAuth = fileProviderAuth;
        this.permissionUtils = new PermissionUtils(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE, WRITE_PERMISSION_REQUEST_CODE);
    }

    @Override
    public void callbackHandler(File file) {
        String mimeType = FileUtils.getMimeType(file);
        if (mimeType.equals(PDF_MIME_TYPE)) {
            openPdf(file);
        } else {
            ToastUtils.displayToastFromMainThreadLong(activity, RESOURCE_ERROR);
        }
    }

    private void openPdf(File file) {
        if (!permissionUtils.confirmPermissionGranted()) {
            return;
        }

        try {
            File pdfFile = copyFileToExternalStorage(file);
            if (pdfFile != null && pdfFile.exists()) {
                openPdfActivity(pdfFile);
            } else {
                throw new IOException();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            ToastUtils.displayToastFromMainThreadLong(activity, OPEN_PDF_FAILURE);
        }
    }

    //The file must be moved to external storage in order for an external pdf application to access the file.
    private File copyFileToExternalStorage(File file) throws IOException {
        File pdfFile = DeviceStorageFileUtils.createFileInExternalLocalStorage(s3Path, Environment.DIRECTORY_DOCUMENTS);
        boolean copySuccessful = FileUtils.copyFile(file, pdfFile);
        if (!copySuccessful) {
            pdfFile = null;
        }
        return pdfFile;
    }

    private void openPdfActivity(File pdfFile) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (intent.resolveActivity(activity.getPackageManager()) != null) {
            Uri fileUri = FileProvider.getUriForFile(activity, fileProviderAuth, pdfFile);
            intent.setDataAndType(fileUri, "application/pdf");
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_ACTIVITY_NO_HISTORY);
            activity.startActivity(intent);
        } else {
            ToastUtils.displayToastFromMainThreadLong(activity, NO_PDF_READER);
        }
    }
}
