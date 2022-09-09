package com.rhinodesktop.foreorder_golf_consumer.activities;

import android.content.Context;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.rhinodesktop.activityanimatorutility.activityutils.ActivityAnimationUtils;
import com.rhinodesktop.activityanimatorutility.models.AnimationType;
import com.rhinodesktop.foreorder_golf_consumer.R;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;

//import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by rhinodesktop on 2017-03-15.
 */

public abstract class ToolbarActivity extends AppCompatActivity {

    protected ImageButton leftButton;
    protected ImageButton rightButton;
    protected Toolbar toolbar;
    protected AnimationType animType = AnimationType.NONE;
    protected View.OnClickListener backClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onBackPressed();
        }
    };

    protected void initToolbarView() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        leftButton = findViewById(R.id.menu_button_left);
        rightButton = findViewById(R.id.menu_button_right);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ActivityAnimationUtils.animateActivityTransition(this, animType);
    }

    @Override
    protected void attachBaseContext(Context base) {
//        super.attachBaseContext(CalligraphyContextWrapper.wrap(base));
        super.attachBaseContext(ViewPumpContextWrapper.wrap(base));
    }

    protected void initLeftButton(int imageResourceId, View.OnClickListener onClickListener) {
        leftButton.setImageResource(imageResourceId);
        leftButton.setVisibility(View.VISIBLE);
        leftButton.setOnClickListener(onClickListener);
    }

    protected void initRightButton(int imageResourceId, View.OnClickListener onClickListener) {
        rightButton.setImageResource(imageResourceId);
        rightButton.setVisibility(View.VISIBLE);
        rightButton.setOnClickListener(onClickListener);
    }

    protected void showLogo() {
        ImageView logoImageView = findViewById(R.id.imageview_toolbar_logo);
        setVisibility(logoImageView, View.VISIBLE);
    }

    protected void hideLogo() {
        ImageView logoImageView = findViewById(R.id.imageview_toolbar_logo);
        setVisibility(logoImageView, View.GONE);
    }

    protected void showRefresh() {
        setVisibility(rightButton, View.VISIBLE);
    }

    protected void hideRefresh() {
        ImageButton rightButton = findViewById(R.id.menu_button_right);
        setVisibility(rightButton, View.GONE);
    }

    private void setVisibility(View view, int visibility) {
        view.setVisibility(visibility);
    }
}
