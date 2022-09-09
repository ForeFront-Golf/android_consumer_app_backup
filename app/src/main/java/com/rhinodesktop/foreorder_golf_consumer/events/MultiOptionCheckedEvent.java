package com.rhinodesktop.foreorder_golf_consumer.events;

import com.rhinodesktop.foreorder_golf_consumer.fragments.ChoiceFragment;
import com.rhinodesktop.foreorder_golf_consumer.models.OptionItem;

import lombok.Getter;

/**
 * Created by Hunter Andrin on 2017-04-12.
 */

public class MultiOptionCheckedEvent extends OptionCheckedEvent {

    @Getter
    private boolean isChecked;

    public MultiOptionCheckedEvent(ChoiceFragment choiceFragment, OptionItem optionItem, boolean isChecked) {
        super(choiceFragment, optionItem);
        this.isChecked = isChecked;
    }
}
