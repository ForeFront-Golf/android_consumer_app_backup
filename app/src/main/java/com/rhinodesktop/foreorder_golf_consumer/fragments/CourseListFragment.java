package com.rhinodesktop.foreorder_golf_consumer.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.rhinodesktop.foreorder_golf_consumer.R;
import com.rhinodesktop.foreorder_golf_consumer.adapters.CourseAdapter;
import com.rhinodesktop.foreorder_golf_consumer.events.ClubsUpdatedEvent;
import com.rhinodesktop.foreorder_golf_consumer.events.ClubInRangeChangeEvent;
import com.rhinodesktop.foreorder_golf_consumer.events.PermissionGrantedEvent;
import com.rhinodesktop.foreorder_golf_consumer.interfaces.OnClubInRangeChangeListener;

import com.rhinodesktop.foreorder_golf_consumer.managers.apirequestmanagers.ClubApiManager;
import com.rhinodesktop.foreorder_golf_consumer.managers.apirequestmanagers.LocationApiManager;
import com.rhinodesktop.foreorder_golf_consumer.models.Club;
import com.rhinodesktop.foreorder_golf_consumer.models.User;
import com.rhinodesktop.foreorder_golf_consumer.models.UserLocation;
import com.rhinodesktop.foreorder_golf_consumer.models.enums.DrawerToolbarType;
import com.rhinodesktop.foreorder_golf_consumer.utils.GolfCourseDataUtils;
import com.rhinodesktop.locationutilities.newlocationupdates.events.LastKnownLocationEvent;
import com.rhinodesktop.locationutilities.newlocationupdates.utils.LastKnownLocationUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import io.realm.Realm;

public class CourseListFragment extends Fragment {

    private Realm mRealm;
    private Context context;
    private OnClubInRangeChangeListener clubInRangeChangeListener;
    private CourseAdapter courseAdapter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        Activity activity;
        try {
            activity = (Activity) context;
            clubInRangeChangeListener = (OnClubInRangeChangeListener) activity;
        } catch (ClassCastException ex) {
            throw new ClassCastException(context.toString() + " must implement OnClubInRangeChangeListener");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRealm = Realm.getDefaultInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.course_list_fragment, container, false);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        initRecyclerView(rootView);
        LastKnownLocationUtils.getLastKnownLocation( getActivity());
//        ClubApiManager.getClubsNearCurrentUser(event.getLocation());

        return rootView;
    }

    @Override
    public void onDestroyView() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        mRealm.close();
        super.onDestroy();
    }

    @Subscribe (threadMode = ThreadMode.MAIN)
    public void onClubsUpdatedEvent(ClubsUpdatedEvent event) {
        //This is needed as the list won't update if there weren't any course in the list previously.
        List<Club> clubs = mRealm.where(Club.class).findAll();
        courseAdapter.setClubs(clubs);
        courseAdapter.notifyDataSetChanged();
    }

    @Subscribe
    public void onInClubGeoFenceEvent(ClubInRangeChangeEvent event) {
        if (event.isInRange()) {
            clubInRangeChangeListener.onClubInRangeChangeEvent(DrawerToolbarType.FoodItemListAppBar);
        }
    }

    @Subscribe
    public void onLocationUpdatesEvent(final LastKnownLocationEvent event) {
        final Location location = event.getLocation();
        try (Realm realm = Realm.getDefaultInstance()) {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.where(UserLocation.class).findAll().deleteAllFromRealm();
                    realm.insert(new UserLocation(location.getLatitude(), location.getLongitude(), location.getAccuracy()));
                }
            });
            LocationApiManager.updateCurrentUserLocationOnServer(realm.where(User.class).findFirst().getUserId(), event.getLocation());
            ClubApiManager.getClubsNearCurrentUser(event.getLocation());
        }
        GolfCourseDataUtils.setClubDistanceFromCurrentUser();
    }

    @Subscribe
    public void onPermissionGrantedEvent(PermissionGrantedEvent event) {
        LastKnownLocationUtils.getLastKnownLocation(getActivity());
    }

    private void initRecyclerView(View rootView) {
        List<Club> clubs = mRealm.where(Club.class).findAll();
        courseAdapter = new CourseAdapter(getActivity(), clubs);
        RecyclerView coursesRecyclerView = rootView.findViewById(R.id.recyclerview_courses);
        coursesRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        coursesRecyclerView.setLayoutManager(layoutManager);
        coursesRecyclerView.setAdapter(courseAdapter);
    }
}
