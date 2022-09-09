package com.rhinodesktop.activityanimatorutility.models;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

import com.rhinodesktop.activityanimatorutility.fragmentutils.FragmentAndAnimateManager;

/**
 * Created by hunter on 2018-02-13.
 */

/**
 * This class is used by the {@link FragmentAndAnimateManager} to animate fragment transitions.
 */
public class FragmentInfo {

    private FragmentManager fragmentManager;
    private Fragment fragment;
    private int fragmentContainerResourceId;
    private FragmentTransactionInfo fragmentTransactionInfo;

    /**
     *
     * @param fragmentManager The fragment manager that will add the fragment to the view.
     * @param fragment The fragment to add to the view.
     * @param fragmentContainerResourceId The resource ID of the view that will contain the Fragment.
     *                                   Should be in the format: R.id.fragment_container_example
     * @param fragmentTransactionInfo More info regarding the fragment transaction.
     */
    public FragmentInfo(FragmentManager fragmentManager, Fragment fragment, int fragmentContainerResourceId,
                        FragmentTransactionInfo fragmentTransactionInfo) {
        this.fragmentManager = fragmentManager;
        this.fragment = fragment;
        this.fragmentContainerResourceId = fragmentContainerResourceId;
        this.fragmentTransactionInfo = fragmentTransactionInfo;
    }

    public FragmentManager getFragmentManager() {
        return fragmentManager;
    }

    public Fragment getFragment() {
        return fragment;
    }

    public int getFragmentContainerResourceId() {
        return fragmentContainerResourceId;
    }

    public FragmentTransactionInfo getFragmentTransactionInfo() {
        return fragmentTransactionInfo;
    }
}
