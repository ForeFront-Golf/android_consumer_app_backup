package com.rhinodesktop.foreorder_golf_consumer.activities;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;


import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.afollestad.materialdialogs.MaterialDialog;
import com.appsee.Appsee;
import com.rhinoactive.foreorder_library_android.events.SessionValidCheckEvent;
import com.rhinoactive.generalutilities.KeyboardUtils;
import com.rhinoactive.generalutilities.MainThreadRunner;
import com.rhinoactive.generalutilities.ViewEnabler;
import com.rhinoactive.generalutilities.animations.AnimationUtils;
import com.rhinoactive.generalutilities.models.HideViewAnimation;
import com.rhinoactive.generalutilities.models.ShowViewAnimation;
import com.rhinoactive.imageutility.AwsMobileClientUtils;
import com.rhinoactive.imageutility.legaldocutils.DocS3LinkUtils;
import com.rhinoactive.imageutility.legaldocutils.LegalDocNameAndPath;
import com.rhinodesktop.activityanimatorutility.activityutils.ActivityAndAnimateManager;
import com.rhinodesktop.activityanimatorutility.activityutils.ActivityAnimationUtils;
import com.rhinodesktop.activityanimatorutility.fragmentutils.SupportFragmentAnimateManager;
import com.rhinodesktop.activityanimatorutility.models.AnimationType;
import com.rhinodesktop.facebookutilities.FacebookLoginManager;
import com.rhinodesktop.facebookutilities.FacebookPermission;
import com.rhinodesktop.facebookutilities.LogoutHandler;
import com.rhinodesktop.facebookutilities.callbacks.FacebookLoginButtonCallback;
import com.rhinodesktop.facebookutilities.callbacks.LoginCallback;
import com.rhinodesktop.foreorder_golf_consumer.R;
import com.rhinodesktop.foreorder_golf_consumer.events.DuplicatePhoneNumberEvent;
import com.rhinodesktop.foreorder_golf_consumer.events.InitVerifyPinEvent;
import com.rhinodesktop.foreorder_golf_consumer.events.LoginEvent;
import com.rhinodesktop.foreorder_golf_consumer.events.PasswordResetEvent;
import com.rhinodesktop.foreorder_golf_consumer.events.ConfirmInformationEvent;
import com.rhinodesktop.foreorder_golf_consumer.events.ValidatePinEvent;
import com.rhinodesktop.foreorder_golf_consumer.facebook.ForeOrderFacebookLoginButtonCallback;
import com.rhinodesktop.foreorder_golf_consumer.facebook.ForeOrderLogoutHandler;
import com.rhinodesktop.foreorder_golf_consumer.fragments.CreateAccountFragment;
import com.rhinodesktop.foreorder_golf_consumer.interfaces.CreateAccountFragmentListener;
import com.rhinodesktop.foreorder_golf_consumer.listeners.HideFragmentAnimationListener;
import com.rhinodesktop.foreorder_golf_consumer.managers.ForeOrderActivityAndAnimateManager;
import com.rhinodesktop.foreorder_golf_consumer.managers.apirequestmanagers.AccountApiManager;
import com.rhinodesktop.foreorder_golf_consumer.models.Membership;
import com.rhinodesktop.foreorder_golf_consumer.models.Session;
import com.rhinodesktop.foreorder_golf_consumer.models.User;
import com.rhinodesktop.foreorder_golf_consumer.networking.ApiRequests;
import com.rhinodesktop.foreorder_golf_consumer.parsers.loginparsers.FacebookLoginParser;
import com.rhinoactive.foreorder_library_android.utils.Constants;
import com.rhinodesktop.foreorder_golf_consumer.utils.ForeOrderApp;
import com.rhinodesktop.foreorder_golf_consumer.utils.ForeOrderDialogUtils;
import com.rhinodesktop.foreorder_golf_consumer.utils.ForeOrderResourceUtils;
import com.rhinodesktop.foreorder_golf_consumer.utils.ForeOrderToastUtils;
import com.rhinodesktop.foreorder_golf_consumer.views.VerifyPinLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

