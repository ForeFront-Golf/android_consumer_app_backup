package com.rhinodesktop.facebookutilities.models;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by rhinodesktop on 2017-01-19.
 */

public class FbPhotosAndPagers {

    public FbPhotosAndPagers() {
        fbPhotos = new ArrayList<>();
        hasMoreTaggedImages = true;
        hasMoreUploadedImages = true;
    }

    @Getter
    @Setter
    private List<FbPhoto> fbPhotos;
    @Getter
    @Setter
    private FbPager fbUploadedPager;
    @Getter
    @Setter
    private FbPager fbTaggedPager;
    @Getter
    @Setter
    private boolean hasMoreUploadedImages;
    @Getter
    @Setter
    private boolean hasMoreTaggedImages;
}
