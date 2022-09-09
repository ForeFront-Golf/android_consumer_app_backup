package com.rhinodesktop.foreorder_golf_consumer.utils;

import com.rhinoactive.generalutilities.dialogutils.DialogUtils;
import com.rhinodesktop.foreorder_golf_consumer.R;

/**
 * Created by rhinodesktop on 2017-03-16.
 */

public class ForeOrderDialogUtils {

    private static DialogUtils dialogUtils;

    public static DialogUtils getInstance() {
        if (dialogUtils == null) {
            ForeOrderResourceUtils resourceUtils = ForeOrderResourceUtils.getInstance();
            DialogUtils.DialogUtilsBuilder builder = new DialogUtils.DialogUtilsBuilder();
            builder.titleColor(resourceUtils.colorRes(R.color.fore_order_blue));
            builder.positiveTextColor(resourceUtils.colorRes(R.color.fore_order_red));
            builder.negativeTextColor(resourceUtils.colorRes(R.color.fore_order_blue));
            builder.itemsColor(resourceUtils.colorRes(R.color.fore_order_blue));
            builder.backgroundColor(resourceUtils.colorRes(R.color.white));
            builder.contentColor(resourceUtils.colorRes(R.color.fore_order_blue));
            builder.widgetColor(resourceUtils.colorRes(R.color.fore_order_red));
            dialogUtils = builder.build();
        }
        return dialogUtils;
    }
}
