package com.rhinodesktop.foreorder_golf_consumer.listeners;


import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;

import com.rhinodesktop.foreorder_golf_consumer.models.Menu;
import com.rhinodesktop.foreorder_golf_consumer.views.JumpNavButton;

import java.util.List;

/**
 * Created by rhinodesktop on 2017-03-23.
 */

public class OnMenuPageChangeListener implements ViewPager.OnPageChangeListener {

    private FragmentActivity activity;
    private JumpNavButton jumpNavButton;
    private List<Menu> menus;


    public OnMenuPageChangeListener(FragmentActivity activity, JumpNavButton jumpNavButton, List<Menu> menus) {
        this.activity = activity;
        this.jumpNavButton = jumpNavButton;
        this.menus = menus;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

    @Override
    public void onPageSelected(int position) {
        Menu menu = menus.get(position);
        jumpNavButton.setJumpSectionOptions(activity, menu);
    }

    @Override
    public void onPageScrollStateChanged(int state) {}
}
