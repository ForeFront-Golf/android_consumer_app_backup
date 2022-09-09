package com.rhinodesktop.foreorder_golf_consumer.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.text.method.TransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.request.RequestOptions;
import com.rhinoactive.imageutility.GlideImageViewLoader;
import com.rhinoactive.imageutility.amazonfiledownloaders.AmazonImageGlideDownloader;
import com.rhinoactive.imageutility.amazonfiledownloaders.AmazonImageViewDownloader;
import com.rhinodesktop.foreorder_golf_consumer.R;
import com.rhinodesktop.foreorder_golf_consumer.events.DuplicateKeyEvent;
import com.rhinodesktop.foreorder_golf_consumer.events.InitVerifyPinEvent;
import com.rhinodesktop.foreorder_golf_consumer.events.LoginEvent;
import com.rhinodesktop.foreorder_golf_consumer.interfaces.CreateAccountFragmentListener;
import com.rhinodesktop.foreorder_golf_consumer.managers.apirequestmanagers.AccountApiManager;
import com.rhinoactive.foreorder_library_android.utils.Constants;
import com.rhinodesktop.foreorder_golf_consumer.models.User;
import com.rhinodesktop.foreorder_golf_consumer.utils.ForeOrderDialogUtils;
import com.rhinodesktop.foreorder_golf_consumer.utils.ForeOrderResourceUtils;
import com.rhinodesktop.foreorder_golf_consumer.utils.ForeOrderToastUtils;
import com.rhinodesktop.foreorder_golf_consumer.utils.imagecaptureutils.FragmentImageCaptureUtils;
import com.rhinodesktop.foreorder_golf_consumer.utils.imagecaptureutils.ImageCaptureUtils;
import com.tsongkha.spinnerdatepicker.DatePicker;
import com.tsongkha.spinnerdatepicker.DatePickerDialog;
import com.tsongkha.spinnerdatepicker.SpinnerDatePickerDialogBuilder;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

public class CreateAccountFragment extends Fragment implements
        ActivityCompat.OnRequestPermissionsResultCallback, DatePickerDialog.OnDateSetListener {

    private Realm mRealm;
    private CreateAccountFragmentListener createAccountFragmentListener;
    private SpinnerDatePickerDialogBuilder spinnerDatePickerDialogBuilder;
    private ImageView userImageView;
    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private EditText phoneNumberEditText;
    private EditText dateOfBirthEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private Button createAccountButton;
    private ImageCaptureUtils imageCaptureUtils;
    private MaterialDialog progressDialog;
    private ImageView showPasswordImageView;
    @Nullable
    private File userImageFile;
    private boolean isVerifyingAccount;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            Activity activity = (Activity) context;
            createAccountFragmentListener = (CreateAccountFragmentListener) activity;
        } catch (ClassCastException ex) {
            throw new ClassCastException(context.toString() + " must implement CreateAccountFragmentListener");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRealm = Realm.getDefaultInstance();
    }

