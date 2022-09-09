package com.rhinoactive.generalutilities.dialogutils.decorators;

import com.afollestad.materialdialogs.MaterialDialog;

/**
 * Created by hunter on 2018-02-12.
 */

public class DialogBackgroundColorDecorator extends DialogColorDecorator {

    public DialogBackgroundColorDecorator(int color) {
        super(color);
    }

    @Override
    public MaterialDialog.Builder colorDialog(MaterialDialog.Builder builder) {
        return builder.backgroundColor(color);
    }
}
