package com.rhinodesktop.foreorder_golf_consumer.views;

import android.content.Context;

import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.rhinodesktop.foreorder_golf_consumer.R;
import com.rhinodesktop.foreorder_golf_consumer.managers.apirequestmanagers.AccountApiManager;
import com.rhinoactive.foreorder_library_android.utils.Constants;
import com.rhinodesktop.foreorder_golf_consumer.models.User;
import com.rhinodesktop.foreorder_golf_consumer.utils.ForeOrderToastUtils;

import io.realm.Realm;

/**
 * Created by Hunter Andrin on 2017-04-26.
 */

public class VerifyPinLayout extends LinearLayout {

    private EditText pinEditText;
    private Button sendButton;
    private TextView resendTextView;

    public VerifyPinLayout(Context context) {
        super(context);
        initViews();
    }

    public VerifyPinLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initViews();
    }

    public VerifyPinLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews();
    }

    private void initViews() {
        inflate(getContext(), R.layout.verify_pin_view, this);
        this.pinEditText = findViewById(R.id.edittext_verify_pin);
        this.sendButton = findViewById(R.id.button_verify_pin);
        this.resendTextView = findViewById(R.id.textview_resend_pin);
        setOnClickListeners();
    }

    private void setOnClickListeners() {
        final Realm realm = Realm.getDefaultInstance();
        sendButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                User currentUser = realm.where(User.class).findFirst();
                int userId = currentUser.getUserId();
                String pin = pinEditText.getText().toString();
                AccountApiManager.validatePin(userId, pin);
            }
        });

        resendTextView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                User currentUser = realm.where(User.class).findFirst();
                int userId = currentUser.getUserId();
                ForeOrderToastUtils.getInstance().displayToastFromMainThreadLong(Constants.RESENDING_PIN);
                AccountApiManager.sendPin(userId);
            }
        });
    }
}