import static com.rhinodesktop.foreorder_golf_consumer.R.id.textview_use_email;

public class LoginActivity extends StandardToolbarActivity implements CreateAccountFragmentListener {

    private static final int animDuration = 200;

    private boolean isVerifyPinViewOpen = false;
    private boolean isConfirmInfoViewOpen = false;

    private Realm mRealm;
    private RelativeLayout emailLoginLayout;
    private RelativeLayout facebookLoginLayout;
    private RelativeLayout forgotPasswordLayout;
    private FrameLayout termsOfUseLayout;
    private TextView switchToEmailTextView;
    private TextView verifyPinTextView;
    private TextView forgotPasswordTextView;
    private TextView legalDocsTextView;
    private Button loginButton;
    private Button emailLoginButton;
    private ImageView logoImageView;
    private Fragment createAccountFragment;
    private MaterialDialog progressDialog;
    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText forgotPasswordEditText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AwsMobileClientUtils.initAwsMobileClient(this, ForeOrderApp.getAppContext());
        setContentView(R.layout.activity_login);

        mRealm = Realm.getDefaultInstance();

        initToolbar(ToolbarType.EmptyToolbar);
        initLogin();
        Appsee.start();
    }

    @Override
    protected void onStart() {
        EventBus.getDefault().register(this);
        super.onStart();
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mRealm.close();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        KeyboardUtils.closeKeyboard(this);
        if (isVerifyPinViewOpen || isConfirmInfoViewOpen) {
            return;
        }
        if (createAccountFragment != null) {
            closeCreateAccountFragment();
            reDisplayEmailLogin();
        } else if (emailLoginLayout.getVisibility() == View.VISIBLE) {
            useFacebookLogin();
        } else if (forgotPasswordLayout.getVisibility() == View.VISIBLE) {
            legalDocsTextView.setVisibility(View.VISIBLE);
            closeForgotPasswordLayout();
        } else {
            super.onBackPressed();
            ActivityAnimationUtils.animateActivityTransition(this, AnimationType.FADE_IN);
        }
    }

    @Override
    public void fragmentCreated(CreateAccountFragment createAccountFragment) {
        this.createAccountFragment = createAccountFragment;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        FacebookLoginManager.getInstance().onActivityResult(requestCode, resultCode, data);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSessionValidCheck(SessionValidCheckEvent event) {
        User currentUser = mRealm.where(User.class).findFirst();

        boolean isSessionValid = event.isSuccessful();
        if (isSessionValid && currentUser.getPhoneValid()) {
            Appsee.setUserId(String.valueOf(currentUser.getUserId()));
            ForeOrderActivityAndAnimateManager.proceedToAppropriateScreenUponLogin(this);
        } else {
            displayLoginButtons(true);
            try (Realm realm = Realm.getDefaultInstance()) {
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.where(User.class).findAll().deleteAllFromRealm();
                        realm.where(Session.class).findAll().deleteAllFromRealm();
                    }
                });
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoginEvent(final LoginEvent loginEvent) {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        emailLoginButton.setEnabled(true);
        if (!loginEvent.isSuccessful()) {
            displayLoginButtons(true);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onInitPinVerificationEvent(InitVerifyPinEvent event) {
        initVerifyPinView();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onConfirmInfoEvent(ConfirmInformationEvent event) {
        openConfirmInfoView();
    }

    @Subscribe
    public void onValidatePinEvent(ValidatePinEvent event) {
        if (event.isValidateSuccessful()) {
            Membership.gotHereFromPINfragment(this, true);
            ActivityAndAnimateManager.Builder builder = new ActivityAndAnimateManager.Builder(this, MembershipActivity.class, AnimationType.FADE_IN);
            builder.clearStack();
            builder.buildActivityAndTransition();
        } else {
            ForeOrderToastUtils.getInstance().displayToastFromMainThreadLong(Constants.INCORRECT_PIN);
        }
    }

    @Subscribe
    public void onDuplicatePhoneNumberEvent(DuplicatePhoneNumberEvent event) {
        ForeOrderToastUtils.getInstance().displayToastFromMainThreadLong(Constants.PHONE_NUMBER_ALREADY_IN_USE);
    }

    @Subscribe
    public void onPasswordResetEvent(PasswordResetEvent event) {
        ForeOrderResourceUtils resourceUtils = ForeOrderResourceUtils.getInstance();
        if (event.getCallbackCode().equals(PasswordResetEvent.PasswordResetCallbackCode.SUCCESS)) {
            if (forgotPasswordLayout.getVisibility() == View.VISIBLE) {
                closeForgotPasswordLayout();
            }
            ForeOrderToastUtils.getInstance().displayToastFromMainThreadLong(resourceUtils.strRes(R.string.reset_password_success));
        } else if (event.getCallbackCode().equals(PasswordResetEvent.PasswordResetCallbackCode.FAILURE)) {
            ForeOrderToastUtils.getInstance().displayToastFromMainThreadLong(resourceUtils.strRes(R.string.reset_password_failure));
        } else {
            ForeOrderToastUtils.getInstance().displayToastFromMainThreadLong(resourceUtils.strRes(R.string.reset_password_failure_three_attempts));
        }
    }

    public void useEmailLogin(View v) {
        ViewEnabler.enableDisableView(facebookLoginLayout, false);
        AnimationUtils.getInstance().showAnimation(emailLoginLayout, ShowViewAnimation.SLIDE_UP, animDuration);
        initToolbar(ToolbarType.BackButtonToolbar);
    }

    public void createAccount(View v) {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                emailLoginLayout.setVisibility(View.GONE);
            }
        }, animDuration);
        initToolbar(ToolbarType.ExitButtonToolbarFade);
       showCreateAccountFragment();
    }

    public void login(View v) {
        emailLoginButton.setEnabled(false);
        progressDialog = ForeOrderDialogUtils.getInstance().showProgressDialog(LoginActivity.this, Constants.LOGGING_IN, null);
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        AccountApiManager.emailLoginForUser(this, email, password);
    }

    public void forgotPassword(View v) {
        legalDocsTextView.setVisibility(View.INVISIBLE);
        AnimationUtils.getInstance().hideAnimation(emailLoginLayout, HideViewAnimation.SLIDE_DOWN, animDuration);
        AnimationUtils.getInstance().showAnimation(forgotPasswordLayout, ShowViewAnimation.SLIDE_UP, animDuration);
        AnimationUtils.getInstance().hideAnimation(logoImageView, HideViewAnimation.FADE_OUT, animDuration);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                AnimationUtils.getInstance().showAnimation(forgotPasswordTextView, ShowViewAnimation.FADE_IN, animDuration);
            }
        }, animDuration);
    }

    public void emailNewPassword(View v) {
        String email = forgotPasswordEditText.getText().toString();
        String resetting = ForeOrderResourceUtils.getInstance().strRes(R.string.resetting);
        Toast.makeText(this, resetting, Toast.LENGTH_SHORT).show();
        AccountApiManager.resetPassword(email);
    }

    private void initLogin() {
        initKeyboardEvent();
        initFacebookLogin();
        initEmailLogin();
        initLegalDocLinks();
        handleSessionValidity();
    }

    private void initKeyboardEvent() {
        RelativeLayout mainLayout = findViewById(R.id.activity_login);
        mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KeyboardUtils.closeKeyboard(LoginActivity.this);
            }
        });
    }

    private void initFacebookLogin() {
        facebookLoginLayout = findViewById(R.id.rlayout_sign_in_facebook);
        termsOfUseLayout = findViewById(R.id.layout_terms_of_use);
        switchToEmailTextView = findViewById(textview_use_email);
        LoginCallback loginCallback = new LoginCallback(new FacebookLoginParser(LoginActivity.this));
        loginButton = findViewById(R.id.fb_login_btn);
        initFacebookLoginButton(loginButton, loginCallback);
    }

    private void initEmailLogin() {
        logoImageView = findViewById(R.id.imageview_fore_order_logo);
        emailLoginLayout = findViewById(R.id.rlayout_sign_in_email);
        emailEditText = findViewById(R.id.edittext_email_login);
        passwordEditText = findViewById(R.id.edittext_password_login);
        emailLoginButton = findViewById(R.id.button_email_login);
        verifyPinTextView = findViewById(R.id.textview_verify_pin);
        forgotPasswordLayout = findViewById(R.id.rlayout_forgot_password);
        forgotPasswordEditText = findViewById(R.id.edittext_email_forgot_password);
        forgotPasswordTextView = findViewById(R.id.textview_forgot_password_title);
    }

    private void handleSessionValidity() {
        Session session = mRealm.where(Session.class).findFirst();
        if (session != null && session.getSessionId() != null) {
            displayLoginButtons(false);
            AccountApiManager.checkValidityOfCurrentSession();
        } else {
            displayLoginButtons(true);
        }
    }

    private void initFacebookLoginButton(Button loginButton, LoginCallback loginCallback) {
        List<String> requestedPermissions = new ArrayList<>();
        requestedPermissions.add(FacebookPermission.PUBLIC_PROFILE.toString());
        requestedPermissions.add(FacebookPermission.BIRTHDAY_PERMISSION.toString());
        requestedPermissions.add(FacebookPermission.EMAIL_PERMISSION.toString());
        List<FacebookPermission> requiredPermissions = new ArrayList<>();
        String loginUrl = ApiRequests.getInstance().getFacebookLoginUrl();
        LogoutHandler logoutHandler = new ForeOrderLogoutHandler();
        FacebookLoginButtonCallback loginButtonCallback = new ForeOrderFacebookLoginButtonCallback(this, loginUrl, loginButton, requiredPermissions, loginCallback, logoutHandler);
        FacebookLoginManager.getInstance().registerCustomLoginButton(LoginActivity.this, loginButton, loginButtonCallback, requestedPermissions);
    }

    private void displayLoginButtons(boolean display) {
        final int visibility;
        if (display) {
            visibility = View.VISIBLE;
        } else {
            visibility = View.INVISIBLE;
        }
        MainThreadRunner.runFromUiThread(new Runnable() {
            @Override
            public void run() {
                termsOfUseLayout.setVisibility(visibility);
                loginButton.setVisibility(visibility);
                switchToEmailTextView.setVisibility(visibility);
            }
        });
    }

    private void useFacebookLogin() {
        ViewEnabler.enableDisableView(facebookLoginLayout, true);
        AnimationUtils.getInstance().hideAnimation(emailLoginLayout, HideViewAnimation.SLIDE_DOWN, animDuration);
        MainThreadRunner.runFromUiThread(new Runnable() {
            @Override
            public void run() {
                initToolbar(ToolbarType.EmptyToolbar);
            }
        });
    }

    private void openConfirmInfoView() {
        isConfirmInfoViewOpen = true;
        initToolbar(ToolbarType.EmptyToolbar);
        ViewEnabler.enableDisableView(facebookLoginLayout, false);
        showCreateAccountFragment();
    }

    private void showCreateAccountFragment() {
        AnimationUtils.getInstance().hideAnimation(logoImageView, HideViewAnimation.FADE_OUT, animDuration);
        SupportFragmentAnimateManager.getInstance().changeSupportFragment(getFragmentManager(), new CreateAccountFragment(), AnimationType.SLIDE_UP, R.id.flayout_account_container);
    }

    private void initVerifyPinView() {
        isVerifyPinViewOpen = true;
        if (logoImageView.getVisibility() == View.VISIBLE) {
            AnimationUtils.getInstance().hideAnimation(logoImageView, HideViewAnimation.FADE_OUT, animDuration);
        }
        if (emailLoginLayout.getVisibility() == View.VISIBLE) {
            AnimationUtils.getInstance().hideAnimation(emailLoginLayout, HideViewAnimation.SLIDE_DOWN, animDuration);
        }
        if (createAccountFragment != null) {
            isConfirmInfoViewOpen = false;
            closeCreateAccountFragment();
        }

        ViewEnabler.enableDisableView(facebookLoginLayout, false);
        VerifyPinLayout verifyPinLayout = findViewById(R.id.layout_verify_pin);
        AnimationUtils.getInstance().showAnimation(verifyPinLayout, ShowViewAnimation.SLIDE_UP, animDuration);
        initToolbar(ToolbarType.EmptyToolbar);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                AnimationUtils.getInstance().showAnimation(verifyPinTextView, ShowViewAnimation.FADE_IN, animDuration);
            }
        }, animDuration);
    }

    private void closeForgotPasswordLayout() {
        AnimationUtils.getInstance().hideAnimation(forgotPasswordLayout, HideViewAnimation.SLIDE_DOWN, animDuration);
        AnimationUtils.getInstance().showAnimation(emailLoginLayout, ShowViewAnimation.SLIDE_UP, animDuration);
        AnimationUtils.getInstance().hideAnimation(forgotPasswordTextView, HideViewAnimation.FADE_OUT, animDuration);
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                AnimationUtils.getInstance().showAnimation(logoImageView, ShowViewAnimation.FADE_IN, animDuration);
            }
        }, animDuration);
    }

    private void closeCreateAccountFragment() {
        animateHideFragmentSlideDown();
        createAccountFragment = null;
    }

    private void animateHideFragmentSlideDown() {
        final Animation anim = HideViewAnimation.SLIDE_DOWN.getAnimation();
        final View view = createAccountFragment.getView();
        anim.setAnimationListener(new HideFragmentAnimationListener(createAccountFragment));
        anim.setDuration(animDuration);
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        MainThreadRunner.runFromUiThread(new Runnable() {
            @Override
            public void run() {
                if (view != null) {
                    view.startAnimation(anim);
                }
            }
        });
    }

    private void reDisplayEmailLogin() {
        MainThreadRunner.runFromUiThread(new Runnable() {
            @Override
            public void run() {
                emailLoginLayout.setVisibility(View.VISIBLE);
                initToolbar(ToolbarType.BackButtonToolbar);
                AnimationUtils.getInstance().showAnimation(logoImageView, ShowViewAnimation.FADE_IN, animDuration);
            }
        });
    }

    private void initLegalDocLinks() {
        legalDocsTextView = LoginActivity.this.findViewById(R.id.textview_legal_doc_links);
        ForeOrderResourceUtils resourceUtils = ForeOrderResourceUtils.getInstance();
        String precedingText = resourceUtils.strRes(R.string.terms_and_privacy_policy) + "\n";

        String fileProviderAuth = resourceUtils.strRes(R.string.file_provider_authority);
        String termsName = resourceUtils.strRes(R.string.terms_of_use);
        String privacyName = resourceUtils.strRes(R.string.privacy_policy);
        LegalDocNameAndPath terms = new LegalDocNameAndPath(termsName, Constants.TERMS_OF_USE_URL);
        LegalDocNameAndPath privacy = new LegalDocNameAndPath(privacyName, Constants.PRIVACY_POLICY_URL);
        DocS3LinkUtils.setLegalDocLinkSpannableString(legalDocsTextView, precedingText, terms, privacy, fileProviderAuth, this);
    }
}
