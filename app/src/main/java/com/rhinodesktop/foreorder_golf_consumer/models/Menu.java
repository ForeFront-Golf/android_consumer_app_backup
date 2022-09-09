package com.rhinodesktop.foreorder_golf_consumer.models;

import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;
import com.rhinoactive.foreorder_library_android.utils.Constants;

import java.util.Date;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import lombok.Getter;

/**
 * Created by Hunter Andrin on 2017-04-07.
 */

public class Menu extends RealmObject {

    @PrimaryKey
    @SerializedName(Constants.MENU_ID)
    @Getter
    private Integer menuId;
    @Getter
    private String name;
    @Getter
    private String desc;
    @SerializedName(Constants.CLUB_ID)
    @Getter
    private Integer clubId;
    @Getter
    private Boolean valid;
    @Getter
    private RealmList<MenuItemsByItemType> menuItemsByItemType = new RealmList<>();
    @SerializedName(Constants.MODIFIED_AT)
    @Getter
    private Date modifiedAt;

    public void addMenuItemToAppropriateList(MenuItem menuItem) {
        List<ItemType> itemTypes = menuItem.getItemTypes();
        for (ItemType itemType : itemTypes) {
            MenuItemsByItemType existingMenuItemsByItemType = getExistingMenuItemsByItemType(itemType);
            if (existingMenuItemsByItemType == null) {
                MenuItemsByItemType newMenuItemsByItemType = new MenuItemsByItemType(menuId, itemType);
                newMenuItemsByItemType.getMenuItems().add(menuItem);
                menuItemsByItemType.add(newMenuItemsByItemType);
            } else {
                existingMenuItemsByItemType.getMenuItems().add(menuItem);
            }
        }
    }

    @Nullable
    private MenuItemsByItemType getExistingMenuItemsByItemType(ItemType itemType) {
        MenuItemsByItemType sameMenuItemsByItemType = null;
        String typeName = itemType.getName().toLowerCase();
        for (MenuItemsByItemType menuItemsByItemType : this.getMenuItemsByItemType()) {
            if (menuItemsByItemType.getItemType().getName().toLowerCase().equals(typeName)) {
                sameMenuItemsByItemType = menuItemsByItemType;
                break;
            }
        }
        return sameMenuItemsByItemType;
    }
}
