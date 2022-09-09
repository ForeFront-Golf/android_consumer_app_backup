package com.rhinoactive.imageutility.imagefileloaders;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;

import com.rhinoactive.imageutility.interfaces.ImageFileLoaderInterface;

import java.io.File;

/**
 * Created by rhinodesktop on 2017-01-12.
 */

public class SynchronousImageFileLoader implements ImageFileLoaderInterface {
    @Override
    public void loadImageIntoImageView(final ImageView imageView, final File imageFile) {
        Handler mainHandler = new Handler(Looper.getMainLooper());
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                Bitmap myBitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
                imageView.setImageBitmap(myBitmap);
            }
        });
    }
}
