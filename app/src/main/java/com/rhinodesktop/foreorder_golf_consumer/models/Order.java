package com.rhinodesktop.foreorder_golf_consumer.models;

import com.rhinoactive.foreorder_library_android.utils.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by rhinodesktop on 2017-03-27.
 */

public class Order {

    @Getter
    private float priceTotal = 0;
    @Getter
    @Setter
    private float priceTotalTax = 0;
    @Getter
    private List<OrderItem> orderItems = new ArrayList<>();

    public void addMenuItems(List<MenuItem> menuItems, List<OptionGroupWithItems> optionGroupWithItemsList, String specialInstructions) {
        OrderItem orderItem = new OrderItem(menuItems, optionGroupWithItemsList, specialInstructions);
        orderItems.add(orderItem);
        priceTotal = priceTotal + orderItem.getPrice();
    }

    public boolean removeOrderItem(OrderItem orderItem) {
        boolean removed = orderItems.remove(orderItem);
        if (removed) {
            priceTotal = priceTotal - orderItem.getPrice();
        }
        return removed;
    }

    public int getTotalNumberOfItemsInOrder() {
        int numOfItemsInOrder = 0;
        for (OrderItem orderItem : orderItems) {
            numOfItemsInOrder = numOfItemsInOrder + orderItem.numOfItems();
        }
        return numOfItemsInOrder;
    }

    public void clearOrder() {
        priceTotal = 0;
        orderItems = new ArrayList<>();
    }

    public List<Map<String, Object>> createOrderMapJson() {
        List<Map<String, Object>> orderData = new ArrayList<>();
        for (OrderItem orderItem : orderItems) {
            Map<String, Object> params = new HashMap<>();
            int menuItemId = orderItem.getMenuItems().get(0).getMenuItemId();
            params.put(Constants.MENU_ITEM_ID, menuItemId);
            params.put(Constants.ORDER_OPTION_IDS, createOptionList(orderItem));
            params.put(Constants.QUANTITY, orderItem.numOfItems());
            params.put(Constants.SPECIAL_REQUEST, orderItem.getSpecialInstructions());
            orderData.add(params);
        }
        return orderData;
    }

    private List<Integer> createOptionList(OrderItem orderItem) {
        List<Integer> optionIds = new ArrayList<>();
        for (OptionGroupWithItems optionGroupWithItems : orderItem.getOptionGroupWithItemsList()) {
            optionIds.addAll(createListOfOptionItemIds(optionGroupWithItems));
        }
        return optionIds;
    }

    private List<Integer> createListOfOptionItemIds(OptionGroupWithItems optionGroupWithItems) {
        List<Integer> optionItemIds = new ArrayList<>();
        for (OptionItem optionItem : optionGroupWithItems.getOptionItems()) {
            optionItemIds.add(optionItem.getOptionItemId());
        }
        return optionItemIds;
    }
}
