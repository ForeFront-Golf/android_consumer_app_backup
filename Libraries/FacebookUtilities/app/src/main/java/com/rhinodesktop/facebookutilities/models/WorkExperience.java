package com.rhinodesktop.facebookutilities.models;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;

/**
 * Created by rhinodesktop on 2017-02-06.
 */

public class WorkExperience {

    private String id;
    @SerializedName("start_date")
    private String startDate;
    private Page employer;
    private Page location;
    @Getter
    private Page position;

}
