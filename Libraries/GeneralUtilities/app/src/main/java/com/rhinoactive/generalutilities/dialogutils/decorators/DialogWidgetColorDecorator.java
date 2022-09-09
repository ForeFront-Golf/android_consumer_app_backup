package com.rhinoactive.generalutilities.dialogutils.decorators;

import com.afollestad.materialdialogs.MaterialDialog;
import com.rhinoactive.generalutilities.dialogutils.decorators.DialogColorDecorator;

/**
 * Created by hunter on 2018-02-12.
 */

public class DialogWidgetColorDecorator extends DialogColorDecorator {

    public DialogWidgetColorDecorator(int color) {
        super(color);
    }

    @Override
    public MaterialDialog.Builder colorDialog(MaterialDialog.Builder builder) {
        return builder.widgetColor(color);
    }
}
