package com.rhinoactive.generalutilities.dialogutils.decorators;

import com.afollestad.materialdialogs.MaterialDialog;

/**
 * Created by hunter on 2018-02-12.
 */

public abstract class DialogColorDecorator {

    protected int color;

    public DialogColorDecorator(int color) {
         this.color = color;
    }

    public abstract MaterialDialog.Builder colorDialog(MaterialDialog.Builder builder);
}
