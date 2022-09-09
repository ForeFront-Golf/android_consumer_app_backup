package com.rhinodesktop.foreorder_golf_consumer.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import com.rhinoactive.foreorder_library_android.utils.Constants;
import com.rhinoactive.generalutilities.SizeConverter;
import com.rhinoactive.generalutilities.ViewUtils;
import com.rhinoactive.imageutility.amazonfiledownloaders.AmazonImageGlideDownloader;
import com.rhinoactive.imageutility.amazonfiledownloaders.AmazonImageViewDownloader;
import com.rhinodesktop.activityanimatorutility.activityutils.ActivityAndAnimateManager;
import com.rhinodesktop.activityanimatorutility.activityutils.externalactivities.ExternalActivity;
import com.rhinodesktop.activityanimatorutility.activityutils.externalactivities.activitieswithoutresult.ExternalPhoneActivity;
import com.rhinodesktop.activityanimatorutility.models.AnimationType;
import com.rhinodesktop.foreorder_golf_consumer.R;
import com.rhinodesktop.foreorder_golf_consumer.activities.CheckoutActivity;
import com.rhinodesktop.foreorder_golf_consumer.adapters.MenuPagerAdapter;
import com.rhinodesktop.foreorder_golf_consumer.events.MenusUpdatedEvent;
import com.rhinodesktop.foreorder_golf_consumer.listeners.OnMenuPageChangeListener;
import com.rhinodesktop.foreorder_golf_consumer.managers.CurrentOrder;
import com.rhinodesktop.foreorder_golf_consumer.managers.apirequestmanagers.MenuApiManager;
import com.rhinodesktop.foreorder_golf_consumer.models.Club;
import com.rhinodesktop.foreorder_golf_consumer.models.Menu;
import com.rhinodesktop.foreorder_golf_consumer.models.Order;
import com.rhinodesktop.foreorder_golf_consumer.utils.ForeOrderResourceUtils;
import com.rhinodesktop.foreorder_golf_consumer.utils.ForeOrderSharedPrefUtils;
import com.rhinodesktop.foreorder_golf_consumer.views.CustomTabLayout;
import com.rhinodesktop.foreorder_golf_consumer.views.JumpNavButton;
import com.rhinodesktop.foreorder_golf_consumer.views.ToggleSwipingPageViewer;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;
import java.util.Locale;

import io.realm.Realm;
import timber.log.Timber;

/**
 * Created by rhinodesktop on 2017-03-17.
 */

//TODO: turn this into an activity
public class MenuFragment extends Fragment {

    private static final int ADJUSTED_NAV_BUTTON_PADDING = 12;

