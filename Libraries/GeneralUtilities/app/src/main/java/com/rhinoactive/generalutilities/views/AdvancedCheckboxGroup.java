package com.rhinoactive.generalutilities.views;

import android.content.Context;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Checkable;

/**
 * Created by hunter on 2018-03-21.
 */


/**
 * By default in Android, Checkboxes can only be checked by clicking on the checkbox directly.
 * This means that checkboxes cannot be selected by clicking on the associated text.
 *
 * This does not allow for much visual customization. The AdvancedCheckboxGroup remedy's this shortcoming.
 * With the AdvancedCheckboxGroup, Checkboxes can be nested anywhere in the AdvancedCheckboxGroup
 * and
 */
public class AdvancedCheckboxGroup extends AdvancedCompoundButtonGroup {

    public AdvancedCheckboxGroup(Context context) {
        super(context);
    }

    public AdvancedCheckboxGroup(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AdvancedCheckboxGroup(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void setCheckableClick(View clickedView, View otherClickableView, Checkable checkable) {
        if (clickedView == otherClickableView) {
            checkable.setChecked(!checkable.isChecked());
        }
    }
}
