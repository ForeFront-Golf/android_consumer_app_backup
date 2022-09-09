package com.rhinodesktop.foreorder_golf_consumer.callbacks;

import com.rhinoactive.imageutility.interfaces.CallbackInterface;
import com.rhinodesktop.foreorder_golf_consumer.R;
import com.rhinodesktop.foreorder_golf_consumer.managers.apirequestmanagers.AccountApiManager;
import com.rhinodesktop.foreorder_golf_consumer.utils.ForeOrderResourceUtils;
import com.rhinodesktop.foreorder_golf_consumer.utils.ForeOrderToastUtils;

/**
 * Created by hunter on 2018-03-19.
 */

public class AmazonImageUploadCallback implements CallbackInterface {

    private String s3Path;
    private int userId;

    public AmazonImageUploadCallback(int userId, String s3Path) {
        this.userId = userId;
        this.s3Path = s3Path;
    }

    @Override
    public void callbackHandlerUploadSuccessful() {
        AccountApiManager.updateCurrentUserImagePath(userId, s3Path);
    }

    @Override
    public void callbackHandlerUploadFailed() {
        ForeOrderResourceUtils resourceUtils = ForeOrderResourceUtils.getInstance();
        ForeOrderToastUtils.getInstance().displayToastFromMainThreadLong(resourceUtils.strRes(R.string.error_uploading_profile_photo));
    }
}
