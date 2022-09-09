package com.rhinodesktop.foreorder_golf_consumer.activities;

import android.app.Fragment;
import android.os.Bundle;
import android.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;


import com.rhinoactive.foreorder_library_android.utils.Constants;
import com.rhinoactive.generalutilities.GpsUtils;
import com.rhinoactive.generalutilities.MainThreadRunner;
import com.rhinodesktop.activityanimatorutility.activityutils.ActivityAnimationUtils;
import com.rhinodesktop.activityanimatorutility.fragmentutils.SupportFragmentAnimateManager;
import com.rhinodesktop.activityanimatorutility.models.AnimationType;
import com.rhinodesktop.foreorder_golf_consumer.R;
import com.rhinodesktop.foreorder_golf_consumer.fragments.CourseListFragment;
import com.rhinodesktop.foreorder_golf_consumer.fragments.MenuFragment;
import com.rhinodesktop.foreorder_golf_consumer.fragments.NoGpsFragment;
import com.rhinodesktop.foreorder_golf_consumer.interfaces.OnClubInRangeChangeListener;
import com.rhinodesktop.foreorder_golf_consumer.models.Club;
import com.rhinodesktop.foreorder_golf_consumer.models.enums.DrawerToolbarType;
import com.rhinodesktop.foreorder_golf_consumer.utils.ForeOrderSharedPrefUtils;
import com.rhinodesktop.locationutilities.newlocationupdates.utils.LastKnownLocationUtils;

import io.realm.Realm;

public class MainActivity extends DrawerAppBarActivity implements OnClubInRangeChangeListener {

    private TextView coursesNearYouTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        LastKnownLocationUtils.getLastKnownLocation(this);
        Fragment noGpsFragment = getFragmentManager().findFragmentByTag(Constants.NO_GPS_FRAGMENT_TAG);
        if (GpsUtils.isGpsEnabled(this) && noGpsFragment != null) {
            getSupportFragmentManager().beginTransaction().remove(getSupportFragmentManager().findFragmentByTag(Constants.NO_GPS_FRAGMENT_TAG)).commit();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ActivityAnimationUtils.animateActivityTransition(this, AnimationType.FADE_IN);
    }

    @Override
    public void onClubInRangeChangeEvent(final DrawerToolbarType drawerToolbarType) {
        MainThreadRunner.runFromUiThread(new Runnable() {
            @Override
            public void run() {
                Fragment fragment = getProperFragment(drawerToolbarType);
                SupportFragmentAnimateManager.getInstance().changeSupportFragment(getFragmentManager(), fragment, AnimationType.FADE_IN, R.id.fragment_container);
            }
        });
    }

    private void initViews() {
        coursesNearYouTextView = findViewById(R.id.textview_courses_near_you);
        initProperFragment();
    }

    private void initProperFragment() {
        // TODO: init fragment based on if user is in a geofence
        DrawerToolbarType drawerToolbarType;
        Realm realm = Realm.getDefaultInstance();

        Club currentClub = realm.where(Club.class).equalTo("clubId", ForeOrderSharedPrefUtils.getCurrentClubId(this)).findFirst();
        realm.close();

//        Club currentClub=null;
        if (currentClub == null) {
            drawerToolbarType = DrawerToolbarType.CourseListAppBar;
        } else {
            drawerToolbarType = DrawerToolbarType.FoodItemListAppBar;
        }
        Fragment fragment = getProperFragment(drawerToolbarType);
        SupportFragmentAnimateManager.getInstance().addSupportFragment(getFragmentManager(), fragment, R.id.fragment_container);
    }

    private Fragment getProperFragment(DrawerToolbarType drawerToolbarType) {
        Fragment fragment;
        if (drawerToolbarType.equals(DrawerToolbarType.CourseListAppBar)) {
            fragment = initCourseListFragment();
            if (!GpsUtils.isGpsEnabled(this)) {
                initNoGpsOverlay();
            }
        } else {
            fragment = initMenuFragment();
        }
        return fragment;
    }

    private void initNoGpsOverlay() {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.add(R.id.no_gps_layout, new NoGpsFragment(), Constants.NO_GPS_FRAGMENT_TAG);
        transaction.commit();
    }

    private Fragment initCourseListFragment() {
        coursesNearYouTextView.setVisibility(View.VISIBLE);
        initToolbar(DrawerToolbarType.CourseListAppBar);
        return new CourseListFragment();
    }

    private Fragment initMenuFragment() {
        coursesNearYouTextView.setVisibility(View.GONE);
        initToolbar(DrawerToolbarType.FoodItemListAppBar);
        return new MenuFragment();
    }
}
