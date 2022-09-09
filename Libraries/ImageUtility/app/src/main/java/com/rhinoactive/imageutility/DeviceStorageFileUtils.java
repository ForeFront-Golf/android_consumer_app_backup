package com.rhinoactive.imageutility;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * Created by Hunter Andrin on 2017-10-02.
 */

/**
 * Internal library class
 */
public class DeviceStorageFileUtils {

    /**
     *
     * @param s3Path The
     * @param environmentDirectory The directory to save the file to the ExternalStoragePublicDirectory
     *                             ex. {@link Environment#DIRECTORY_PICTURES}
     * @return The
     */
    public static File createFileInExternalLocalStorage(String s3Path, String environmentDirectory) throws IOException {
        File storageDir = Environment.getExternalStoragePublicDirectory(environmentDirectory);
        File file = new File(storageDir, s3Path);

        File base = file.getParentFile();
        if (!base.exists()) {
            base.mkdirs();
        }
        if (!file.exists()) {
            file.createNewFile();
        }
        return file;
    }

    public static File downloadFileFromUrl(String imageLink, String photoS3Path) throws IOException {
        final File imageFile = createLocalFileInCache(photoS3Path);
        URL url = new URL(imageLink);
        FileUtils.copyURLToFile(url, imageFile);
        return imageFile;
    }

    public static File moveImageFileToAppLocalCache(Context context, Uri imgUri, String photoS3Path) throws IOException {
        final File imageFile;
        String path = com.rhinoactive.imageutility.thirdpartycode.FileUtils.getPath(context, imgUri);
        if (path != null) {
            File inFile = new File(path);
            imageFile = createLocalFileInCache(photoS3Path);
            FileUtils.copyFile(inFile, imageFile);
        } else {
            throw new IOException();
        }
        return imageFile;
    }

    public static File createLocalFileInCache(String photoS3Path) {
        return new File(AwsMobileClientUtils.getInstance().getLocalS3FilePath(), photoS3Path);
    }
}
