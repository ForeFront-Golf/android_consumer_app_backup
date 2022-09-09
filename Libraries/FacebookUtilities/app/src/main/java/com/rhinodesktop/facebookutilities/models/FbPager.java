package com.rhinodesktop.facebookutilities.models;

import lombok.Getter;

/**
 * Created by rhinodesktop on 2017-01-19.
 */

public class FbPager {

    // Will be null if there are no more pages
    @Getter
    private String next;
    @Getter
    private Cursors cursors;
}
