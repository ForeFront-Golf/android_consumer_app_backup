package com.rhinodesktop.activityanimatorutility.models;

import android.app.FragmentManager;

import androidx.annotation.Nullable;


import com.rhinodesktop.activityanimatorutility.fragmentutils.FragmentAndAnimateManager;

/**
 * Created by hunter on 2018-02-13.
 */

/**
 * This class is used by the {@link FragmentAndAnimateManager} to animate fragment transitions.
 */
public class FragmentTransactionInfo {

    @Nullable
    private String tag;
    @Nullable
    private String transName;
    private boolean addToBackStack;

    /**
     *
     * @param tag The optional tag name for the fragment, can be used later to retrieve the
     *            fragment with {@link FragmentManager#findFragmentByTag(String)}.
     * @param transName The optional name of this transaction, can be used as the string parameter for
     *      {@link FragmentManager#popBackStack(String, int)} to pop that transaction from the back stack.
     * @param addToBackStack True if this fragment transaction is to be added to the back stack.
     */
    public FragmentTransactionInfo(@Nullable String tag, @Nullable String transName, boolean addToBackStack) {
        this.tag = tag;
        this.transName = transName;
        this.addToBackStack = addToBackStack;
    }

    @Nullable
    public String getTag() {
        return tag;
    }

    @Nullable
    public String getTransName() {
        return transName;
    }

    public boolean isAddToBackStack() {
        return addToBackStack;
    }
}
