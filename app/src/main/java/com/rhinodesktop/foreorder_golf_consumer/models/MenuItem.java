package com.rhinodesktop.foreorder_golf_consumer.models;

import com.google.gson.annotations.SerializedName;
import com.rhinoactive.foreorder_library_android.utils.Constants;

import java.util.Comparator;
import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import lombok.Getter;

/**
 * Created by Hunter Andrin on 2017-04-07.
 */

public class MenuItem extends RealmObject {

    @SerializedName(Constants.MENU_ITEM_ID)
    @PrimaryKey
    @Getter
    private Integer menuItemId;
    @Getter
    private Boolean available;
    @Getter
    private Boolean valid;
    @Getter
    private String desc;
    @SerializedName(Constants.MENU_ID)
    @Getter
    private Integer menuId;
    @Getter
    private String name;
    @Getter
    private Float price;
    @Getter
    private Integer stock;
    @SerializedName("photo_url")
    @Getter
    private String photoUrl;
    @SerializedName("photo_url_thumb")
    @Getter
    private String photoUrlThumb;
    @SerializedName(Constants.ADDED_TAX_RATE)
    @Getter
    private Double addedTaxRate;
    @SerializedName(Constants.ITEM_TYPES)
    @Getter
    private RealmList<ItemType> itemTypes = new RealmList<>();
    @SerializedName(Constants.OPTION_GROUPS)
    @Getter
    private RealmList<OptionGroup> optionGroups = new RealmList<>();
    @SerializedName(Constants.MODIFIED_AT)
    @Getter
    private Date modifiedAt;

    public MenuItem() {
        // Required public empty constructor for Realm
    }

    public static Comparator<MenuItem> COMPARE_BY_NAME = new Comparator<MenuItem>() {
        public int compare(MenuItem one, MenuItem two) {
            return one.name.compareTo(two.name);
        }
    };
}
