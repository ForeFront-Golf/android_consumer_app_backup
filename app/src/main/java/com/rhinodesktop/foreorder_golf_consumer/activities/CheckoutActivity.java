package com.rhinodesktop.foreorder_golf_consumer.activities;

import android.os.Bundle;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.rhinoactive.generalutilities.GpsUtils;
import com.rhinoactive.generalutilities.MainThreadRunner;
import com.rhinodesktop.foreorder_golf_consumer.R;
import com.rhinodesktop.foreorder_golf_consumer.adapters.OrderItemAdapter;
import com.rhinodesktop.foreorder_golf_consumer.events.PlaceOrderEvent;
import com.rhinodesktop.foreorder_golf_consumer.events.RemoveOrderItemEvent;
import com.rhinodesktop.foreorder_golf_consumer.managers.CurrentOrder;
import com.rhinodesktop.foreorder_golf_consumer.managers.LinearLayoutManagerWithSmoothScroller;
import com.rhinodesktop.foreorder_golf_consumer.managers.apirequestmanagers.OrderApiManager;
import com.rhinodesktop.foreorder_golf_consumer.models.Club;
import com.rhinodesktop.foreorder_golf_consumer.models.Order;
import com.rhinoactive.foreorder_library_android.utils.Constants;
import com.rhinodesktop.foreorder_golf_consumer.models.User;
import com.rhinodesktop.foreorder_golf_consumer.models.UserLocation;
import com.rhinodesktop.foreorder_golf_consumer.utils.ForeOrderDialogUtils;
import com.rhinoactive.generalutilities.PriceUtils;
import com.rhinodesktop.foreorder_golf_consumer.utils.ForeOrderSharedPrefUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Locale;

import io.realm.Realm;
import timber.log.Timber;

public class CheckoutActivity extends CheckoutAppBarActivity implements OnMapReadyCallback {

    private static final float INITIAL_ZOOM_DISTANCE = 18.0f;

    private Realm mRealm;
    private TextView totalPriceTextView;
    private RelativeLayout placeOrderLayout;
    private MaterialDialog progressDialog;
    private GoogleMap mMap;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        mRealm = Realm.getDefaultInstance();

        initToolbar();
        EventBus.getDefault().register(this);
        initViews();
        connectToMaps();
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        mRealm.close();
        super.onDestroy();
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        enableMyLocation();
    }

    @Subscribe
    public void onOrderItemRemovedEvent(RemoveOrderItemEvent event) {
        MainThreadRunner.runFromUiThread(new Runnable() {
            @Override
            public void run() {
                setTotalPriceTextView();
            }
        });
    }

    @Subscribe
    public void onPlaceOrderEvent(final PlaceOrderEvent event) {
        //TODO: start service 30 sec (if not already?)
        MainThreadRunner.runFromUiThread(new Runnable() {
            @Override
            public void run() {
                placeOrderLayout.setEnabled(true);
                progressDialog.dismiss();
                if (event.isSuccessful()) {
                    Order order = CurrentOrder.getInstance().getOrder();
                    order.clearOrder();
                    LinearLayout reviewOrderLayout = (LinearLayout) findViewById(R.id.llayout_order_review);
                    RelativeLayout backToMenuLayout = (RelativeLayout) findViewById(R.id.rlayout_back_to_menu);
                    LinearLayout orderConfirmationLayout = (LinearLayout) findViewById(R.id.llayout_order_confirmation);
                    reviewOrderLayout.setVisibility(View.GONE);
                    placeOrderLayout.setVisibility(View.GONE);
                    orderConfirmationLayout.setVisibility(View.VISIBLE);
                    backToMenuLayout.setVisibility(View.VISIBLE);
                    setBackToMenuClickListener(backToMenuLayout);
                } else {
                    Toast.makeText(CheckoutActivity.this, Constants.ERROR_OCCURRED_PLACING_ORDER, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void connectToMaps() {
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    protected void enableMyLocation() {
        try {
            if (mMap != null) {
                UserLocation currentLocation = mRealm.where(UserLocation.class).findFirst();
                double lat = currentLocation.getLat();
                double lng = currentLocation.getLon();
                LatLng currentLatLng = new LatLng(lat, lng);
                mMap.addMarker(new MarkerOptions().position(currentLatLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_map_marker)));
                mMap.getUiSettings().setMapToolbarEnabled(false);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, INITIAL_ZOOM_DISTANCE));
            }
        } catch (SecurityException ex) {
            // Permission to access the location is missing.
            Timber.e("User has not yet allowed the app to track the their location.");
            ex.printStackTrace();
        }
    }

    private void initViews() {
        initRecyclerView();
        initTotalPriceTextView();
        initPlaceOrderButton();
    }

    private void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recyclerview_your_order);
        OrderItemAdapter orderItemAdapter = new OrderItemAdapter(this, CurrentOrder.getInstance().getOrder());
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManagerWithSmoothScroller(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(orderItemAdapter);
    }

    private void initPlaceOrderButton() {
        placeOrderLayout = findViewById(R.id.rlayout_place_order);
        placeOrderLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!GpsUtils.isGpsEnabled(CheckoutActivity.this)) {
                    Toast.makeText(CheckoutActivity.this, getString(R.string.permission_required_to_place_order), Toast.LENGTH_LONG).show();
                } else {
                    placeOrderClicked();
                }
            }
        });
    }

    private void placeOrderClicked() {
        Order order = CurrentOrder.getInstance().getOrder();
        if (order.getTotalNumberOfItemsInOrder() > 0) {
            OrderApiManager.placeOrder(
                    mRealm.where(User.class).findFirst().getUserId(),
                    ForeOrderSharedPrefUtils.getCurrentClubId(this),
                    mRealm.where(UserLocation.class).findFirst());
            placeOrderLayout.setEnabled(false);
            progressDialog = ForeOrderDialogUtils.getInstance().showProgressDialog(this, Constants.ORDERING, null);
        } else {
            Toast.makeText(this, Constants.CART_EMPTY, Toast.LENGTH_SHORT).show();
        }
    }

    private void initTotalPriceTextView() {
        totalPriceTextView = findViewById(R.id.textview_your_order_total_price);
        setTotalPriceTextView();
    }

    private void setTotalPriceTextView() {
        float price = CurrentOrder.getInstance().getOrder().getPriceTotal();
        float hst = PriceUtils.calculateHst(price);
        float totalPrice = price + hst;

        if (mRealm.where(Club.class).equalTo("clubId", ForeOrderSharedPrefUtils.getCurrentClubId(this)).findFirst().getShowTax()) {
            totalPriceTextView.setText(String.format(Locale.ENGLISH, "$%.2f", totalPrice));
        } else {
            totalPriceTextView.setText(String.format(Locale.ENGLISH, "$%.2f", price));
        }
    }

    private void setBackToMenuClickListener(RelativeLayout backToMenuLayout) {
        backToMenuLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckoutActivity.super.onBackPressed();
            }
        });
    }
}
