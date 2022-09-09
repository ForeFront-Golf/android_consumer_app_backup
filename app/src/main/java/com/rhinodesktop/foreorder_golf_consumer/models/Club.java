package com.rhinodesktop.foreorder_golf_consumer.models;

import com.google.gson.annotations.SerializedName;
import com.rhinoactive.foreorder_library_android.utils.Constants;

import java.util.Comparator;
import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by rhinodesktop on 2017-03-16.
 */

public class Club extends RealmObject {

    @PrimaryKey
    @SerializedName(Constants.CLUB_ID)
    @Getter
    private Integer clubId;
    @Getter
    private String name;
    @SerializedName(Constants.PHONE_NUMBER)
    @Getter
    private String phoneNumber;
    @Getter
    private Boolean valid;
    @Getter
    private String address;
    @Getter
    private Double lat;
    @Getter
    private Double lon;
    @SerializedName("photo_url")
    @Getter
    private String photoUrl;
    @SerializedName("photo_url_thumb")
    @Getter
    private String photoUrlThumb;
    @SerializedName(Constants.MODIFIED_AT)
    @Getter
    private Date modifiedAt;
    @SerializedName(Constants.TAX_RATE)
    @Getter
    private Double taxRate;
    @SerializedName(Constants.SHOW_TAX)
    @Getter
    private Boolean showTax;
    @Ignore
    @Getter
    @Setter
    private Double dist;
    @Getter
    @Setter
    private RealmList<Menu> menus = new RealmList<>();

    public Club() {
        // Required public empty constructor for Realm
    }

    public void removeOldMenuIfAlreadyAdded(Menu newMenu) {
        Menu menuToRemove = null;
        for (Menu oldMenu : menus) {
            if (newMenu.getMenuId().equals(oldMenu.getMenuId())) {
                menuToRemove = oldMenu;
                break;
            }
        }
        if (menuToRemove != null) {
            menus.remove(menuToRemove);
        }
    }

    public static Comparator<Club> COMPARE_BY_DISTANCE = new Comparator<Club>() {
        public int compare(Club one, Club two) {
            if(one.dist.equals(two.dist))
                return 0;
            return one.dist < two.dist ? -1 : 1;
        }
    };
}
