package com.rhinodesktop.foreorder_golf_consumer.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.rhinodesktop.foreorder_golf_consumer.R;
import com.rhinodesktop.foreorder_golf_consumer.adapters.MenuItemAdapter;
import com.rhinodesktop.foreorder_golf_consumer.events.ClubInRangeChangeEvent;
import com.rhinodesktop.foreorder_golf_consumer.interfaces.OnClubInRangeChangeListener;
import com.rhinodesktop.foreorder_golf_consumer.managers.CurrentOrder;
import com.rhinodesktop.foreorder_golf_consumer.managers.LinearLayoutManagerWithSmoothScroller;
import com.rhinodesktop.foreorder_golf_consumer.models.Menu;
import com.rhinodesktop.foreorder_golf_consumer.models.Order;
import com.rhinodesktop.foreorder_golf_consumer.models.enums.DrawerToolbarType;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import io.realm.Realm;

/**
 * Created by rhinodesktop on 2017-03-20.
 */

public class MenuListFragment extends Fragment {

    private static final String MENU_KEY = "menu_key";
    private Menu menu;
    private Context context;
    private OnClubInRangeChangeListener clubInRangeChangeListener;

    public static MenuListFragment newInstance(Menu menu) {
        Realm realm = Realm.getDefaultInstance();
        MenuListFragment menuListFragment = new MenuListFragment();
        Bundle bundle = new Bundle();
        Gson gson = new Gson();
        String menuJson = gson.toJson(realm.copyFromRealm(menu));
        bundle.putString(MENU_KEY, menuJson);
        menuListFragment.setArguments(bundle);
        realm.close();
        return menuListFragment;
    }

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        String menuJson = getArguments().getString(MENU_KEY);
        Gson gson = new Gson();
        menu = gson.fromJson(menuJson, Menu.class);
        View rootView = inflater.inflate(R.layout.food_list_fragment, container, false);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        RecyclerView recyclerView = rootView.findViewById(R.id.recyclerview_food_items);
        initRecyclerView(recyclerView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        super.onDestroyView();
    }

    @Subscribe
    public void onInClubGeoFenceEvent(ClubInRangeChangeEvent event) {
        if (event.isInRange()) {
            clubInRangeChangeListener.onClubInRangeChangeEvent(DrawerToolbarType.FoodItemListAppBar);
            // empty cart
            Order order = CurrentOrder.getInstance().getOrder();
            order.clearOrder();
        } else {
            clubInRangeChangeListener.onClubInRangeChangeEvent(DrawerToolbarType.CourseListAppBar);
        }
    }

    private void initRecyclerView(RecyclerView foodItemsRecyclerView) {
        MenuItemAdapter menuItemAdapter = new MenuItemAdapter(getActivity(), menu);
        foodItemsRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManagerWithSmoothScroller(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        foodItemsRecyclerView.setLayoutManager(layoutManager);
        foodItemsRecyclerView.setAdapter(menuItemAdapter);
    }
}
