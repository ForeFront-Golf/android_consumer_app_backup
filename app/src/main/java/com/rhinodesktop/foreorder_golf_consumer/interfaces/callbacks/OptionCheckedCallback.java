package com.rhinodesktop.foreorder_golf_consumer.interfaces.callbacks;

import com.rhinodesktop.foreorder_golf_consumer.models.OptionItem;

/**
 * Created by Hunter Andrin on 2017-04-12.
 */

public interface OptionCheckedCallback {

    void optionCheckedCallback(OptionItem optionItem, boolean checked);
}
