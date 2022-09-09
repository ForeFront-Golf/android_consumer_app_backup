package com.rhinodesktop.foreorder_golf_consumer.parsers;

import android.content.Context;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.rhinoactive.foreorder_library_android.utils.Constants;
import com.rhinoactive.jsonparsercallback.JsonArrayParser;
import com.rhinodesktop.foreorder_golf_consumer.events.MenusUpdatedEvent;
import com.rhinodesktop.foreorder_golf_consumer.models.Club;
import com.rhinodesktop.foreorder_golf_consumer.models.Menu;
import com.rhinodesktop.foreorder_golf_consumer.models.MenuItem;
import com.rhinodesktop.foreorder_golf_consumer.models.MenuItemsByItemType;
import com.rhinodesktop.foreorder_golf_consumer.models.OptionGroup;
import com.rhinodesktop.foreorder_golf_consumer.models.OptionItem;
import com.rhinodesktop.foreorder_golf_consumer.utils.ForeOrderSharedPrefUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.realm.Realm;
import timber.log.Timber;

/**
 * Created by Hunter Andrin on 2017-04-07.
 */

public class MenusParser extends JsonArrayParser<Menu> {

    private Context context;

    public MenusParser(Context context) {
        this.context = context;
    }

    @Override
    public void handleError(Exception ex) {
        Timber.e("Getting the menus failed with the following exception: %s", ex.toString());
        EventBus.getDefault().post(new MenusUpdatedEvent(true));
    }

    @Override
    protected String getJsonKey() {
        return Constants.MENU_TABLE;
    }

    @Override
    protected Menu parseSingleElement(JsonElement menuElement, GsonBuilder builder) {
        Menu menu = builder.create().fromJson(menuElement, Menu.class);
        JsonArray menuItemsJson = menuElement.getAsJsonObject().getAsJsonArray(Constants.MENU_ITEMS);
        for (JsonElement menuItemElement : menuItemsJson) {
            MenuItem menuItem = builder.create().fromJson(menuItemElement, MenuItem.class);
            addMenuItemIfValid(menu, menuItem);
        }
        sortMenuItemsAlphabetically(menu);
//        addOrUpdateMenusInCurrentClub(menu);
        return menu;
    }

    @Override
    protected void postSuccessfulParsingLogic(final List<Menu> parsedObject) {
        try (Realm realm = Realm.getDefaultInstance()) {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.insertOrUpdate(parsedObject);
                }
            });
        }
        EventBus.getDefault().post(new MenusUpdatedEvent(false));
    }

    private void addMenuItemIfValid(Menu menu, MenuItem menuItem) {
        if (menuItem.getValid() && menuItem.getAvailable()) {
            removeInvalidOptionItems(menuItem);
            menu.addMenuItemToAppropriateList(menuItem);
        }
    }

    private void removeInvalidOptionItems(MenuItem menuItem) {
        List<OptionGroup> invalidOptionGroups = new ArrayList<>();
        for (OptionGroup optionGroup : menuItem.getOptionGroups()) {
            if (!optionGroup.getValid()) {
                invalidOptionGroups.add(optionGroup);
            } else {
                List<OptionItem> invalidOptionItems = new ArrayList<>();
                for (OptionItem optionItem : optionGroup.getOptionItems()) {
                    if (!optionItem.getValid() || !optionItem.getAvailable()) {
                        invalidOptionItems.add(optionItem);
                    }
                }
                optionGroup.getOptionItems().removeAll(invalidOptionItems);
            }
        }
        menuItem.getOptionGroups().removeAll(invalidOptionGroups);
    }

    private void sortMenuItemsAlphabetically(Menu menu) {
        for (MenuItemsByItemType menuItemsByItemType : menu.getMenuItemsByItemType()) {
            Collections.sort(menuItemsByItemType.getMenuItems(), MenuItem.COMPARE_BY_NAME);
        }
    }

    private void addOrUpdateMenusInCurrentClub(final Menu newMenu) {
        try (Realm realm = Realm.getDefaultInstance()) {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    Club currentClub = realm.where(Club.class).equalTo("clubId", ForeOrderSharedPrefUtils.getCurrentClubId(context)).findFirst();
                    currentClub.removeOldMenuIfAlreadyAdded(newMenu);

                    if (newMenu.getValid()) {
                        currentClub.getMenus().add(newMenu);
                    }
                }
            });
        }
    }
}
