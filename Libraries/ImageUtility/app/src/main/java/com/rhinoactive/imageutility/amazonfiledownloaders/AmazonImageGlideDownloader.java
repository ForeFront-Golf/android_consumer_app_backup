package com.rhinoactive.imageutility.amazonfiledownloaders;

import android.widget.ImageView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

/**
 * Created by hunter on 2018-02-27.
 */

public abstract class AmazonImageGlideDownloader extends AmazonImageDownloader {

    protected RequestOptions requestOptions = new RequestOptions();

    public AmazonImageGlideDownloader(ImageView imageView) {
        super(imageView);
    }

    public AmazonImageGlideDownloader(ImageView imageView, RequestOptions requestOptions) {
        super(imageView);
        this.requestOptions = requestOptions;
    }

    public void circleImage() {
        requestOptions = requestOptions.circleCrop();
    }

    public void skipGlideCache() {
        requestOptions = requestOptions.skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE);
    }

    public void roundedCorners(int cornerRadius) {
        requestOptions = RequestOptions.bitmapTransform(new RoundedCorners(cornerRadius));
    }
}
