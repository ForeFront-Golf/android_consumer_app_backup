package com.rhinodesktop.foreorder_golf_consumer.utils.imagecaptureutils;

import android.app.Activity;

import androidx.appcompat.app.AppCompatActivity;

import com.rhinodesktop.activityanimatorutility.activityutils.externalactivities.ExternalActivity;
import com.rhinodesktop.activityanimatorutility.activityutils.externalactivities.activitieswithresults.ExternalCameraActivity;
import com.rhinodesktop.activityanimatorutility.activityutils.externalactivities.activitieswithresults.ExternalGalleryActivity;

/**
 * Created by hunter on 2018-03-20.
 */

public class ActivityImageCaptureUtils extends ImageCaptureUtils {

    private Activity activity;

    public ActivityImageCaptureUtils(Activity activity) {
        super(activity);
        this.activity = activity;
    }

    @Override
    protected ExternalGalleryActivity createExternalGalleryActivity() {
        return new ExternalGalleryActivity((AppCompatActivity) activity);
    }

    @Override
    protected ExternalActivity createExternalPhotoActivity(String fileProviderAuth) {
        return new ExternalCameraActivity((AppCompatActivity) activity, fileProviderAuth, imageFile);
    }
}
