package com.rhinoactive.generalutilities;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.WindowManager;

/**
 * Created by rhinodesktop on 2017-03-17.
 */

public class SizeConverter {

    /**
     *
     * @param dp The dp value that needs to be converted into pixels
     * @param context a reference to a context
     * @return The value of the passing in dp in pixels
     */
    public static int dpToPx(float dp, Context context) {
        Resources r = context.getResources();
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                r.getDisplayMetrics()
        );
    }

    /**
     *
     * @param context A reference to a context
     * @return the height of the screen in pixels
     */
    private static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        return metrics.heightPixels;
    }

    /**
     *
     * @param context a reference to a context
     * @return the width of the screen in pixels
     */
    private static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        return metrics.widthPixels;
    }
}
