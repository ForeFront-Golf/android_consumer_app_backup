package com.rhinodesktop.activityanimatorutility.fragmentutils;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;


import androidx.appcompat.app.AppCompatActivity;

import com.rhinodesktop.activityanimatorutility.models.AnimatorType;
import com.rhinodesktop.activityanimatorutility.models.FragmentInfo;
import com.rhinodesktop.activityanimatorutility.models.FragmentTransactionInfo;

/**
 * Created by rhinodesktop on 2017-02-01.
 */

/**
 * This class provides methods that allow animations to occur when transitioning
 * between fragments {@link Fragment}.
 */
public class FragmentAndAnimateManager {

    private static FragmentAndAnimateManager instance;

    private FragmentAndAnimateManager() {}

    public static FragmentAndAnimateManager getInstance() {
        if (instance == null) {
            instance = new FragmentAndAnimateManager();
        }
        return instance;
    }

    /**
     * Adds a fragment to a view.
     *
     * @param fragmentInfo Fragment info required for the fragment transaction.
     */
    public void addFragment(FragmentInfo fragmentInfo) {
        FragmentTransactionInfo fragmentTransactionInfo = fragmentInfo.getFragmentTransactionInfo();
        FragmentTransaction fragmentTransaction = fragmentInfo.getFragmentManager().beginTransaction();
        fragmentTransaction.add(fragmentInfo.getFragmentContainerResourceId(), fragmentInfo.getFragment(), fragmentTransactionInfo.getTag());
        if(fragmentTransactionInfo.isAddToBackStack()) {
            fragmentTransaction.addToBackStack(fragmentTransactionInfo.getTransName());
        }
        fragmentTransaction.commit();
    }

    /**
     * Changes a fragment in a view. Allows options to animate the transition.
     *
     * @param fragmentInfo Fragment info required for the fragment transaction.
     * @param animType The type of animation that will occur when transitioning between fragments.
     * @param popAnimType The animation that will occur when the fragment is popped from the back stack.
     */
    public void changeFragment(FragmentInfo fragmentInfo, AnimatorType animType, AnimatorType popAnimType) {
        FragmentTransactionInfo fragmentTransactionInfo = fragmentInfo.getFragmentTransactionInfo();
        FragmentTransaction fragmentTransaction = fragmentInfo.getFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(animType.getEnterAnimator(), animType.getExitAnimator(), popAnimType.getEnterAnimator(), popAnimType.getExitAnimator());
        fragmentTransaction.replace(fragmentInfo.getFragmentContainerResourceId(), fragmentInfo.getFragment(), fragmentTransactionInfo.getTag());
        if(fragmentTransactionInfo.isAddToBackStack()) {
            fragmentTransaction.addToBackStack(fragmentTransactionInfo.getTransName());
        }
        fragmentTransaction.commit();
    }

    /**
     * This method should only be used in rare cases. {@link AppCompatActivity#onBackPressed()} will pop
     * any fragments on the back stack by default when the back button is pressed on the containing
     * activity. If you need to pop a fragment before {@link AppCompatActivity#onBackPressed()} is called
     * then call one of the FragmentManager's popBackStack methods {@link FragmentManager#popBackStack()}.
     * This is the proper way to remove fragments.
     *
     * Only use on this method if the default functionality does not satisfy your use case.
     *
     * @param fragment The fragment to remove.
     */
    public void removeFragment(Fragment fragment) {
        FragmentManager fragmentManager = fragment.getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.remove(fragment);
        fragmentTransaction.commit();
    }
}
