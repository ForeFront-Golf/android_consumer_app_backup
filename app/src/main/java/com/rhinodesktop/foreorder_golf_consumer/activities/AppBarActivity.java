package com.rhinodesktop.foreorder_golf_consumer.activities;

import android.view.View;
import android.widget.TextView;

import com.google.android.material.appbar.AppBarLayout;
import com.rhinodesktop.activityanimatorutility.models.AnimationType;
import com.rhinodesktop.foreorder_golf_consumer.R;
import com.rhinodesktop.foreorder_golf_consumer.listeners.TitleOnOffsetChangedListener;

/**
 * Created by Hunter Andrin on 2017-04-20.
 */

public abstract class AppBarActivity extends NoInternetAppBarActivity {

    protected void initToolbar() {
        super.initToolbarView();
        animType = AnimationType.SLIDE_DOWN;
        toolbar.setBackground(null);
        super.initLeftButton(R.drawable.icon_close_wht, backClickListener);
        initTitleView();
    }

    protected void initTitleView() {
        AppBarLayout appBarLayout = findViewById(R.id.app_bar_layout);
        TextView titleTextView = findViewById(R.id.textview_toolbar_title);
        titleTextView.setVisibility(View.VISIBLE);
        titleTextView.setText(getTitleForToolbar());
       AppBarLayout.OnOffsetChangedListener onOffsetChangedListener = new TitleOnOffsetChangedListener(titleTextView);
        appBarLayout.addOnOffsetChangedListener(onOffsetChangedListener);
    }

    protected abstract String getTitleForToolbar();
}
