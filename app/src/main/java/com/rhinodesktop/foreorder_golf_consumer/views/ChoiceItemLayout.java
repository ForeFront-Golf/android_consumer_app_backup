package com.rhinodesktop.foreorder_golf_consumer.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rhinodesktop.foreorder_golf_consumer.R;
import com.rhinodesktop.foreorder_golf_consumer.interfaces.callbacks.OptionCheckedCallback;
import com.rhinodesktop.foreorder_golf_consumer.models.OptionItem;

import java.util.Locale;

import lombok.Getter;

/**
 * Created by Hunter Andrin on 2017-04-11.
 */

public abstract class ChoiceItemLayout extends RelativeLayout {

    @Getter
    private OptionItem optionItem;
    private TextView nameTextView;
    private TextView priceTextView;
    protected CompoundButton compoundOptionButton;

    public ChoiceItemLayout(Context context) {
        super(context);
    }

    public ChoiceItemLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ChoiceItemLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    protected void initViews() {
        priceTextView = findViewById(R.id.textview_item_price);
        nameTextView = findViewById(R.id.textview_option_item_name);
    }

    public void setViews(final OptionItem optionItem, final OptionCheckedCallback callback) {
        this.optionItem = optionItem;
        if (optionItem.getPrice() == 0) {
            priceTextView.setText(null);
        } else {
            priceTextView.setText(String.format(Locale.ENGLISH, "+$%.2f", optionItem.getPrice()));
        }
        nameTextView.setText(optionItem.getName());
        compoundOptionButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                callback.optionCheckedCallback(optionItem, checked);
            }
        });
    }
}
