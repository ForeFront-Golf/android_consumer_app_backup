package com.rhinodesktop.foreorder_golf_consumer.utils.imagecaptureutils;


import android.app.Fragment;

import com.rhinodesktop.activityanimatorutility.activityutils.externalactivities.ExternalActivity;
import com.rhinodesktop.activityanimatorutility.activityutils.externalactivities.activitieswithresults.ExternalCameraActivity;
import com.rhinodesktop.activityanimatorutility.activityutils.externalactivities.activitieswithresults.ExternalGalleryActivity;

/**
 * Created by hunter on 2018-03-20.
 */

public class FragmentImageCaptureUtils extends ImageCaptureUtils {

    private Fragment fragment;

    public FragmentImageCaptureUtils(Fragment fragment) {
        super(fragment.getActivity());
        this.fragment = fragment;
    }

    @Override
    protected ExternalGalleryActivity createExternalGalleryActivity() {
        return new ExternalGalleryActivity(fragment);
    }

    @Override
    protected ExternalActivity createExternalPhotoActivity(String fileProviderAuth) {
        return new ExternalCameraActivity(fragment, fileProviderAuth, imageFile);
    }
}
