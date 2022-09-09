package com.rhinoactive.imageutility;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;

/**
 * Created by hunter on 2018-02-21.
 */

/**
 * Class that loads images into {@link ImageView}s using Glide
 * @see <a href="https://github.com/bumptech/glide"></a>
 */
public class GlideImageViewLoader {

    private ImageView imageView;
    private RequestManager requestManager;
    private RequestOptions requestOptions = new RequestOptions();

    /**
     * Constructor for the GlideImageViewLoader.
     *
     * Will throw an {@link IllegalArgumentException} if the context corresponds to an Activity that
     * is has been destroyed.
     *
     * @param imageView The ImageView to load the image into.
     */
    public GlideImageViewLoader(ImageView imageView) throws IllegalArgumentException {
        this.imageView = imageView;
        requestManager = Glide.with(imageView.getContext());
    }

    /**
     * Applies custom options for loading images into ImageViews.
     * For example you can apply {@link RequestOptions#circleCrop()}} to display the image as a circle.
     * @see <a href="https://bumptech.github.io/glide/doc/options.html"></a> for more info.
     *
     * @param requestOptions The request options that are to be applied.
     */
    public void setRequestOptions(RequestOptions requestOptions) {
        this.requestOptions = requestOptions;
    }

    /**
     *
     * @param imageFile The image file to load into the ImageView.
     */
    public void loadImageIntoImageView(File imageFile) {
        loadImageIntoImageView(requestManager.load(imageFile));
    }

    /**
     *
     * @param imageUri The image Uri to load into the ImageView.
     */
    public void loadImageIntoImageView(Uri imageUri) {
        loadImageIntoImageView(requestManager.load(imageUri));
    }

    /**
     *
     * @param bitmap The image bitmap to load into the ImageView.
     */
    public void loadImageIntoImageView(Bitmap bitmap) {
        loadImageIntoImageView(requestManager.load(bitmap));
    }
    /**
     *
     * @param resourceId The image resource ID to load into the ImageView.
     */
    public void loadImageIntoImageView(Integer resourceId) {
        loadImageIntoImageView(requestManager.load(resourceId));
    }

    private void loadImageIntoImageView(RequestBuilder<Drawable> requestBuilder) {
        requestBuilder.transition(DrawableTransitionOptions.withCrossFade())
                .apply(requestOptions)
                .into(imageView);
    }
}
