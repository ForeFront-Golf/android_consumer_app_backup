package com.rhinodesktop.foreorder_golf_consumer.models;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

/**
 * Created by rhinodesktop on 2017-03-28.
 */

public class OrderItem {

    @Getter
    private List<MenuItem> menuItems = new ArrayList<>();
    @Getter
    private List<OptionGroupWithItems> optionGroupWithItemsList = new ArrayList<>();
    @Getter
    private Float price;
    @Getter
    private String specialInstructions;

    public OrderItem(List<MenuItem> menuItems, List<OptionGroupWithItems> optionGroupWithItemsList, String specialInstructions) {
        this.menuItems = menuItems;
        this.optionGroupWithItemsList = optionGroupWithItemsList;
        this.specialInstructions = specialInstructions;
        price = setPrice();
    }

    public String getItemName() {
        return menuItems.get(0).getName();
    }

    public int numOfItems() {
        return menuItems.size();
    }

    public boolean hasSpecialInstructions() {
        return specialInstructions != null && !specialInstructions.equals("");
    }

    private float setPrice() {
        float priceOfOneItem = menuItems.get(0).getPrice();
        float priceOfOptionItems = calculateCostOfOptionItems();
        return (priceOfOneItem + priceOfOptionItems) * menuItems.size();
    }

    private float calculateCostOfOptionItems() {
        float costOfOptionItems = 0;
        for (OptionGroupWithItems optionGroupWithItems : optionGroupWithItemsList) {
            for (OptionItem optionItem : optionGroupWithItems.getOptionItems()) {
                costOfOptionItems = costOfOptionItems + optionItem.getPrice();
            }
        }
        return costOfOptionItems;
    }
}
