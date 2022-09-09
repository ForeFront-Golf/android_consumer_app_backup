package com.rhinodesktop.foreorder_golf_consumer.models;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

/**
 * Created by Hunter Andrin on 2017-04-13.
 */

public class OptionGroupWithItems {

    @Getter
    private OptionGroup optionGroup;
    @Getter
    private List<OptionItem> optionItems = new ArrayList<>();

    public OptionGroupWithItems(OptionGroup optionGroup) {
        this.optionGroup = optionGroup;
    }
}
