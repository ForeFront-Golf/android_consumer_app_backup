package com.rhinodesktop.foreorder_golf_consumer.fragments;

import android.os.Build;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.RequiresApi;

import com.rhinodesktop.foreorder_golf_consumer.R;
import com.rhinodesktop.foreorder_golf_consumer.events.SingleOptionCheckedEvent;
import com.rhinodesktop.foreorder_golf_consumer.models.OptionGroup;
import com.rhinodesktop.foreorder_golf_consumer.models.OptionItem;
import com.rhinodesktop.foreorder_golf_consumer.views.ChoiceItemLayout;
import com.rhinodesktop.foreorder_golf_consumer.views.SingleItemLayout;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Hunter Andrin on 2017-04-11.
 */

public class SingleChoiceFragment extends ChoiceFragment {
    private static final String SINGLE_OPTION_GROUP_KEY = "single_option_group_key";

    public static SingleChoiceFragment newInstance(OptionGroup optionGroup) {
        SingleChoiceFragment singleChoiceFragment = new SingleChoiceFragment();
        return (SingleChoiceFragment) singleChoiceFragment.createChoiceFragment(optionGroup);
    }

    @Override
    protected String getOptionTypeKey() {
        return SINGLE_OPTION_GROUP_KEY;
    }

    @Override
    protected int getLayoutFragment() {
        return R.layout.single_choice_fragment;
    }

    @Override
    protected LinearLayout getChoiceItemsLayout(View rootView) {
        return rootView.findViewById(R.id.aradiogroup_single_choice_items);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected ChoiceItemLayout createChoiceItemView() {
        return new SingleItemLayout(getContext());
    }

    @Override
    public void optionCheckedCallback(OptionItem optionItem, boolean checked) {
        if (checked) {
            EventBus.getDefault().post(new SingleOptionCheckedEvent(SingleChoiceFragment.this, optionItem));
        }
    }
}
