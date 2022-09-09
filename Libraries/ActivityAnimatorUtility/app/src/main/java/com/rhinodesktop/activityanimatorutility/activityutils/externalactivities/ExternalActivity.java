package com.rhinodesktop.activityanimatorutility.activityutils.externalactivities;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Toast;

import com.rhinodesktop.activityanimatorutility.models.AnimationType;

/**
 * Created by hunter on 2018-02-13.
 */

/**
 * Abstract base class for a number of external activities.
 */
public abstract class ExternalActivity {

    protected abstract Intent createIntent();

    protected abstract String getErrorMsg();

    protected abstract void startActivity(Intent intent, AnimationType fadeIn);

    protected AppCompatActivity activity;

    /**
     * Base constructor for the {@link ExternalActivity}.
     *
     * @param activity A reference to the calling activity.
     */
    public ExternalActivity(AppCompatActivity activity) {
        this.activity = activity;
    }

    /**
     * Starts the external activity.
     */
    public void start() {
        Intent intent = createIntent();
        if (intent.resolveActivity(activity.getPackageManager()) != null) {
            startActivity(intent, AnimationType.FADE_IN);
        } else {
            displayToastFromMainThread(activity, getErrorMsg());
        }
    }

    private void displayToastFromMainThread(final Context context, final String toastText) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run () {
                Toast.makeText(context, toastText, Toast.LENGTH_LONG).show();
            }
        });
    }
}
