package com.rhinoactive.generalutilities.views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import timber.log.Timber;

/**
 * Created by Hunter Andrin on 2017-05-19.
 */

/**
 * @deprecated Please use the <a href="https://github.com/chrisjenx/Calligraphy">https://github.com/chrisjenx/Calligraphy</a>
 * library when using custom fonts.
 */
@Deprecated
public abstract class CustomTextView extends androidx.appcompat.widget.AppCompatTextView {

    public CustomTextView(Context context) {
        super(context);
        setCustomFont(context);
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setCustomFont(context);
    }

    public CustomTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setCustomFont(context);
    }

    public boolean setCustomFont(Context context) {
        Typeface typeface;
        try {
            typeface = Typeface.createFromAsset(context.getAssets(), getTypefacePath());
        } catch (Exception e) {
            Timber.e("Unable to load typeface: %s", e.getMessage());
            return false;
        }

        setTypeface(typeface);
        return true;
    }

    /**
     *
     * @return returns the relative path the desired font file. Fonts should exist under the following
     * file path: src/main/assets/fonts. An example return value would be: "fonts/Montserrat_Bold.otf"
     */
    protected abstract String getTypefacePath();
}
