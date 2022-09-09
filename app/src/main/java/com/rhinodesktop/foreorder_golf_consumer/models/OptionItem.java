package com.rhinodesktop.foreorder_golf_consumer.models;

import com.google.gson.annotations.SerializedName;
import com.rhinoactive.foreorder_library_android.utils.Constants;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import lombok.Getter;

/**
 * Created by Hunter Andrin on 2017-04-11.
 */

public class OptionItem extends RealmObject {

    @PrimaryKey
    @SerializedName(Constants.OPTION_ITEM_ID)
    @Getter
    private Integer optionItemId;
    @Getter
    private Float price;
    @Getter
    private Boolean available;
    @Getter
    private Boolean valid;
    @Getter
    private String name;
    @Getter
    private String desc;
    @SerializedName(Constants.MODIFIED_AT)
    @Getter
    private Date modifiedAt;
    @SerializedName(Constants.ADDED_TAX_RATE)
    @Getter
    private Double addedTaxRate;
}
