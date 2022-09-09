package com.rhinodesktop.foreorder_golf_consumer.models;

import com.google.gson.annotations.SerializedName;
import com.rhinoactive.foreorder_library_android.utils.Constants;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by rhinodesktop on 2017-03-14.
 */

public class User extends RealmObject {

    public User() {}

    @PrimaryKey
    @SerializedName(Constants.USER_ID)
    @Getter
    private Integer userId;
    @SerializedName(Constants.FIRST_NAME)
    @Getter
    private String firstName;
    @SerializedName(Constants.LAST_NAME)
    @Getter
    private String lastName;
    @SerializedName(Constants.PHONE_NUMBER)
    @Getter
    private String phoneNumber;
    @Getter
    private Integer age;
    @Getter
    private String dob;
    @Getter
    private String email;
    @SerializedName(Constants.PERM_LVL)
    @Getter
    private String permLvl;
    @Getter
    private Float rating;
    @SerializedName(Constants.PROFILE_PHOTO_URL)
    @Getter
    @Setter
    private String profilePhotoUrl;
    @Getter
    private Boolean valid;
    @SerializedName(Constants.RATING_COUNT)
    @Getter
    private Integer ratingCount;
    @SerializedName(Constants.SIGNUP_COMPLETE)
    @Getter
    private Boolean signUpComplete;
    @SerializedName(Constants.PHONE_VALID)
    @Getter
    private Boolean phoneValid;
    @Getter
    private Boolean banned;
    @SerializedName(Constants.MODIFIED_AT)
    @Getter
    private Date modifiedAt;

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public String createProfileImageUrl() {
        return "public/user/" + userId + "/profile.jpg";
    }
}
