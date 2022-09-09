package com.rhinoactive.generalutilities;

import android.content.Context;
import android.graphics.drawable.Drawable;
import androidx.core.content.ContextCompat;

import java.util.Arrays;
import java.util.List;

/**
 * Created by rhinodesktop on 2017-02-21.
 */

/**
 * Provides utility methods to simplify the retrieval of various resources.
 *
 * To use, extend this class and return an instance of the app content
 * in the {@link #getAppContext()} method.
 */
public abstract class ResourceUtils {

    /**
     *
     * @param resId The resource ID of a string. Should be in the following format: R.string.string_resource
     * @return The String value of the associated resId.
     */
    public String strRes(int resId) {
        return getAppContext().getResources().getString(resId);
    }

    /**
     *
     * @param arrayId The resource ID of an array. Should be in the following format: R.array.array_resource
     * @return The list of Strings for the associated array.
     */
    public List<String> arrayRes(int arrayId) {
        return Arrays.asList(getAppContext().getResources().getStringArray(arrayId));
    }

    /**
     *
     * @param colorId The resource ID of a color. Should be in the following format: R.color.color_resource
     * @return  A color value in the form 0xAARRGGBB for the associated color resource.
     */
    public int colorRes(int colorId) {
        return ContextCompat.getColor(getAppContext(), colorId);
    }

    /**
     *
     * @param drawableId The resource ID of a drawable. Should be in the following format: R.drawable.drawable_resource
     * @return The Drawable for the associated drawable resource.
     */
    public Drawable drawableRes(int drawableId) {
        return ContextCompat.getDrawable(getAppContext(), drawableId);
    }

    /**
     *
     * @return a reference to the app context.
     */
    protected abstract Context getAppContext();
}
