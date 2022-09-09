package com.rhinodesktop.foreorder_golf_consumer.models;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import lombok.Getter;

/**
 * Created by rhinodesktop on 2017-03-24.
 */

public class MenuItemsByItemType extends RealmObject {

    @PrimaryKey
    @Getter
    private String menuItemByItemTypeId;

    @Getter
    private ItemType itemType;

    @Getter
    RealmList<MenuItem> menuItems = new RealmList<>();

    public MenuItemsByItemType() {}

    public MenuItemsByItemType(Integer menuId, ItemType itemType) {
        this.itemType = itemType;
        this.menuItemByItemTypeId = menuId + "_" + itemType.getItemTypeId();
    }
}
