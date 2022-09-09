package com.rhinodesktop.foreorder_golf_consumer.activities;

import android.view.View;

import com.rhinodesktop.activityanimatorutility.models.AnimationType;
import com.rhinodesktop.foreorder_golf_consumer.R;

/**
 * Created by rhinodesktop on 2017-03-15.
 */

public abstract class StandardToolbarActivity extends ToolbarActivity {

    protected enum ToolbarType {
        EmptyToolbar, BackButtonToolbar, ExitButtonToolbarFade, ExitButtonToolbarVertical
    }

    protected void initToolbar(ToolbarType toolbarType) {
        initToolbarView();
        switch (toolbarType) {
            case EmptyToolbar:
                initEmptyToolbar();
                break;
            case BackButtonToolbar:
                initBackButton();
                break;
            case ExitButtonToolbarFade:
                initExitButtonFade();
                break;
            case ExitButtonToolbarVertical:
                initExitButtonVertical();
                break;
            default:
                break;
        }
    }

    @Override
    protected void initToolbarView() {
        super.initToolbarView();
    }

    private void initEmptyToolbar() {
        animType = AnimationType.NONE;
        leftButton.setVisibility(View.GONE);
    }

    private void initBackButton() {
        animType = AnimationType.SLIDE_RIGHT;
        initLeftButton(R.drawable.icon_arrow_left_wht, backClickListener);
    }

    private void initExitButtonFade() {
        animType = AnimationType.FADE_IN;
        initLeftButton(R.drawable.icon_close_wht, backClickListener);
    }

    private void initExitButtonVertical() {
        animType = AnimationType.SLIDE_DOWN;
        initLeftButton(R.drawable.icon_close_wht, backClickListener);
    }
}
