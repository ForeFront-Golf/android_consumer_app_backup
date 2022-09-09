package com.rhinoactive.generalutilities.views;

import android.content.Context;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Checkable;
import android.widget.LinearLayout;

import java.util.ArrayList;

/**
 * Created by hunter on 2018-03-21.
 */


/**
 * Abstract base class for button groups that contain compound buttons.
 */
public abstract class AdvancedCompoundButtonGroup extends LinearLayout {

    private ArrayList<View> mCheckables = new ArrayList<>();

    public AdvancedCompoundButtonGroup(Context context) {
        super(context);
    }

    public AdvancedCompoundButtonGroup(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AdvancedCompoundButtonGroup(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void addView(View child) {
        super.addView(child);
        mCheckables.add(child);
        setChildOnClickListener(child);
    }

    private void setChildOnClickListener(final View child) {
        child.setOnClickListener(new OnClickListener() {

            public void onClick(View clickedView) {
                for(int i = 0; i < mCheckables.size();i++) {
                    View otherClickableView = mCheckables.get(i);
                    Checkable checkable = parseViewForCheckable(otherClickableView);
                    if (checkable != null) {
                        setCheckableClick(clickedView, otherClickableView, checkable);
                    }
                }
            }
        });
    }

    protected abstract void setCheckableClick(View clickedView, View otherClickableView, Checkable checkable);

    @Nullable
    private Checkable parseViewForCheckable(final View child) {
        Checkable checkable = null;
        if(child instanceof Checkable) {
            checkable = (Checkable) child;
        }
        else if(child instanceof ViewGroup) {
            checkable = parseChildrenForCheckable((ViewGroup)child);
        }
        return checkable;
    }

    @Nullable
    private Checkable parseChildrenForCheckable(final ViewGroup child) {
        Checkable checkable = null;
        for (int i = 0; i < child.getChildCount();i++) {
            checkable = parseViewForCheckable(child.getChildAt(i));
            if (checkable != null) {
                break;
            }
        }
        return checkable;
    }
}
