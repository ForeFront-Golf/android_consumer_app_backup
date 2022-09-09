package com.rhinoactive.generalutilities.dialogutils;

import android.content.Context;
import androidx.annotation.Nullable;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.rhinoactive.generalutilities.R;
import com.rhinoactive.generalutilities.dialogutils.decorators.DialogBackgroundColorDecorator;
import com.rhinoactive.generalutilities.dialogutils.decorators.DialogColorDecorator;
import com.rhinoactive.generalutilities.dialogutils.decorators.DialogContentColorDecorator;
import com.rhinoactive.generalutilities.dialogutils.decorators.DialogItemsColorDecorator;
import com.rhinoactive.generalutilities.dialogutils.decorators.DialogNegativeColorDecorator;
import com.rhinoactive.generalutilities.dialogutils.decorators.DialogPositiveColorDecorator;
import com.rhinoactive.generalutilities.dialogutils.decorators.DialogTitleColorDecorator;
import com.rhinoactive.generalutilities.dialogutils.decorators.DialogWidgetColorDecorator;
import com.rhinoactive.generalutilities.interfaces.ListSelectionCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rhinodesktop on 2017-01-27.
 */

/**
 * Utility class that simplifies and encapsulates the creation of dialogs. Uses the third party library {@link MaterialDialog}
 * For more information on the MaterialDialogs and how they work please @see
 * <a href="https://github.com/afollestad/material-dialogs">https://github.com/afollestad/material-dialogs</a>
 */
public class DialogUtils {

    private List<DialogColorDecorator> dialogColorDecorators = new ArrayList<>();

    /**
     * Private constructor, use the {@link DialogUtilsBuilder} class to instantiate the DialogUtils.
     *
     * @param dialogUtilsBuilder Builder for the DialogUtils
     */
    private DialogUtils(DialogUtilsBuilder dialogUtilsBuilder) {
        this.dialogColorDecorators = dialogUtilsBuilder.dialogColorDecorators;
    }

    /**
     * Creates a dialog that only contains a title and an okay button.
     *
     * @param context A reference to a context.
     * @param title The title to display in the dialog.
     */
    public void showTitleDialog(Context context, String title) {
        buildDialog(context, title)
                .positiveText(R.string.okay)
                .show();
    }

    /**
     * Creates a dialog that contains a title, an okay button, and content text.
     *
     * @param context A reference to a context.
     * @param title The title to display in the dialog.
     * @param content Content text to be displayed in the center of the dialog.
     */
    public void showTextContentDialog(Context context, String title, String content) {
        buildDialog(context, title)
                .content(content)
                .positiveText(R.string.okay)
                .show();
    }

    /**
     * Creates a dialog that contains a title and a 'Yes' or 'No' button.
     *
     * @param context A reference to a context.
     * @param title The title to display in the dialog.
     * @param onYesClickListener A callback that gets called when the 'Yes' button is clicked.
     */
    public void showYesNoDialog(Context context, final String title, MaterialDialog.SingleButtonCallback onYesClickListener) {
        buildDialog(context, title)
                .negativeText(R.string.no)
                .positiveText(R.string.yes)
                .onPositive(onYesClickListener)
                .show();
    }

    /**
     * Creates a dialog that displays a list of selectable options to the user.
     *
     * @param context A reference to a context.
     * @param title The title to display in the dialog.
     * @param values The list of string value options.
     * @param callback A callback that gets called when an option in the list gets selected.
     */
    public void showListDialog(Context context, String title, List<String> values, ListSelectionCallback callback) {
        buildDialog(context, title)
                .negativeText(R.string.cancel)
                .items(values).itemsCallback(createListItemCallback(callback))
                .show();
    }

