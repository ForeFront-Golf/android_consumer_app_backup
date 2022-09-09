package com.rhinodesktop.foreorder_golf_consumer.parsers.loginparsers;

import android.app.Activity;

import com.rhinoactive.foreorder_library_android.utils.Constants;
import com.rhinodesktop.foreorder_golf_consumer.utils.ForeOrderToastUtils;

/**
 * Created by rhinodesktop on 2017-03-31.
 */

public class CreateAccountParser extends ForeOrderLoginParser {

    public CreateAccountParser(Activity previousActivity) {
        super(previousActivity);
    }

    @Override
    protected void displayFailedMessage() {
        ForeOrderToastUtils.getInstance().displayToastFromMainThreadLong(Constants.ACCOUNT_CREATION_FAILED);
    }
}
