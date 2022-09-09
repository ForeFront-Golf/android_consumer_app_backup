package com.rhinoactive.imageutility.amazonfiledownloaders;

import androidx.recyclerview.widget.RecyclerView;
import android.widget.ImageView;

import com.bumptech.glide.request.RequestOptions;
import com.rhinoactive.imageutility.imageviewloaders.ImageViewLoader;
import com.rhinoactive.imageutility.imageviewloaders.RecyclerViewImageViewLoader;

import java.io.File;

/**
 * Created by hunter on 2018-02-23.
 */

public class AmazonRecyclerViewImageDownloader extends AmazonImageGlideDownloader {

    private RecyclerView.ViewHolder holder;

    public AmazonRecyclerViewImageDownloader(ImageView imageView, RecyclerView.ViewHolder holder) {
        super(imageView);
        this.holder = holder;
    }

    public AmazonRecyclerViewImageDownloader(ImageView imageView, RequestOptions requestOptions, RecyclerView.ViewHolder holder) {
        super(imageView, requestOptions);
        this.holder = holder;
    }

    @Override
    protected ImageViewLoader createRegularImageViewLoader(File s3File, String photoS3Path) {
        return new RecyclerViewImageViewLoader(s3File, imageView, photoS3Path, checkCacheForFile, holder, requestOptions);
    }
}
