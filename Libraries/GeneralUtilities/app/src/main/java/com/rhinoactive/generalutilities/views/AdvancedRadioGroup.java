package com.rhinoactive.generalutilities.views;

import android.content.Context;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Checkable;

/**
 * Created by Hunter Andrin on 2017-04-12.
 */

/**
 * With Android's default RadioGroup, RadioButtons need to be direct children in order to work properly.
 * This does not allow for much visual customization. The AdvancedRadioGroup remedy's this shortcoming.
 * With the AdvancedRadioGroup, RadioButtons can be nested anywhere in the AdvancedRadioGroup while
 * still allowing only one radio button to be checked
 */
public class AdvancedRadioGroup extends AdvancedCompoundButtonGroup {

    public AdvancedRadioGroup(Context context) {
        super(context);
    }

    public AdvancedRadioGroup(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AdvancedRadioGroup(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void setCheckableClick(View clickedView, View otherClickableView, Checkable checkable) {
        if (clickedView == otherClickableView) {
            checkable.setChecked(true);
        } else {
            checkable.setChecked(false);
        }
    }
}
