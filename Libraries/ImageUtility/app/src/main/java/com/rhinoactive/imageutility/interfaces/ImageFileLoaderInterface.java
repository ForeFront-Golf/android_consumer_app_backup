package com.rhinoactive.imageutility.interfaces;

import android.widget.ImageView;

import java.io.File;

/**
 * Created by rhinodesktop on 2017-01-12.
 */

public interface ImageFileLoaderInterface {
    void loadImageIntoImageView(ImageView imageView, File imageFile);
}
