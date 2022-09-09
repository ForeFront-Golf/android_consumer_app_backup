package com.rhinodesktop.foreorder_golf_consumer.managers.apirequestmanagers;

import android.app.Activity;


import androidx.annotation.Nullable;

import com.rhinoactive.jsonparsercallback.StandardCallback;
import com.rhinodesktop.facebookutilities.callbacks.LoginCallback;
import com.rhinodesktop.foreorder_golf_consumer.callbacks.DuplicateKeyCallback;
import com.rhinodesktop.foreorder_golf_consumer.callbacks.DuplicatePhoneNumberCallback;
import com.rhinodesktop.foreorder_golf_consumer.callbacks.LogoutCallback;
import com.rhinodesktop.foreorder_golf_consumer.callbacks.ResetPasswordCallback;
import com.rhinodesktop.foreorder_golf_consumer.callbacks.SendPinCallback;
import com.rhinodesktop.foreorder_golf_consumer.callbacks.ValidateSessionCallback;
import com.rhinodesktop.foreorder_golf_consumer.networking.ApiRequests;
import com.rhinodesktop.foreorder_golf_consumer.parsers.CreateAccountParserWithPhoto;
import com.rhinodesktop.foreorder_golf_consumer.parsers.ImagePathParser;
import com.rhinodesktop.foreorder_golf_consumer.parsers.ConfirmAccountInfoParser;
import com.rhinodesktop.foreorder_golf_consumer.parsers.ValidatePinParser;
import com.rhinodesktop.foreorder_golf_consumer.parsers.loginparsers.CreateAccountParser;
import com.rhinodesktop.foreorder_golf_consumer.parsers.loginparsers.ForeOrderLoginParser;

import java.io.File;

import okhttp3.Call;
import okhttp3.Callback;

/**
 * Created by rhinodesktop on 2017-03-30.
 */

public class AccountApiManager {

    public static void checkValidityOfCurrentSession() {
        ValidateSessionCallback validateSessionCallback = new ValidateSessionCallback();
        try {
            Call call = ApiRequests.getInstance().validateSession();
            call.enqueue(validateSessionCallback);
        } catch (Exception ex) {
            validateSessionCallback.handleFailure(ex);
        }
    }

    public static void emailLoginForUser(Activity activity, String email, String password) {
        ForeOrderLoginParser loginParser = new ForeOrderLoginParser(activity);
        try {
            StandardCallback callback = new LoginCallback(loginParser);
            Call call = ApiRequests.getInstance().logUserInWithEmail(email, password);
            call.enqueue(callback);
        } catch (Exception ex) {
            loginParser.handleError(ex);
        }
    }

    public static void invalidateCurrentUserSession(int userId) {
        LogoutCallback callback = new LogoutCallback();
        try {
            Call call = ApiRequests.getInstance().invalidateSession(userId);
            call.enqueue(callback);
        } catch (Exception ex) {
            callback.handleFailure(ex);
        }
    }

    public static void updateCurrentUserImagePath(int userId, String imageS3Path) {
        ImagePathParser imagePathParser = new ImagePathParser();
        try {
            StandardCallback callback = new StandardCallback(imagePathParser);
            Call call = ApiRequests.getInstance().updateCurrentUserImagePath(userId, imageS3Path);
            call.enqueue(callback);
        } catch (Exception e) {
            imagePathParser.handleError(e);
        }
    }

    public static void createUser(Activity activity, String firstName, String lastName, String email,
                                  String phoneNumber, String dateOfBirth, String password, @Nullable File userImageFile) {
        CreateAccountParser createAccountParser;
        if (userImageFile != null && userImageFile.exists()) {
            createAccountParser = new CreateAccountParserWithPhoto(activity, userImageFile);
        } else {
            createAccountParser = new CreateAccountParser(activity);
        }
        try {
            StandardCallback callback = new DuplicateKeyCallback(createAccountParser);
            Call call = ApiRequests.getInstance().createAccount(firstName, lastName, email, phoneNumber, dateOfBirth, password);
            call.enqueue(callback);
        } catch (Exception ex) {
            createAccountParser.handleError(ex);
        }
    }

    public static void validatePin(int userId, String pin) {
        ValidatePinParser validatePinParser = new ValidatePinParser();
        try {
            Callback callback = new StandardCallback(validatePinParser);
            Call call = ApiRequests.getInstance().validatePin(userId, pin);
            call.enqueue(callback);
        } catch (Exception ex) {
            validatePinParser.handleError(ex);
        }
    }

    public static void sendPin(int userId) {
        SendPinCallback callback = new SendPinCallback();
        try {
            Call call = ApiRequests.getInstance().sendPin(userId);
            call.enqueue(callback);
        } catch (Exception ex) {
            callback.displayPinErrorMessage(ex);
        }
    }

    public static void confirmAccountInformation(int userId, String firstName, String lastName, String email,
                                                 String phoneNumber, String dateOfBirth, @Nullable File userImageFile) {
        ConfirmAccountInfoParser confirmAccountInfoParser = new ConfirmAccountInfoParser(userImageFile);
        try {
            Callback callback = new DuplicatePhoneNumberCallback(confirmAccountInfoParser);
            Call call = ApiRequests.getInstance().verifyCurrentUserInfo(userId, firstName, lastName, email, phoneNumber, dateOfBirth);
            call.enqueue(callback);
        } catch (Exception ex) {
            confirmAccountInfoParser.handleError(ex);
        }
    }

    public static void resetPassword(String email) {
        ResetPasswordCallback resetPasswordCallback = new ResetPasswordCallback();
        try {
            Call call = ApiRequests.getInstance().resetPassword(email);
            call.enqueue(resetPasswordCallback);
        } catch (Exception ex) {
            resetPasswordCallback.handleError(ex);
        }
    }
}
