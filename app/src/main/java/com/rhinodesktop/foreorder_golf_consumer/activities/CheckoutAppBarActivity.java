package com.rhinodesktop.foreorder_golf_consumer.activities;

import com.rhinodesktop.foreorder_golf_consumer.R;
import com.rhinodesktop.foreorder_golf_consumer.utils.ForeOrderResourceUtils;

/**
 * Created by Hunter Andrin on 2017-04-20.
 */

public abstract class CheckoutAppBarActivity extends AppBarActivity {

    @Override
    protected void initToolbar() {
        super.initToolbar();
    }

    @Override
    protected String getTitleForToolbar() {
        ForeOrderResourceUtils resourceUtils = ForeOrderResourceUtils.getInstance();
        return resourceUtils.strRes(R.string.your_order_title);
    }
}
