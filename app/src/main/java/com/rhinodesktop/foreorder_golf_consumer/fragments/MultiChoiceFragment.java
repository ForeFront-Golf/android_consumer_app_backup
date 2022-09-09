package com.rhinodesktop.foreorder_golf_consumer.fragments;

import android.os.Build;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.RequiresApi;

import com.rhinodesktop.foreorder_golf_consumer.R;
import com.rhinodesktop.foreorder_golf_consumer.events.MultiOptionCheckedEvent;
import com.rhinodesktop.foreorder_golf_consumer.models.OptionGroup;
import com.rhinodesktop.foreorder_golf_consumer.models.OptionItem;
import com.rhinodesktop.foreorder_golf_consumer.views.ChoiceItemLayout;
import com.rhinodesktop.foreorder_golf_consumer.views.MultiItemLayout;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Hunter Andrin on 2017-04-11.
 */

public class MultiChoiceFragment extends ChoiceFragment {
    private static final String MULTI_OPTION_GROUP_KEY = "multi_option_group_key";

    public static MultiChoiceFragment newInstance(OptionGroup optionGroup) {
        MultiChoiceFragment multiChoiceFragment = new MultiChoiceFragment();
        return (MultiChoiceFragment) multiChoiceFragment.createChoiceFragment(optionGroup);
    }

    @Override
    protected String getOptionTypeKey() {
        return MULTI_OPTION_GROUP_KEY;
    }

    @Override
    protected int getLayoutFragment() {
        return R.layout.multi_choice_fragment;
    }

    @Override
    protected LinearLayout getChoiceItemsLayout(View rootView) {
        return rootView.findViewById(R.id.acheckboxgroup_multi_choice_items);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected ChoiceItemLayout createChoiceItemView() {
        return new MultiItemLayout(getContext());
    }

    @Override
    public void optionCheckedCallback(OptionItem optionItem, boolean checked) {
        EventBus.getDefault().post(new MultiOptionCheckedEvent(MultiChoiceFragment.this, optionItem, checked));
    }
}
