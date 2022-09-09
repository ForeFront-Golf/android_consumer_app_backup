package com.rhinodesktop.foreorder_golf_consumer.models;

import com.google.gson.annotations.SerializedName;
import com.rhinoactive.foreorder_library_android.utils.Constants;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import lombok.Getter;

/**
 * Created by Hunter Andrin on 2017-04-07.
 */

public class ItemType extends RealmObject {

    @PrimaryKey
    @SerializedName(Constants.ITEM_TYPE_ID)
    @Getter
    private Integer itemTypeId;
    @Getter
    private String name;
    @Getter
    private String desc;
    @SerializedName(Constants.MODIFIED_AT)
    @Getter
    private Date modifiedAt;
}
