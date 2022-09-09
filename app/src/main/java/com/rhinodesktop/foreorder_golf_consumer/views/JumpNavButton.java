package com.rhinodesktop.foreorder_golf_consumer.views;

import android.app.Fragment;
import android.content.Context;

import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.rhinoactive.generalutilities.animations.AnimationUtils;
import com.rhinoactive.generalutilities.models.HideViewAnimation;
import com.rhinoactive.generalutilities.models.ShowViewAnimation;
import com.rhinodesktop.foreorder_golf_consumer.R;
import com.rhinodesktop.foreorder_golf_consumer.adapters.MenuPagerAdapter;
import com.rhinodesktop.foreorder_golf_consumer.models.ItemType;
import com.rhinodesktop.foreorder_golf_consumer.models.Menu;
import com.rhinodesktop.foreorder_golf_consumer.models.MenuItem;
import com.rhinodesktop.foreorder_golf_consumer.models.MenuItemsByItemType;

import java.util.List;

import lombok.Getter;
import lombok.NonNull;

/**
 * Created by rhinodesktop on 2017-03-23.
 */

public class JumpNavButton extends FrameLayout {

    @Getter
    private boolean jumpNavOpen = false;

    private FrameLayout dimBackgroundLayout;
    private LinearLayout jumpNavOptionsLayout;
    private ImageButton jumpSectionButton;

    private OnClickListener navButtonClickedListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            handleOpenCloseOfJumpNav();
        }
    };

    public JumpNavButton(@NonNull Context context) {
        super(context);
        initView();
    }

    public JumpNavButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public JumpNavButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public void handleOpenCloseOfJumpNav() {
        int animDuration = 200;
        if (jumpNavOpen) {
            AnimationUtils.getInstance().hideAnimation(jumpNavOptionsLayout, HideViewAnimation.SHRINK_TO_BOTTOM_RIGHT, animDuration);
            jumpSectionButton.setImageResource(R.drawable.icon_jump_menu_wht);
            AnimationUtils.getInstance().hideAnimation(dimBackgroundLayout, HideViewAnimation.FADE_OUT, animDuration);
        } else {
            AnimationUtils.getInstance().showAnimation(jumpNavOptionsLayout, ShowViewAnimation.GROW_FROM_BOTTOM_RIGHT, animDuration);
            jumpSectionButton.setImageResource(R.drawable.icon_close_wht);
            AnimationUtils.getInstance().showAnimation(dimBackgroundLayout, ShowViewAnimation.FADE_IN, animDuration);
        }
        jumpNavOpen = !jumpNavOpen;
    }

    public void setJumpSectionOptions(FragmentActivity activity, Menu menu) {
        removePreviousJumpSections();
        for (MenuItemsByItemType menuItemsByItemType : menu.getMenuItemsByItemType()) {
            initJumpToItemTypeNavButton(activity, menuItemsByItemType.getItemType(), menu);
        }
    }

    private void initView() {
        inflate(getContext(), R.layout.jump_nav_button, this);
        this.dimBackgroundLayout = (FrameLayout) findViewById(R.id.flayout_dim_background);
        this.jumpNavOptionsLayout = (LinearLayout) findViewById(R.id.llayout_jump_section_nav);
        this.jumpSectionButton = (ImageButton) findViewById(R.id.button_jump_section);
        setOnClickListeners();
    }

    private void removePreviousJumpSections() {
        int numOfViews = jumpNavOptionsLayout.getChildCount();
        if (numOfViews <= 1) { // We don't want to remove the "Jump to a section" layout
            return;
        }
        while (jumpNavOptionsLayout.getChildAt(1) != null) {
            View childView = jumpNavOptionsLayout.getChildAt(1);
            ((ViewGroup) childView.getParent()).removeView(childView);
        }
    }

    private void initJumpToItemTypeNavButton(FragmentActivity activity, ItemType itemType, Menu menu) {
        JumpToTypeLayout jumpToTypeLayout = new JumpToTypeLayout(getContext());
        jumpToTypeLayout.setItemTypeNameTextView(itemType.getName());
        jumpToTypeLayout.setOnClickListener(createJumpToSectionClickListener(activity, itemType, menu));
        jumpNavOptionsLayout.addView(jumpToTypeLayout);
    }

    private View.OnClickListener createJumpToSectionClickListener(final FragmentActivity activity, ItemType itemType, Menu menu) {
        final int positionToScrollTo = getPositionToScrollTo(itemType, menu);
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // since each pager has a recycler view with the same id we must do findById on the current page fragment
                ViewPager viewPager = (ViewPager) activity.findViewById(R.id.viewpager);
                Fragment fragment = activity.getFragmentManager().findFragmentByTag(MenuPagerAdapter.getTagName(viewPager));

                RecyclerView recyclerView = (RecyclerView) fragment.getView().findViewById(R.id.recyclerview_food_items);
                recyclerView.smoothScrollToPosition(positionToScrollTo);
                handleOpenCloseOfJumpNav();
            }
        };
    }

    private int getPositionToScrollTo(ItemType itemType, Menu menu) {
        String name = itemType.getName().toLowerCase();
        int positionToScrollTo = 0;
        for (MenuItemsByItemType menuItemsByItemType : menu.getMenuItemsByItemType()) {
            if (menuItemsByItemType.getItemType().getName().toLowerCase().equals(name)) {
                break;
            } else {
                List<MenuItem> menuItems = menuItemsByItemType.getMenuItems();
                int numOfItems = menuItems.size();
                if (numOfItems > 0) {
                    // Add the number of items in the list as well as 1 for the header
                    positionToScrollTo = positionToScrollTo + numOfItems + 1;
                }
            }
        }
        return positionToScrollTo;
    }

    private void setOnClickListeners() {
        this.jumpSectionButton.setOnClickListener(navButtonClickedListener);
        this.dimBackgroundLayout.setOnClickListener(navButtonClickedListener);
    }
}