//    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.create_account_fragment, container, false);
        createAccountFragmentListener.fragmentCreated(this);
        imageCaptureUtils = new FragmentImageCaptureUtils(this);
        isVerifyingAccount = mRealm.where(User.class).findFirst() != null;
        initViews(rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        mRealm.close();
        super.onDestroy();
    }

    public CreateAccountFragment() {
        // Required fragment empty public constructor
    }

    @Override
    public void onStart() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        super.onStart();
    }

    @Override
    public void onStop() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        super.onStop();
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        userImageFile = imageCaptureUtils.handleOnActivityResults(requestCode, resultCode, data);
        if (userImageFile != null) {
            GlideImageViewLoader glideImageViewLoader = new GlideImageViewLoader(userImageView);
            glideImageViewLoader.setRequestOptions(RequestOptions.circleCropTransform());
            glideImageViewLoader.loadImageIntoImageView(userImageFile);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoginEvent(LoginEvent loginEvent) {
        reEnableCreateAccountViews();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDuplicateKeyEvent(DuplicateKeyEvent event) {
        ForeOrderToastUtils.getInstance().displayToastFromMainThreadLong(Constants.KEY_ALREADY_IN_USE);
        reEnableCreateAccountViews();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onInitPinVerificationEvent(InitVerifyPinEvent event) {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        String month = (monthOfYear + 1 < 10) ? String.format("0%s", monthOfYear + 1) : String.valueOf(monthOfYear + 1);
        String day = (dayOfMonth < 10) ? String.format("0%s", dayOfMonth) : String.valueOf(dayOfMonth);
        dateOfBirthEditText.setText(String.format("%s-%s-%s", year, month, day));
        spinnerDatePickerDialogBuilder.defaultDate(year, monthOfYear, dayOfMonth);
    }

    private void reEnableCreateAccountViews() {
        progressDialog.dismiss();
        createAccountButton.setEnabled(true);
    }

//    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("NewApi")
    private void initViews(View rootView) {
        RelativeLayout profileImageLayout = rootView.findViewById(R.id.rlayout_add_profile_photo);
        userImageView = rootView.findViewById(R.id.imageview_profile_image_create);
        User currentUser = mRealm.where(User.class).findFirst();
        if (currentUser != null && currentUser.getProfilePhotoUrl() != null) {
            AmazonImageGlideDownloader amazonImageDownloader = new AmazonImageViewDownloader(userImageView);
            amazonImageDownloader.doNotCheckCacheForFile();
            amazonImageDownloader.skipGlideCache();
            amazonImageDownloader.circleImage();
            amazonImageDownloader.downloadFile(currentUser.createProfileImageUrl());
        }
        firstNameEditText = rootView.findViewById(R.id.edittext_first_name);
        lastNameEditText = rootView.findViewById(R.id.edittext_last_name);
        phoneNumberEditText = rootView.findViewById(R.id.edittext_phone_number);
        emailEditText = rootView.findViewById(R.id.edittext_email_create);
        passwordEditText = rootView.findViewById(R.id.edittext_password_create);
        showPasswordImageView = rootView.findViewById(R.id.imageview_show_password);
        dateOfBirthEditText = rootView.findViewById(R.id.edittext_date_of_birth);
        dateOfBirthEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });
        createAccountButton = rootView.findViewById(R.id.button_create_account);
        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmCreateAccount();
            }
        });
        profileImageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageCaptureUtils.createPhotoDialogIfPermissionGranted();
            }
        });
        setShowPasswordClickListener();
        initViewsForVerifyAccount();
        spinnerDatePickerDialogBuilder = new SpinnerDatePickerDialogBuilder()
                .context(getContext())
                .callback(this)
                .spinnerTheme(R.style.DatePickerStyle)
                .showTitle(true)
                .defaultDate(2018, 0, 1)
                .maxDate(2019, 11, 31)
                .minDate(1900, 0, 1);
    }

    private void initViewsForVerifyAccount() {
        if (isVerifyingAccount) {
            User currentUser = mRealm.where(User.class).findFirst();
            firstNameEditText.setText(currentUser.getFirstName());
            lastNameEditText.setText(currentUser.getLastName());
            phoneNumberEditText.setText(currentUser.getPhoneNumber());
            passwordEditText.setVisibility(View.GONE);
            showPasswordImageView.setVisibility(View.GONE);
            emailEditText.setText(currentUser.getEmail());
            dateOfBirthEditText.setText(currentUser.getDob());
        }
    }

    private void setShowPasswordClickListener() {
        showPasswordImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TransformationMethod method = passwordEditText.getTransformationMethod();
                int start = passwordEditText.getSelectionStart();
                int end = passwordEditText.getSelectionEnd();
                if (method == null) {
                    passwordEditText.setTransformationMethod(new PasswordTransformationMethod());
                } else {
                    passwordEditText.setTransformationMethod(null);
                }
                passwordEditText.setSelection(start, end);
            }
        });
    }

    private void showDatePicker() {
        spinnerDatePickerDialogBuilder.build().show();
    }

    private void confirmCreateAccount() {
        String firstName = firstNameEditText.getText().toString().trim();
        String lastName = lastNameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String phoneNumber = phoneNumberEditText.getText().toString().trim();
        String dateOfBirth = dateOfBirthEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString();
        boolean isFormComplete = isFormComplete(firstName, lastName, email, phoneNumber, dateOfBirth, password);
        if (isFormComplete) {
            createAccountButton.setEnabled(false);
            progressDialog = ForeOrderDialogUtils.getInstance().showProgressDialog(getActivity(), Constants.CREATING_ACCOUNT, null);
            makeNetworkRequest(firstName, lastName, email, phoneNumber, dateOfBirth, password);
        } else {
            Toast.makeText(getActivity(), ForeOrderResourceUtils.getInstance().strRes(R.string.form_incomplete), Toast.LENGTH_SHORT).show();
        }
    }

    private void makeNetworkRequest(String firstName, String lastName, String email, String phoneNumber, String dateOfBirth, String password) {
        if (isVerifyingAccount) {
            int userId = mRealm.where(User.class).findFirst().getUserId();
            AccountApiManager.confirmAccountInformation(userId, firstName, lastName, email, phoneNumber, dateOfBirth, userImageFile);
        } else {
            AccountApiManager.createUser(getActivity(), firstName, lastName, email, phoneNumber, dateOfBirth, password, userImageFile);
        }
    }

    private boolean isFormComplete(String firstName, String lastName, String email, String phoneNumber, String dateOfBirth, String password) {
        List<String> strings = new ArrayList<>();
        strings.add(firstName);
        strings.add(lastName);
        strings.add(email);
        strings.add(phoneNumber);
        strings.add(dateOfBirth);
        if (!isVerifyingAccount) {
            strings.add(password);
        }
        return areStringsEmpty(strings);
    }

    private boolean areStringsEmpty(List<String> strings) {
        boolean isFormComplete = true;
        for (String text : strings) {
            if (TextUtils.isEmpty(text)) {
                isFormComplete = false;
                break;
            }
        }
        return isFormComplete;
    }
}
