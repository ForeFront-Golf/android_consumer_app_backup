package com.rhinodesktop.foreorder_golf_consumer.models;

import com.google.gson.annotations.SerializedName;
import com.rhinoactive.foreorder_library_android.utils.Constants;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import lombok.Getter;

/**
 * Created by Hunter Andrin on 2017-04-11.
 */

public class OptionGroup extends RealmObject {

    @PrimaryKey
    @SerializedName(Constants.OPTION_GROUP_ID)
    @Getter
    private Integer optionGroupId;
    @SerializedName(Constants.SINGLE_CHOICE)
    @Getter
    private Boolean singleChoice;
    @Getter
    private Boolean valid;
    @Getter
    private String name;
    @Getter
    private String desc;
    @Getter
    private Boolean required;
    @SerializedName(Constants.ITEMS)
    @Getter
    private RealmList<OptionItem> optionItems = new RealmList<>();
    @SerializedName(Constants.MODIFIED_AT)
    @Getter
    private Date modifiedAt;
}
