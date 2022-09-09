package com.rhinodesktop.foreorder_golf_consumer.parsers;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.rhinoactive.foreorder_library_android.utils.Constants;
import com.rhinoactive.imageutility.amazonimageuploaders.AmazonImageFileUploader;
import com.rhinoactive.imageutility.amazonimageuploaders.AmazonImageUploader;
import com.rhinoactive.jsonparsercallback.JsonObjectParser;
import com.rhinodesktop.foreorder_golf_consumer.events.InitVerifyPinEvent;
import com.rhinodesktop.foreorder_golf_consumer.events.ValidatePinEvent;
import com.rhinodesktop.foreorder_golf_consumer.managers.apirequestmanagers.ForeOrderImageUploader;
import com.rhinodesktop.foreorder_golf_consumer.models.User;
import com.rhinodesktop.foreorder_golf_consumer.utils.ForeOrderToastUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.File;

import io.realm.Realm;
import timber.log.Timber;

/**
 * Created by Hunter Andrin on 2017-04-27.
 */

public class ConfirmAccountInfoParser extends JsonObjectParser<User> {

    File userImageFile;

    public ConfirmAccountInfoParser(File userImageFile) {
        this.userImageFile = userImageFile;
    }

    @Override
    public void handleError(Exception ex) {
        Timber.e("Account info confirmation failed with the following exception: %s", ex.toString());
        ForeOrderToastUtils.getInstance().displayToastFromMainThreadLong(Constants.ERROR_OCCURRED);
    }

    @Override
    protected String getJsonKey() {
        return Constants.USER_TABLE;
    }

    @Override
    protected User handleSuccessfulParse(JsonObject userObj, GsonBuilder builder) {
        return builder.create().fromJson(userObj, User.class);
    }

    @Override
    protected void postSuccessfulParsingLogic(final User user) {
        if(user.getPhoneValid()) {
            // If an existing user account is directed to 'CreateAccountFragment' just to put the dob, no pin verification is required.
            EventBus.getDefault().post(new ValidatePinEvent(true));
        } else {
            EventBus.getDefault().post(new InitVerifyPinEvent());
        }

        try (Realm realm = Realm.getDefaultInstance()) {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.where(User.class).findAll().deleteAllFromRealm();
                    realm.insert(user);
                }
            });
        }


        if (userImageFile != null) {
            AmazonImageUploader amazonImageUploader = new AmazonImageFileUploader(userImageFile);
            ForeOrderImageUploader.uploadImageToAmazon(amazonImageUploader);
        }
    }
}
