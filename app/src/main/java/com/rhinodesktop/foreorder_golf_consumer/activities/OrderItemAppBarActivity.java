package com.rhinodesktop.foreorder_golf_consumer.activities;

import android.widget.ImageView;

import com.rhinoactive.imageutility.GlideImageViewLoader;
import com.rhinoactive.imageutility.amazonfiledownloaders.AmazonImageGlideDownloader;
import com.rhinoactive.imageutility.amazonfiledownloaders.AmazonImageViewDownloader;
import com.rhinodesktop.foreorder_golf_consumer.R;
import com.rhinodesktop.foreorder_golf_consumer.models.MenuItem;

/**
 * Created by Hunter Andrin on 2017-04-20.
 */

public abstract class OrderItemAppBarActivity extends AppBarActivity {

    MenuItem menuItem;

    protected void initToolbar(MenuItem menuItem) {
        this.menuItem = menuItem;
        super.initToolbar();
        ImageView backdropImageView = findViewById(R.id.imageview_menu_item_backdrop);
        if (menuItem.getPhotoUrl() == null) {
            GlideImageViewLoader loader = new GlideImageViewLoader(backdropImageView);
            loader.loadImageIntoImageView(R.drawable.item_header_img_placeholder);
        } else {
            AmazonImageGlideDownloader amazonImageGlideDownloader = new AmazonImageViewDownloader(backdropImageView);
            amazonImageGlideDownloader.downloadFile(menuItem.getPhotoUrl());
        }
    }


    @Override
    protected String getTitleForToolbar() {
        return menuItem.getName();
    }
}
