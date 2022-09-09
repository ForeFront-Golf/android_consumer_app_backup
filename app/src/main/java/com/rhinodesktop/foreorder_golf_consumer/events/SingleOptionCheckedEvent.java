package com.rhinodesktop.foreorder_golf_consumer.events;

import com.rhinodesktop.foreorder_golf_consumer.fragments.ChoiceFragment;
import com.rhinodesktop.foreorder_golf_consumer.models.OptionItem;

/**
 * Created by Hunter Andrin on 2017-04-12.
 */

public class SingleOptionCheckedEvent extends OptionCheckedEvent {

    public SingleOptionCheckedEvent(ChoiceFragment choiceFragment, OptionItem optionItem) {
        super(choiceFragment, optionItem);
    }
}
