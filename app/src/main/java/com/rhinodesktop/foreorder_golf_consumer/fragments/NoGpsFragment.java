package com.rhinodesktop.foreorder_golf_consumer.fragments;

import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.afollestad.materialdialogs.MaterialDialog;
import com.rhinoactive.foreorder_library_android.utils.Constants;
import com.rhinoactive.generalutilities.GpsUtils;
import com.rhinodesktop.foreorder_golf_consumer.R;
import com.rhinodesktop.foreorder_golf_consumer.utils.ForeOrderDialogUtils;
import com.rhinodesktop.locationutilities.newlocationupdates.utils.LastKnownLocationUtils;

import lombok.NonNull;

/**
 * Created by sungwook on 2018-05-25.
 */

public class NoGpsFragment extends Fragment {

    private ConstraintLayout mMainLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.no_gps_fragment, container, false);
        setRefreshClickListener(view);
        return view;
    }


    private void setRefreshClickListener(View view) {
        Button refreshButton = view.findViewById(R.id.button_refresh);
        mMainLayout = view.findViewById(R.id.main_container);
        mMainLayout.setFocusable(true);
        mMainLayout.setClickable(true);
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkLocationServices();
            }
        });
    }

    private void checkLocationServices() {
        mMainLayout.setVisibility(View.GONE);
        MaterialDialog progressDialog = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            progressDialog = ForeOrderDialogUtils.getInstance().showProgressDialog(getContext(), Constants.CHECKING_LOCATION_SERVICES, null);
        }
        if (GpsUtils.isGpsEnabled(getActivity())) {
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
            getActivity().getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentByTag(Constants.NO_GPS_FRAGMENT_TAG)).commit();
            LastKnownLocationUtils.getLastKnownLocation(getActivity());
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Toast.makeText(getContext(), Constants.LOCATION_STILL_DISABLED, Toast.LENGTH_LONG).show();
            }
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
            mMainLayout.setVisibility(View.VISIBLE);
        }
    }
}