    private Realm mRealm;
    private Club mCurrentClub;
    private View rootView;
    private ToggleSwipingPageViewer viewPager;
    private CustomTabLayout tabLayout;
    private RelativeLayout noMenusLayout;
    private TextView courseContactTextView;
    private RelativeLayout viewCartLayout;
    private TextView totalPriceTextView;
    private TextView numOfItemsTextView;
    private FrameLayout navButtonLayout;
    private FrameLayout fragmentLayout;
    private MenuPagerAdapter menuPagerAdapter;
    private boolean marginsAdjusted = false;
    private boolean shouldLoadAgain = true;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRealm = Realm.getDefaultInstance();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mCurrentClub = mRealm.where(Club.class).equalTo("clubId", ForeOrderSharedPrefUtils.getCurrentClubId(getContext())).findFirst();
        EventBus.getDefault().register(this);
        //Todo: check local database for menus for the club then make api call based on modified at time
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            MenuApiManager.getMenusForCurrentClub(getContext(), ForeOrderSharedPrefUtils.getCurrentClubId(getContext()));
        }
        rootView = inflater.inflate(R.layout.menu_fragment, container, false);
        findViewCartLayouts();
        initNoMenusAvailableViews();
        initTabs();
        initActivityViews();
        return rootView;
    }

    @Override
    public void onResume() {
        if (CurrentOrder.getInstance().getOrder().getTotalNumberOfItemsInOrder() > 0) {
            setViewCartView();
            enableDisableTabbing(false);
            if (!marginsAdjusted) {
                addMarginsForCartView();
                marginsAdjusted = true;
            }
        } else {
            enableDisableTabbing(true);
            if (marginsAdjusted) {
                resetViewMargins();
                marginsAdjusted = false;
            }
        }
        super.onResume();
    }

    @Override
    public void onStop() {
        viewCartLayout.setVisibility(View.INVISIBLE);
        super.onStop();
    }

    //Must set views to null in order to prevent a memory leak: http://stackoverflow.com/a/13422819/6575556
    @Override
    public void onDestroyView() {
        EventBus.getDefault().unregister(this);
        if (marginsAdjusted) {
            resetViewMargins();
        }
        rootView = null;
        viewPager = null;
        tabLayout.setVisibility(View.GONE);
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        mRealm.close();
        super.onDestroy();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMenusUpdatedEvent(MenusUpdatedEvent event) {
        if (event.isErrorInMenu()) {
            if (shouldLoadAgain) {
                shouldLoadAgain = false;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    MenuApiManager.getMenusForCurrentClub(getContext(), ForeOrderSharedPrefUtils.getCurrentClubId(getContext()));
                }
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    Toast.makeText(getContext(), Constants.ERROR_OCCURRED_GETTING_MENUS, Toast.LENGTH_LONG).show();
                }
            }
        } else if (mCurrentClub == null || mRealm.where(Menu.class).equalTo("clubId", mCurrentClub.getClubId()).findAll().isEmpty()) {
            displayNoMenusAvailable();
        } else {
            setUpViewPager();
            tabLayout.setupWithViewPager(viewPager);
        }
    }

    private void initNoMenusAvailableViews() {
        noMenusLayout = rootView.findViewById(R.id.rlayout_no_menus);
        courseContactTextView = rootView.findViewById(R.id.textview_course_contact);
    }

    private void displayNoMenusAvailable() {
        viewPager.setVisibility(View.GONE);
        createClickablePhoneNumber();
        noMenusLayout.setVisibility(View.VISIBLE);
    }

    private void createClickablePhoneNumber() {
        ForeOrderResourceUtils resourceUtils = ForeOrderResourceUtils.getInstance();
        String courseContactText = resourceUtils.strRes(R.string.course_contact);
        SpannableString precedingText = new SpannableString(courseContactText);
//        String clubPhoneNumber = mCurrentClub.getPhoneNumber();
        String clubPhoneNumber = "123-123-1234";
        SpannableString spannableString = new SpannableString(clubPhoneNumber);
        ClickableSpan clickableSpan = createClickableSpan(clubPhoneNumber);
        spannableString.setSpan(clickableSpan, 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        SpannedString entireText = (SpannedString) android.text.TextUtils.concat(precedingText, " ", spannableString);
        courseContactTextView.setText(entireText);
        courseContactTextView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private ClickableSpan createClickableSpan(final String clubPhoneNumber) {
        return new ClickableSpan() {
            @Override
            public void onClick(View view) {
                ExternalActivity phoneActivity = new ExternalPhoneActivity((AppCompatActivity) getActivity(), clubPhoneNumber);
                phoneActivity.start();
            }
        };
    }

    private void findViewCartLayouts() {
        viewCartLayout = getActivity().findViewById(R.id.rlayout_view_cart);
        totalPriceTextView = viewCartLayout.findViewById(R.id.textview_price_in_cart);
        numOfItemsTextView = viewCartLayout.findViewById(R.id.textview_number_of_items_in_cart);
        final Activity activity = getActivity();
        viewCartLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityAndAnimateManager.Builder builder = new ActivityAndAnimateManager.Builder((AppCompatActivity) activity, CheckoutActivity.class, AnimationType.SLIDE_UP);
                builder.buildActivityAndTransition();
            }
        });
    }

    private void initTabs() {
        viewPager = rootView.findViewById(R.id.viewpager);
        setUpViewPager();
        tabLayout = getActivity().findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setVisibility(View.VISIBLE);
    }

    private void setUpViewPager() {
        if (mCurrentClub == null || mRealm.where(Menu.class).equalTo("clubId", mCurrentClub.getClubId()).findAll().isEmpty()) {
            return;
        }
        Timber.e("how many times AM I CALLED?");
        noMenusLayout.setVisibility(View.GONE);
        viewPager.setVisibility(View.VISIBLE);
        menuPagerAdapter = new MenuPagerAdapter(getFragmentManager());
        addPagesToPageView();
        viewPager.setAdapter(menuPagerAdapter);
        setUpJumpSectionButtons();
    }

    private void addPagesToPageView() {
        List<Menu> currentMenus = mRealm.where(Menu.class).equalTo("clubId", mCurrentClub.getClubId()).findAll();
        for (Menu menu : currentMenus) {
            menuPagerAdapter.addFragment(menu);
        }
    }

    private void setUpJumpSectionButtons() {
        JumpNavButton jumpNavButton = getActivity().findViewById(R.id.jumpnavbutton_widget);
        FragmentActivity activity = (FragmentActivity) getActivity();
        List<Menu> menus = mRealm.where(Menu.class).equalTo("clubId", mCurrentClub.getClubId()).findAll();
        OnMenuPageChangeListener menuPageChangeListener = new OnMenuPageChangeListener(activity, jumpNavButton, menus);
        viewPager.addOnPageChangeListener(menuPageChangeListener);
        jumpNavButton.setJumpSectionOptions(activity, mRealm.where(Menu.class).equalTo("clubId", mCurrentClub.getClubId()).findFirst());
    }

    private void setViewCartView() {
        Order order = CurrentOrder.getInstance().getOrder();
        viewCartLayout.setVisibility(View.VISIBLE);
        totalPriceTextView.setText(String.format(Locale.ENGLISH, "$%.2f", order.getPriceTotal()));
        numOfItemsTextView.setText(String.format(Locale.ENGLISH, "%d", order.getTotalNumberOfItemsInOrder()));
    }

    private void enableDisableTabbing(boolean enable) {
        viewPager.setPagingEnabled(enable);
        LinearLayout tabStrip = (LinearLayout) tabLayout.getChildAt(0);
        tabStrip.setEnabled(enable);
        for (int i = 0; i < tabStrip.getChildCount(); i++) {
            tabStrip.getChildAt(i).setClickable(enable);
            if (viewPager.getCurrentItem() != i) {
                if (enable) {
                    tabStrip.getChildAt(i).setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            return false;
                        }
                    });
                } else {
                    tabStrip.getChildAt(i).setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    Toast.makeText(getContext(), getString(R.string.cannot_switch_menu), Toast.LENGTH_LONG).show();
                                }
                            }
                            return false;
                        }
                    });
                }
            }
        }
    }

    private void initActivityViews() {
        Activity activity = getActivity();
        navButtonLayout = activity.findViewById(R.id.flayout_jump_nav_button);
        fragmentLayout = activity.findViewById(R.id.fragment_container);
        if (mCurrentClub != null) {
            initCourseImages(activity);
        }
    }

    private void initCourseImages(Activity activity) {
        ImageView courseBackdropImageView = activity.findViewById(R.id.imageview_menu_backdrop);
        ImageView courseLogoImageView = activity.findViewById(R.id.imageview_course_logo);
        AmazonImageGlideDownloader amazonBackgroundImageDownloader = new AmazonImageViewDownloader(courseBackdropImageView);
        amazonBackgroundImageDownloader.downloadFile(mCurrentClub.getPhotoUrl());
        AmazonImageGlideDownloader amazonLogoImageDownloader = new AmazonImageViewDownloader(courseLogoImageView);
        amazonLogoImageDownloader.downloadFile(mCurrentClub.getPhotoUrlThumb());
    }

    private void addMarginsForCartView() {
        int height = viewCartLayout.getHeight();
        if (height == 0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                height = ForeOrderSharedPrefUtils.getViewCartLayoutHeight(getContext());
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                ForeOrderSharedPrefUtils.setViewCartLayoutHeight(getContext(), height);
            }
        }
        int heightWithPadding = 0; //add 12dp of padding//remove 12dp of padding
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            heightWithPadding = height - SizeConverter.dpToPx(ADJUSTED_NAV_BUTTON_PADDING, getContext());
        }
        ViewUtils.addMargins(navButtonLayout, 0, 0, 0, heightWithPadding);
        ViewUtils.addMargins(fragmentLayout, 0, 0, 0, height);
    }

    private void resetViewMargins() {
        int height = viewCartLayout.getHeight();
        int heightWithPadding = 0; //add 12dp of padding//remove 12dp of padding
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            heightWithPadding = height - SizeConverter.dpToPx(ADJUSTED_NAV_BUTTON_PADDING, getContext());
        }
        ViewUtils.subtractMargins(navButtonLayout, 0, 0, 0, heightWithPadding);
        ViewUtils.subtractMargins(fragmentLayout, 0, 0, 0, height);
    }
}
