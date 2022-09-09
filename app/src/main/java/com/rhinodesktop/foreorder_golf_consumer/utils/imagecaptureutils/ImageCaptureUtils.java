package com.rhinodesktop.foreorder_golf_consumer.utils.imagecaptureutils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.rhinoactive.foreorder_library_android.utils.Constants;
import com.rhinoactive.generalutilities.interfaces.ListSelectionCallback;
import com.rhinoactive.imageutility.DeviceStorageFileUtils;
import com.rhinoactive.permissionutilities.ActivityPermissionManager;
import com.rhinoactive.permissionutilities.PermissionManager;
import com.rhinoactive.permissionutilities.interfaces.PermissionRequestResultCallback;
import com.rhinoactive.permissionutilities.models.Permission;
import com.rhinodesktop.activityanimatorutility.activityutils.externalactivities.ExternalActivity;
import com.rhinodesktop.activityanimatorutility.activityutils.externalactivities.activitieswithresults.ExternalGalleryActivity;
import com.rhinodesktop.foreorder_golf_consumer.R;
import com.rhinodesktop.foreorder_golf_consumer.models.enums.PhotoUploadOption;
import com.rhinodesktop.foreorder_golf_consumer.utils.ForeOrderDialogUtils;
import com.rhinodesktop.foreorder_golf_consumer.utils.ForeOrderResourceUtils;
import com.rhinodesktop.foreorder_golf_consumer.utils.ForeOrderToastUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import lombok.NonNull;
import timber.log.Timber;

import static android.app.Activity.RESULT_OK;

/**
 * Created by hunter on 2018-03-19.
 */

public abstract class ImageCaptureUtils {

    protected File imageFile;
    private Context context;
    private PermissionManager permissionManager;
    private String externalImageFilePath;
    private ForeOrderResourceUtils resourceUtils;

    protected abstract ExternalGalleryActivity createExternalGalleryActivity();

    protected abstract ExternalActivity createExternalPhotoActivity(String fileProviderAuth);

    public ImageCaptureUtils(Activity activity) {
        this.context = activity;
        resourceUtils = ForeOrderResourceUtils.getInstance();
        permissionManager = new ActivityPermissionManager(Permission.WRITE_EXTERNAL_STORAGE_PERMISSION, (AppCompatActivity) activity);
    }

    public void createPhotoDialogIfPermissionGranted() {
        if (permissionManager.isPermissionGranted()) {
            createPhotoDialog();
        } else {
            String permissionRationale = resourceUtils.strRes(R.string.request_write_permission);
            String permissionTitle = resourceUtils.strRes(R.string.request_permission);
            permissionManager.requestPermissionWithRationalDialog(permissionRationale, permissionTitle);
        }
    }

    public void handleOnRequestPermissionResult(final int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionManager.handlePermissionResult(requestCode, permissions, grantResults, new PermissionRequestResultCallback() {
            @Override
            public void permissionGranted() {}

            @Override
            public void permissionDenied() {
                String permissionRequiredContent = resourceUtils.strRes(R.string.request_write_permission);
                String permissionRequiredTitle = resourceUtils.strRes(R.string.no_write_permission);
                permissionManager.displayPermissionDeniedDialog(false, permissionRequiredContent, permissionRequiredTitle);
            }
        });
    }

    @Nullable
    public File handleOnActivityResults(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_CANCELED) {
            return null;
        }

        File imageFile = null;
        if(resultCode==RESULT_OK) {
            if (requestCode == com.rhinodesktop.activityanimatorutility.Constants.TAKE_PIC_RQST) {
                imageFile = handleTakePhotoResult();
            } else if (requestCode == com.rhinodesktop.activityanimatorutility.Constants.PICK_IMG_RQST) {
                imageFile = handleSelectPhotoResult(data);
            }
        }
        if (imageFile == null) {
            ForeOrderToastUtils.getInstance().displayToastFromMainThreadLong(resourceUtils.strRes(R.string.error_uploading_profile_photo));
        }
        return imageFile;
    }

    @Nullable
    private File handleTakePhotoResult() {
        if (imageFile == null || !imageFile.exists()) {
            imageFile = null;
        }
        return imageFile;
    }

    @Nullable
    private File handleSelectPhotoResult(Intent data) {
        File galleryPhotoFile = null;
        Uri userPhoto = data.getData();
        if (userPhoto != null) {
            setExternalImageFilePath();
            try {
                galleryPhotoFile = DeviceStorageFileUtils.moveImageFileToAppLocalCache(context, userPhoto, externalImageFilePath);
            } catch (IOException ex) {
                String errorMsg = String.format("Error uploading Photo: %s", ex.getMessage());
                Timber.e(errorMsg);
            }
        }
        return galleryPhotoFile;
    }

    private void createPhotoDialog() {
        //initiate take picture / from file / cancel dialog
        final List<String> addPhotoOptions = Arrays.asList(PhotoUploadOption.FILE.getTitle(), PhotoUploadOption.PHOTO.getTitle());
        ForeOrderDialogUtils.getInstance().showListDialog(context, resourceUtils.strRes(R.string.add_photo), addPhotoOptions,
                new ListSelectionCallback() {
                    @Override
                    public void selected(int position, String str) {
                        if (str.equals(PhotoUploadOption.FILE.getTitle())) {
                            ExternalActivity externalActivity = createExternalGalleryActivity();
                            externalActivity.start();
                        } else if (str.equals(PhotoUploadOption.PHOTO.getTitle())) {
                            goToCamera();
                        }
                    }
                });
    }

    private void goToCamera() {
        //We must provide the file of where the image will be saved so that we can get the full size version of the photo.
        try {
            createImageFile();
            if (imageFile.exists()) {
                String fileProviderAuth = resourceUtils.strRes(R.string.file_provider_authority);
                ExternalActivity externalActivity = createExternalPhotoActivity(fileProviderAuth);
                externalActivity.start();
            } else {
                throw new IOException();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            String errorMsg = resourceUtils.strRes(R.string.error_creating_photo);
            ForeOrderToastUtils.getInstance().displayToastFromMainThreadLong(errorMsg);
        }
    }

    private void createImageFile() throws IOException {
        setExternalImageFilePath();
        imageFile = DeviceStorageFileUtils.createFileInExternalLocalStorage(externalImageFilePath, Environment.DIRECTORY_PICTURES);
    }

    private void setExternalImageFilePath() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(new Date());
        externalImageFilePath = String.format(Locale.ENGLISH, "public/fore_order/%s.%s", timeStamp, Constants.JPG);
    }
}