    /**
     *
     * @param context A reference to a context.
     * @param title The title to display in the dialog.
     * @param arrayRes The resource ID of an array. Should be in the following format: R.array.array_resource
     * @param callback A callback that gets called when an option in the list gets selected.
     */
    public void showListDialog(Context context, String title, int arrayRes, ListSelectionCallback callback) {
        MaterialDialog.Builder builder = buildDialog(context, title);
        builder.items(arrayRes).itemsCallback(createListItemCallback(callback));
        builder = builder.negativeText(R.string.cancel);
        builder.show();
    }

    /**
     * Creates a dialog that display a circular spinner loading indicator.
     *
     * @param context A reference to a context.
     * @param title The title to display in the dialog.
     * @param content Optional content text to display in the dialog.
     */
    public MaterialDialog showProgressDialog(Context context, String title, @Nullable String content) {
        MaterialDialog.Builder builder = buildDialog(context, title);
        builder = builder.progress(true, 100);
        if (content != null) {
            builder.content(content);
        }
        return builder.show();
    }

    /**
     *
     * @param context A reference to a context.
     * @param title The title to display in the dialog.
     * @return A {@link MaterialDialog.Builder} instance that is configured to have all the set colors
     *         which can be further customized and/or built into an Dialog.
     */
    public MaterialDialog.Builder buildDialog(Context context, String title) {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(context).title(title);
        for (DialogColorDecorator decorator : dialogColorDecorators) {
            builder = decorator.colorDialog(builder);
        }
        return builder;
    }

    private MaterialDialog.ListCallback createListItemCallback(final ListSelectionCallback callback) {
        return new MaterialDialog.ListCallback() {
            @Override
            public void onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                if (callback != null) {
                    callback.selected(which, text.toString());
                }
            }
        };
    }

    /**
     * This class is used to construct an instance of the DialogUtils
     *
     * Note all color parameters should be in the form 0xAARRGGBB, and not the resource ID of the color.
     */
    public static class DialogUtilsBuilder {

        private List<DialogColorDecorator> dialogColorDecorators = new ArrayList<>();

        /**
         *
         * @param titleColor The title color of the dialog.
         */
        public DialogUtilsBuilder titleColor(int titleColor) {
            dialogColorDecorators.add(new DialogTitleColorDecorator(titleColor));
            return this;
        }

        /**
         *
         * @param positiveTextColor The text color of the positive dialog button.
         */
        public DialogUtilsBuilder positiveTextColor(int positiveTextColor) {
            dialogColorDecorators.add(new DialogPositiveColorDecorator(positiveTextColor));
            return this;
        }

        /**
         *
         * @param negativeTextColor The text color of the negative dialog button.
         */
        public DialogUtilsBuilder negativeTextColor(int negativeTextColor) {
            dialogColorDecorators.add(new DialogNegativeColorDecorator(negativeTextColor));
            return this;
        }

        /**
         *
         * @param backgroundColor The background color of the dialog
         */
        public DialogUtilsBuilder backgroundColor(int backgroundColor) {
            dialogColorDecorators.add(new DialogBackgroundColorDecorator(backgroundColor));
            return this;
        }

        /**
         *
         * @param contentColor The color of the dialog's content text.
         */
        public DialogUtilsBuilder contentColor(int contentColor) {
            dialogColorDecorators.add(new DialogContentColorDecorator(contentColor));
            return this;
        }

        /**
         *
         * @param itemsColor The color of the dialog's selectable list items.
         */
        public DialogUtilsBuilder itemsColor(int itemsColor) {
            dialogColorDecorators.add(new DialogItemsColorDecorator(itemsColor));
            return this;
        }

        /**
         *
         * @param widgetColor The color of the dialog's widgets, such as the progress bar widget.
         */
        public DialogUtilsBuilder widgetColor(int widgetColor) {
            dialogColorDecorators.add(new DialogWidgetColorDecorator(widgetColor));
            return this;
        }

        /**
         *
         * @return A DialogUtils instance that is configured to have all the set colors.
         */
        public DialogUtils build() {
            return new DialogUtils(this);
        }
    }
}
