package com.rhinodesktop.foreorder_golf_consumer.views;

import android.content.Context;
import android.graphics.Typeface;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.util.AttributeSet;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;

/**
 * Created by sungwook on 2018-04-16.
 */

public class CustomTabLayout extends TabLayout {
    private Typeface mTypeface;

    public CustomTabLayout(Context context) {
        super(context);
        initView();
    }

    public CustomTabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public CustomTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        mTypeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/Montserrat_Medium.ttf");
    }

    @Override
    public void setupWithViewPager(@Nullable ViewPager viewPager) {
        super.setupWithViewPager(viewPager);

        if (mTypeface != null) {
            this.removeAllTabs();
            ViewGroup slidingTabStrip = (ViewGroup) getChildAt(0);

            PagerAdapter adapter = viewPager.getAdapter();
            if (adapter != null) {
                for (int i = 0; i < adapter.getCount(); i++) {
                    TabLayout.Tab tab = this.newTab();
                    this.addTab(tab.setText(adapter.getPageTitle(i)));
                    AppCompatTextView view = (AppCompatTextView) ((ViewGroup) slidingTabStrip.getChildAt(i)).getChildAt(1);
                    view.setTypeface(mTypeface, Typeface.NORMAL);
                }
            }
        }
    }
}
