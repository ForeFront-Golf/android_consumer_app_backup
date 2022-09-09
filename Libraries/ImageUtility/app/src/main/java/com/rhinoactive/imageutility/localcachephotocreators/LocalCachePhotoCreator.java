package com.rhinoactive.imageutility.localcachephotocreators;

import java.io.File;
import java.io.IOException;

/**
 * Created by rhinodesktop on 2017-02-08.
 */

public abstract class LocalCachePhotoCreator {

    public abstract File createImageFileInLocalCache(String s3Path) throws IOException;
}
