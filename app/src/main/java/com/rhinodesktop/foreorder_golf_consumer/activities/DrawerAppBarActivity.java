package com.rhinodesktop.foreorder_golf_consumer.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.core.view.GravityCompat;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.drawerlayout.widget.DrawerLayout;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.appbar.AppBarLayout;
import com.rhinoactive.foreorder_library_android.utils.Constants;
import com.rhinoactive.imageutility.amazonfiledownloaders.AmazonImageGlideDownloader;
import com.rhinoactive.imageutility.amazonfiledownloaders.AmazonImageViewDownloader;
import com.rhinoactive.imageutility.amazonimageuploaders.AmazonImageFileUploader;
import com.rhinoactive.imageutility.legaldocutils.DocS3LinkUtils;
import com.rhinodesktop.activityanimatorutility.activityutils.ActivityAndAnimateManager;
import com.rhinodesktop.activityanimatorutility.models.AnimationType;
import com.rhinodesktop.facebookutilities.LogoutHandler;
import com.rhinodesktop.foreorder_golf_consumer.BuildConfig;
import com.rhinodesktop.foreorder_golf_consumer.R;
import com.rhinodesktop.foreorder_golf_consumer.events.UserImageUpdatedEvent;
import com.rhinodesktop.foreorder_golf_consumer.facebook.ForeOrderLogoutHandler;
import com.rhinodesktop.foreorder_golf_consumer.listeners.TitleImageOnOffsetChangedListener;
import com.rhinodesktop.foreorder_golf_consumer.managers.apirequestmanagers.ForeOrderImageUploader;
import com.rhinodesktop.foreorder_golf_consumer.models.Club;
import com.rhinodesktop.foreorder_golf_consumer.models.User;
import com.rhinodesktop.foreorder_golf_consumer.models.enums.DrawerToolbarType;
import com.rhinodesktop.foreorder_golf_consumer.utils.ForeOrderResourceUtils;
import com.rhinodesktop.foreorder_golf_consumer.utils.ForeOrderSharedPrefUtils;
import com.rhinodesktop.foreorder_golf_consumer.utils.ForeOrderToastUtils;
import com.rhinodesktop.foreorder_golf_consumer.utils.imagecaptureutils.ActivityImageCaptureUtils;
import com.rhinodesktop.foreorder_golf_consumer.utils.imagecaptureutils.ImageCaptureUtils;
import com.rhinodesktop.foreorder_golf_consumer.views.JumpNavButton;
import com.rhinodesktop.locationutilities.newlocationupdates.utils.LastKnownLocationUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;

import io.realm.Realm;
import lombok.NonNull;

/**
 * Created by Hunter Andrin on 2017-03-15.
 */

public abstract class DrawerAppBarActivity extends NoInternetAppBarActivity {

