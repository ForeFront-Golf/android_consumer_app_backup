package com.rhinoactive.imageutility.amazontransferutilities;

import androidx.recyclerview.widget.RecyclerView;
import android.widget.ImageView;

import com.rhinoactive.imageutility.interfaces.ImageFileLoaderInterface;

import java.io.File;

/**
 * Created by hunter on 2018-02-21.
 */

public class RecyclerViewImageTransferListener extends StandardTransferListener {

    private RecyclerView.ViewHolder holder;
    private int position;
    private ImageFileLoaderInterface imageFileLoader;
    private ImageView imageView;


    public RecyclerViewImageTransferListener(File s3File, RecyclerView.ViewHolder holder,
                                             ImageFileLoaderInterface imageFileLoader, ImageView imageView) {
        super(s3File);
        this.holder = holder;
        this.position = holder.getAdapterPosition();
        this.imageFileLoader = imageFileLoader;
        this.imageView = imageView;
    }

    @Override
    protected void transferComplete() {
        if (holder.getAdapterPosition() == position) {
            imageFileLoader.loadImageIntoImageView(imageView, s3File);
        }
    }
}
