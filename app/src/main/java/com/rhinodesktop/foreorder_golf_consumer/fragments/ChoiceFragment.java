package com.rhinodesktop.foreorder_golf_consumer.fragments;

import android.app.Fragment;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.rhinodesktop.foreorder_golf_consumer.R;
import com.rhinodesktop.foreorder_golf_consumer.interfaces.callbacks.OptionCheckedCallback;
import com.rhinodesktop.foreorder_golf_consumer.models.OptionGroup;
import com.rhinodesktop.foreorder_golf_consumer.models.OptionItem;
import com.rhinodesktop.foreorder_golf_consumer.views.ChoiceItemLayout;

import java.util.List;

/**
 * Created by Hunter Andrin on 2017-04-11.
 */

public abstract class ChoiceFragment extends Fragment implements OptionCheckedCallback {

    protected OptionGroup optionGroup;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        String optionGroupJson = getArguments().getString(getOptionTypeKey());
        Gson gson = new Gson();
        optionGroup = gson.fromJson(optionGroupJson, OptionGroup.class);
        View rootView = inflater.inflate(getLayoutFragment(), container, false);
        LinearLayout choiceItemsLayout = getChoiceItemsLayout(rootView);
        setRequiredTextView(rootView);
        addChoiceItems(choiceItemsLayout);
        return rootView;
    }

    protected ChoiceFragment createChoiceFragment(OptionGroup optionGroup) {
        Bundle bundle = new Bundle();
        Gson gson = new Gson();
        String optionGroupJson = gson.toJson(optionGroup);
        bundle.putString(getOptionTypeKey(), optionGroupJson);
        this.setArguments(bundle);
        return this;
    }

    private void addChoiceItems(LinearLayout choiceItemsLayout) {
        List<OptionItem> optionItems = optionGroup.getOptionItems();
        for (OptionItem optionItem : optionItems) {
            initChoiceItemView(choiceItemsLayout, optionItem);
        }
    }

    private void initChoiceItemView(LinearLayout choiceItemsLayout, OptionItem optionItem) {
        ChoiceItemLayout choiceItemLayout = createChoiceItemView();
        choiceItemLayout.setViews(optionItem, this);
        choiceItemsLayout.addView(choiceItemLayout);
    }

    private void setRequiredTextView(View rootView) {
        TextView requiredTextView = rootView.findViewById(R.id.textview_required_item);
        if (optionGroup.getRequired()) {
            requiredTextView.setVisibility(View.VISIBLE);
        } else {
            requiredTextView.setVisibility(View.GONE);
        }
    }

    protected abstract String getOptionTypeKey();

    protected abstract int getLayoutFragment();

    protected abstract LinearLayout getChoiceItemsLayout(View rootView);

    protected abstract ChoiceItemLayout createChoiceItemView();
}
