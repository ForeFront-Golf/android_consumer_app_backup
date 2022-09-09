package com.rhinoactive.generalutilities;

import android.os.Build;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import androidx.core.content.ContextCompat;

/**
 * Created by rhinodesktop on 2017-01-31.
 */

public class StatusBarColorSetter {

    private int colorResourceId;

    /**
     *
     * @param colorResourceId The color of the status bar.
     */
    public StatusBarColorSetter(int colorResourceId) {
        this.colorResourceId = colorResourceId;
    }

    /**
     *
     * Changes the color of the status bar.
     *
     * @param activity A reference to the activity.
     */
    public void setStatusBarColor(AppCompatActivity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(activity, colorResourceId));
        }
    }
}
