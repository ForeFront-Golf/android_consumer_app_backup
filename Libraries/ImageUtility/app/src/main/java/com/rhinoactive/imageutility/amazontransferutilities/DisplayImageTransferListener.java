package com.rhinoactive.imageutility.amazontransferutilities;

import android.widget.ImageView;

import com.rhinoactive.imageutility.interfaces.ImageFileLoaderInterface;

import java.io.File;

/**
 * Created by hunter on 2018-02-21.
 */

public class DisplayImageTransferListener extends StandardTransferListener {

    protected ImageView profileImg;
    protected ImageFileLoaderInterface imageFileLoader;


    public DisplayImageTransferListener(File s3File, ImageView profileImg, ImageFileLoaderInterface imageFileLoader) {
        super(s3File);
        this.profileImg = profileImg;
        this.imageFileLoader = imageFileLoader;
    }

    @Override
    protected void transferComplete() {
        super.transferComplete();
        imageFileLoader.loadImageIntoImageView(profileImg, s3File);
    }
}
