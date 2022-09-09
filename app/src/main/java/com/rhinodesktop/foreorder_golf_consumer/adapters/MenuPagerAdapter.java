package com.rhinodesktop.foreorder_golf_consumer.adapters;


import android.app.Fragment;
import android.app.FragmentManager;

import androidx.viewpager.widget.ViewPager;

import com.rhinodesktop.foreorder_golf_consumer.fragments.MenuListFragment;
import com.rhinodesktop.foreorder_golf_consumer.models.Menu;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rhinodesktop on 2017-03-20.
 */

public class MenuPagerAdapter extends com.rhinodesktop.foreorder_golf_consumer.adapters.FragmentStatePagerAdapter {
    private static final String MENU_LIST_TAG = "menu_list_fragment_view";

    private final List<Fragment> mFragments = new ArrayList<>();
    private final List<String> mFragmentTitles = new ArrayList<>();

    public MenuPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public void addFragment(Menu menu) {
        mFragments.add(MenuListFragment.newInstance(menu));
        mFragmentTitles.add(menu.getName());
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public String getTag(int position) {
        return MENU_LIST_TAG + position;
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitles.get(position);
    }

    public static String getTagName(ViewPager viewPager) {
        return MenuPagerAdapter.MENU_LIST_TAG + viewPager.getCurrentItem();
    }
}