package com.rhinoactive.imageutility.imageviewloaders;

import androidx.recyclerview.widget.RecyclerView;
import android.widget.ImageView;

import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.bumptech.glide.request.RequestOptions;
import com.rhinoactive.imageutility.amazontransferutilities.RecyclerViewImageTransferListener;
import com.rhinoactive.imageutility.interfaces.ImageFileLoaderInterface;

import java.io.File;

/**
 * Created by Hunter Andrin on 2017-10-02.
 */

public class RecyclerViewImageViewLoader extends GlideImageViewLoader {

    private RecyclerView.ViewHolder holder;

    public RecyclerViewImageViewLoader(File s3File, ImageView imageView, String photoS3Path, boolean checkCacheForImage,
                                       RecyclerView.ViewHolder holder, RequestOptions requestOptions) {
        super(s3File, imageView, photoS3Path, checkCacheForImage, requestOptions);
        this.holder = holder;
    }

    @Override
    protected TransferListener createTransferListener(ImageFileLoaderInterface imageFileLoader) {
        return new RecyclerViewImageTransferListener(s3File, holder, imageFileLoader, imageView);
    }
}
