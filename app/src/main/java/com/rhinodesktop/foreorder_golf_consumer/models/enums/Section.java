package com.rhinodesktop.foreorder_golf_consumer.models.enums;

import lombok.Getter;

/**
 * Created by rhinodesktop on 2017-03-20.
 */

public enum Section {

    MOST_POPULAR("Most Popular"),
    SNACKS("Snacks"),
    ALCOHOLIC_BEVERAGES("Alcoholic Beverages"),
    OTHER_BEVERAGES("Other Beverages"),
    FOOD("Food");

    @Getter
    private String name;

    Section(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
