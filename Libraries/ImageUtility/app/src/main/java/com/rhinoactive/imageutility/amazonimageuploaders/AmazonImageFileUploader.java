package com.rhinoactive.imageutility.amazonimageuploaders;

import java.io.File;
import java.io.IOException;

/**
 * Created by hunter on 2018-02-23.
 */

public class AmazonImageFileUploader extends AmazonImageUploader {

    private File file;

    public AmazonImageFileUploader(File file) {
        this.file = file;
    }

    @Override
    protected File getFile(String s3Path) throws IOException {
        return file;
    }
}
