package com.rhinoactive.generalutilities;

import android.content.Context;
import android.graphics.Bitmap;
import androidx.annotation.Nullable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by rhinodesktop on 2017-02-27.
 */

public class ImageFileUtils {

    /**
     * Saves a bitmap image as a jpeg
     *
     * @param context A context.
     * @param quality The quality of the image. Tells the compressor how much it should compress the image.
     *                The value should be a value between 0-100. 0 meaning compress for small size,
     *                100 meaning compress for max quality. Some formats, like PNG which is lossless,
     *                will ignore the quality setting.
     * @param bmp The bitmap object to be converted into a jpeg file.
     * @param filename The name of the jpeg file. This is not an absolute path.
     *
     * @return The total path to the jpeg image.
     */
    @Nullable
    public static String saveBitmapAsJPEG(Context context, int quality, Bitmap bmp, String filename) {
        String totalPath = null;
        FileOutputStream out = null;
        try {
            File file = new File(context.getCacheDir(), filename);
            out = new FileOutputStream(file);

            bmp.compress(Bitmap.CompressFormat.JPEG, quality, out);

            totalPath = file.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return totalPath;
    }
}
