package com.rhinodesktop.activityanimatorutility.fragmentutils;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

import com.rhinodesktop.activityanimatorutility.models.AnimationType;

/**
 * Created by hunter on 2018-02-13.
 */

/**
 * If you are using the native version of Fragments in your app {@link Fragment},
 * then you need to use the {@link FragmentAndAnimateManager}
 *
 * Use this library in conjunction with the support fragment {@link Fragment}.
 * This class allows animations to occur when transitioning between support fragments.
 *
 */
public class SupportFragmentAnimateManager {

    private static SupportFragmentAnimateManager managerInstance;

    private SupportFragmentAnimateManager() {}

    public static SupportFragmentAnimateManager getInstance() {
        if (managerInstance == null) {
            managerInstance = new SupportFragmentAnimateManager();
        }
        return managerInstance;
    }

    /**
     *
     * @param fragmentManager The fragment manager that will add the fragment to the view.
     * @param fragment The fragment to add to the view.
     * @param fragmentContainerResourceId The resource ID of the view that will contain the Fragment. Should be in the format: R.id.fragment_container_example
     */
    public void addSupportFragment(FragmentManager fragmentManager, Fragment fragment, int fragmentContainerResourceId) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(fragmentContainerResourceId, fragment);
        fragmentTransaction.commit();
    }

    /**
     *
     * @param fragmentManager The fragment manager that will conduct the transaction.
     * @param fragment The new fragment that will replace whichever fragment is currently being displayed in the view.
     * @param animType The type of animation that will occur when transitioning between fragments.
     * @param fragmentContainerResourceId The resource ID of the view that will contain the Fragment. Should be in the format: R.id.fragment_container_example
     */
    public void changeSupportFragment(FragmentManager fragmentManager, Fragment fragment, AnimationType animType, int fragmentContainerResourceId) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.setCustomAnimations(animType.getEnterAnim(), animType.getExitAnim());
        fragmentTransaction.replace(fragmentContainerResourceId, fragment);
        fragmentTransaction.commitAllowingStateLoss();
    }

    /**
     * @param fragment The fragment to remove.
     */
    public void removeFragment(Fragment fragment) {
        FragmentManager fragmentManager = fragment.getFragmentManager();
        if (fragmentManager != null) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.remove(fragment);
            fragmentTransaction.commit();
        }
    }


}
