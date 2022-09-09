package com.rhinodesktop.foreorder_golf_consumer.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rhinodesktop.foreorder_golf_consumer.R;
import com.rhinodesktop.foreorder_golf_consumer.models.OptionItem;

import java.util.Locale;

/**
 * Created by Hunter Andrin on 2017-04-13.
 */

public class OptionItemCheckoutLayout extends RelativeLayout {

    TextView optionItemTextView;

    public OptionItemCheckoutLayout(Context context) {
        super(context);
        initViews();
    }

    public OptionItemCheckoutLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews();
    }

    public OptionItemCheckoutLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews();
    }

    public void setOptionItemText(OptionItem optionItem) {
        String textToDisplay = optionItem.getName();
        if (optionItem.getPrice() > 0) {
            textToDisplay = textToDisplay + String.format(Locale.ENGLISH, " ($%.2f)", optionItem.getPrice());
        }
        optionItemTextView.setText(textToDisplay);
    }

    private void initViews() {
        inflate(getContext(), R.layout.option_item_checkout_view, this);
        optionItemTextView = (TextView) findViewById(R.id.textview_option_item_name_price);
    }
}
