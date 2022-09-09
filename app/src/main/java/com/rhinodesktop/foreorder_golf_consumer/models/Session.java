package com.rhinodesktop.foreorder_golf_consumer.models;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import lombok.Getter;

/**
 * Created by rhinodesktop on 2017-03-14.
 */

public class Session extends RealmObject {

    @PrimaryKey
    @SerializedName("session_id")
    @Getter
    private String sessionId;
    @SerializedName("user_id")
    @Getter
    private Integer userId;
    private Boolean valid;
    @SerializedName("created_at")
    private Date createdAt;
    @SerializedName("modified_at")
    private Date modifiedAt;

}
