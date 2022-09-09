package com.rhinodesktop.facebookutilities.models;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;

/**
 * Created by rhinodesktop on 2017-01-19.
 */

public class Album {
    @SerializedName("created_time")
    private String createdTime;
    @Getter
    private String name;
    @Getter
    private String id;
}