    private Realm mRealm;
    private DrawerLayout drawer;
    private JumpNavButton jumpNavButton;
    private ImageView profileImageView;
    private ImageCaptureUtils imageCaptureUtils = new ActivityImageCaptureUtils(this);
    private AppBarLayout appBarLayout;
    private String mCurrentClubName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        mRealm = Realm.getDefaultInstance();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (drawer.isDrawerVisible(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    public void onDestroy() {
        mRealm.close();
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        File imageFile = imageCaptureUtils.handleOnActivityResults(requestCode, resultCode, data);
        if (imageFile != null) {
            String uploadingMsg = ForeOrderResourceUtils.getInstance().strRes(R.string.uploading_image);
            ForeOrderToastUtils.getInstance().displayToastFromMainThreadShort(uploadingMsg);
            AmazonImageFileUploader amazonImageFileUploader = new AmazonImageFileUploader(imageFile);
            ForeOrderImageUploader.uploadImageToAmazon(amazonImageFileUploader);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        imageCaptureUtils.handleOnRequestPermissionResult(requestCode, permissions, grantResults);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUserImageUpdatedEvent(UserImageUpdatedEvent event) {
        loadProfileImage();
    }

    protected void initToolbar(DrawerToolbarType toolbarType) {
        initToolbarView();
        jumpNavButton = findViewById(R.id.jumpnavbutton_widget);
        switch (toolbarType) {
            case CourseListAppBar:
                initCourseListAppBar();
                jumpNavButton.setVisibility(View.GONE);
                break;
            case FoodItemListAppBar:
                initMenuListAppBar();
                jumpNavButton.setVisibility(View.VISIBLE);
            default:
                break;
        }
    }

    @Override
    protected void initToolbarView() {
        super.initToolbarView();
        appBarLayout = findViewById(R.id.app_bar_layout);
        toolbar.setBackground(null);
        initCustomHamburgerIcon();
        initDrawerViews();
    }

    private void initTitleOffsetListenerForMenu() {
        RelativeLayout titleLayout = findViewById(R.id.rlayout_course_title_expanded);
        TextView titleTextView = findViewById(R.id.textview_toolbar_title);
        titleTextView.setVisibility(View.VISIBLE);
        titleTextView.setText(mCurrentClubName);
        AppBarLayout.OnOffsetChangedListener onOffsetChangedListener = new TitleImageOnOffsetChangedListener(titleTextView, titleLayout);
        appBarLayout.addOnOffsetChangedListener(onOffsetChangedListener);
    }

    private void initTitleOffsetListenerForCourse() {
        RelativeLayout noCoursesTextViews = findViewById(R.id.rlayout_not_at_course_text);
        TextView titleTextView = findViewById(R.id.textview_toolbar_title);
        titleTextView.setText(null);
        AppBarLayout.OnOffsetChangedListener onOffsetChangedListener = new TitleImageOnOffsetChangedListener(titleTextView, noCoursesTextViews);
        appBarLayout.addOnOffsetChangedListener(onOffsetChangedListener);
    }

    private void initCustomHamburgerIcon() {
        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.setDrawerIndicatorEnabled(false); //disable "hamburger to arrow" drawable
        toggle.syncState();
        drawer.addDrawerListener(toggle);
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawer.isDrawerVisible(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    drawer.openDrawer(GravityCompat.START);
                }
            }
        };
        super.initLeftButton(R.drawable.icon_menu_wht, clickListener);
    }

    private void initMenuListAppBar() {
        hideCourseButtons();
        RelativeLayout courseHeaderLayout = findViewById(R.id.rlayout_course_list_header);
        courseHeaderLayout.setVisibility(View.GONE);
        RelativeLayout menuHeaderLayout = findViewById(R.id.rlayout_menu_list_header);
        menuHeaderLayout.setVisibility(View.VISIBLE);
        TextView courseNameTextView = findViewById(R.id.textview_menu_list_course_name);

        int currentClubId = ForeOrderSharedPrefUtils.getCurrentClubId(this);
        if (currentClubId != 0) {
            mCurrentClubName = mRealm.where(Club.class).equalTo("clubId", currentClubId).findFirst().getName();
        }

        courseNameTextView.setText(mCurrentClubName);
        //Add margin to make room for the view pager tab layout to stay in view when the app bar is collapsed
//        ViewUtils.setMargins(toolbar, 0, 0, 0, toolbarMarginBottom);
        initTitleOffsetListenerForMenu();
    }

    private void initCourseListAppBar() {
        showCourseButtons();
        RelativeLayout menuHeaderLayout = findViewById(R.id.rlayout_menu_list_header);
        menuHeaderLayout.setVisibility(View.GONE);
        RelativeLayout courseHeaderLayout = findViewById(R.id.rlayout_course_list_header);
        courseHeaderLayout.setVisibility(View.VISIBLE);
        //remove margin from the toolbar
//        ViewUtils.setMargins(toolbar, 0, 0, 0, 0);
        initTitleOffsetListenerForCourse();
    }

    private void showCourseButtons() {
        showRefresh();
        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LastKnownLocationUtils.getLastKnownLocation(DrawerAppBarActivity.this);
            }
        });
        showLogo();
    }

    private void hideCourseButtons() {
        hideRefresh();
        hideLogo();
    }

    private void initDrawerViews() {
        setLegalDocLinks();
        TextView userNameTextView = findViewById(R.id.textview_user_name);
        userNameTextView.setText(mRealm.where(User.class).findFirst().getFullName());
        TextView versionNumberTextView = findViewById(R.id.textview_version_number);
        versionNumberTextView.setText(BuildConfig.VERSION_NAME);
        profileImageView = findViewById(R.id.imageview_user_image_menu);
        initProfileImage();
        initLogoutButton();
    }

    private void setLegalDocLinks() {
        LinearLayout policyLayout = findViewById(R.id.llayout_privacy_policy);
        LinearLayout termsLayout = findViewById(R.id.llayout_terms_of_use);
        ForeOrderResourceUtils resourceUtils = ForeOrderResourceUtils.getInstance();
        String fileProviderAuth = resourceUtils.strRes(R.string.file_provider_authority);
        DocS3LinkUtils.setLegalDocLinkClickListeners(policyLayout, this, Constants.PRIVACY_POLICY_URL, fileProviderAuth);
        DocS3LinkUtils.setLegalDocLinkClickListeners(termsLayout, this, Constants.TERMS_OF_USE_URL, fileProviderAuth);
        LinearLayout membershipLayout = findViewById(R.id.llayout_membership);
        membershipLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DrawerAppBarActivity.this, MembershipActivity.class));
            }
        });
    }

    private void initProfileImage() {
        loadProfileImage();
        initProfileImageClickListener();
    }

    private void initProfileImageClickListener() {
        View.OnClickListener createPhotoDialogClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageCaptureUtils.createPhotoDialogIfPermissionGranted();
            }
        };
        profileImageView.setOnClickListener(createPhotoDialogClickListener);
        TextView editPhotoTextView = findViewById(R.id.textview_edit_profile);
        editPhotoTextView.setOnClickListener(createPhotoDialogClickListener);
    }

    private void loadProfileImage() {
        User currentUser = mRealm.where(User.class).findFirst();
        if (currentUser.getProfilePhotoUrl() != null) {
            AmazonImageGlideDownloader amazonImageDownloader = new AmazonImageViewDownloader(profileImageView);
            amazonImageDownloader.doNotCheckCacheForFile();
            amazonImageDownloader.skipGlideCache();
            amazonImageDownloader.circleImage();
            amazonImageDownloader.downloadFile(currentUser.createProfileImageUrl());
        }
    }

    private void initLogoutButton() {
        RelativeLayout logoutLayout = findViewById(R.id.rlayout_logout);
        logoutLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogoutHandler logoutHandler = new ForeOrderLogoutHandler();
                logoutHandler.logout();
                ActivityAndAnimateManager.Builder builder = new ActivityAndAnimateManager.Builder(DrawerAppBarActivity.this, LoginActivity.class, AnimationType.FADE_IN);
                builder.clearStack();
                builder.buildActivityAndTransition();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (jumpNavButton.isJumpNavOpen()) {
            jumpNavButton.handleOpenCloseOfJumpNav();
        } else {
            super.onBackPressed();
        }
    }
}
