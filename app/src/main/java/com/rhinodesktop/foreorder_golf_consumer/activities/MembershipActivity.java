package com.rhinodesktop.foreorder_golf_consumer.activities;

import android.content.Context;
import android.os.Bundle;

import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rhinoactive.foreorder_library_android.utils.Constants;
import com.rhinoactive.generalutilities.KeyboardUtils;
import com.rhinoactive.nointernetview.events.InternetConnectionChangeEvent;
import com.rhinodesktop.foreorder_golf_consumer.R;
import com.rhinodesktop.foreorder_golf_consumer.adapters.MembershipListAdapter;
import com.rhinodesktop.foreorder_golf_consumer.events.MembershipAddEvent;
import com.rhinodesktop.foreorder_golf_consumer.events.MembershipListEvent;
import com.rhinodesktop.foreorder_golf_consumer.events.PrivateClubsListEvent;
import com.rhinodesktop.foreorder_golf_consumer.events.RemoveMembershipEvent;
import com.rhinodesktop.foreorder_golf_consumer.managers.ForeOrderActivityAndAnimateManager;
import com.rhinodesktop.foreorder_golf_consumer.managers.apirequestmanagers.MembershipApiManager;
import com.rhinodesktop.foreorder_golf_consumer.models.Club;
import com.rhinodesktop.foreorder_golf_consumer.models.Membership;
import com.rhinodesktop.foreorder_golf_consumer.models.User;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

public class MembershipActivity extends NoInternetAppBarActivity implements AdapterView.OnItemSelectedListener {

    private final Context mContext = MembershipActivity.this;

    private Realm mRealm;
    private Spinner mCourseListSpinner;
    private RecyclerView mRecyclerView;
    private EditText mMemberCode;
    private int mCurrentClubId;
    private List<Club> mClubs;
    private ConstraintLayout mConstraintLayoutContent;
    private View.OnClickListener arrowButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            proceedToMainActivity();
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_membership);

        mRealm = Realm.getDefaultInstance();
        initViews();
    }

    @Override
    protected void onDestroy() {
        mRealm.close();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        proceedToMainActivity();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMembershipAddEvent(MembershipAddEvent membershipAddEvent) {
        if (membershipAddEvent.isSuccessful()) {
            mMemberCode.getText().clear();
            KeyboardUtils.closeKeyboard(this);
            refreshRecyclerView();
            Toast.makeText(this, Constants.MEMBERSHIP_ADDED_SUCCESSFULLY, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, Constants.ERROR_OCCURRED_ADDING_MEMBERSHIP, Toast.LENGTH_SHORT).show();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMemberShipListEvent(MembershipListEvent membershipListEvent) {
        List<Membership> mMembershipClubs = membershipListEvent.getMembershipList();
        MembershipListAdapter mAdapter = new MembershipListAdapter(this, mMembershipClubs, mRealm.where(User.class).findFirst().getUserId());
        mRecyclerView.setAdapter(mAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPrivateClubsListEvent(PrivateClubsListEvent privateClubsListEvent) {
        List<String> courseNameList = new ArrayList<>();
        if (privateClubsListEvent.isPrivateClubExist()) {
            mClubs = privateClubsListEvent.getPrivateClubsList(); // <- hit this endpoint for private clubs
            courseNameList.add(Constants.SELECT_A_CLUB);
            for (Club c : mClubs) {
                courseNameList.add(c.getName());
            }
        } else {
            courseNameList.add(Constants.NO_CLUBS_AVAILABLE);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_item, courseNameList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mCourseListSpinner.setAdapter(adapter);
        mCourseListSpinner.setOnItemSelectedListener(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onInternetConnectionChangedEvent(InternetConnectionChangeEvent internetConnectionChangeEvent) {
        if (internetConnectionChangeEvent.isConnected()) {
            mConstraintLayoutContent.animate().translationY(0);
        } else {
            mConstraintLayoutContent.animate().translationY(120);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRemoveMembershipEvent(RemoveMembershipEvent removeMembershipEvent) {
        if (removeMembershipEvent.isDeleted()) {
            refreshRecyclerView();
            Toast.makeText(this, Constants.MEMBERSHIP_REMOVED_SUCCESSFULLY, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, Constants.ERROR_OCCURRED_REMOVING_MEMBERSHIP, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        /** -1 for the offset. position 0 is 'Select a club' */
        if (mCourseListSpinner.getSelectedItemPosition() - 1 >= 0) {
            mCurrentClubId = mClubs.get(mCourseListSpinner.getSelectedItemPosition() - 1).getClubId();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    private void initViews() {
        initToolbar();
        initSpinner();
        initLayout();
        initRecyclerView();
        initKeyboardEvent();
    }

    private void initToolbar() {
        initToolbarView();

        if (Membership.cameFromPINScreen(mContext)) {
            initRightButton(R.drawable.icon_arrow_right_wht, arrowButtonListener);
        } else {
            initLeftButton(R.drawable.icon_arrow_left_wht, arrowButtonListener);
        }
        RelativeLayout topToolbar = findViewById(R.id.top_toolbar_layout);
        topToolbar.setBackgroundColor(getResources().getColor(R.color.fore_order_blue_membership));
        TextView textToolbar = findViewById(R.id.textview_toolbar_title);
        textToolbar.setText(Constants.MEMBERSHIP_TITLE);
        textToolbar.setVisibility(View.VISIBLE);
    }

    private void initSpinner() {
        mCourseListSpinner = findViewById(R.id.spinner_course);
        MembershipApiManager.getPrivateClubsList();
    }

    private void initLayout() {
        mMemberCode = findViewById(R.id.edittext_member_code);
        Button mAddMembershipButton = findViewById(R.id.button_add_membership);
        mAddMembershipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCourseListSpinner.getSelectedItemPosition() - 1 < 0) {
                    Toast.makeText(mContext, Constants.ERROR_OCCURRED_CLUB_NOT_SELECTED, Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(mMemberCode.getText())) {
                    Toast.makeText(mContext, Constants.ERROR_OCCURRED_EMPTY_MEMBER_ID, Toast.LENGTH_SHORT).show();
                } else {
                    MembershipApiManager.addMembership(mCurrentClubId, mMemberCode.getText().toString(), mRealm.where(User.class).findFirst().getUserId());
                }
            }
        });

        mConstraintLayoutContent = findViewById(R.id.cLayout_content);
    }

    private void initKeyboardEvent() {
        ConstraintLayout mainLayout = findViewById(R.id.activity_membership);
        mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KeyboardUtils.closeKeyboard(MembershipActivity.this);
            }
        });
    }

    private void initRecyclerView() {
        mRecyclerView = findViewById(R.id.recyclerview_membership);
        MembershipApiManager.getMembershipList(mRealm.where(User.class).findFirst().getUserId());
    }

    private void refreshRecyclerView() {
        MembershipApiManager.getMembershipList(mRealm.where(User.class).findFirst().getUserId());
    }

    private void proceedToMainActivity() {
        if (Membership.cameFromPINScreen(mContext)) {
            Membership.gotHereFromPINfragment(mContext, false);
            ForeOrderActivityAndAnimateManager.proceedToMainActivity(MembershipActivity.this);
        }
        finish();
    }
}
