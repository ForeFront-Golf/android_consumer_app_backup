package com.rhinodesktop.foreorder_golf_consumer.models.enums;

import lombok.Getter;

/**
 * Created by rhinodesktop on 2017-03-16.
 */

public enum PhotoUploadOption {

    FILE("From File"),
    PHOTO("Take Photo");

    @Getter
    private String title;

    PhotoUploadOption(String title) {
        this.title = title;
    }
}