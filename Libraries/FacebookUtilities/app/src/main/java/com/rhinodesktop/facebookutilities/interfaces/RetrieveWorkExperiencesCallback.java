package com.rhinodesktop.facebookutilities.interfaces;

import com.rhinodesktop.facebookutilities.models.WorkExperience;

import java.util.List;

/**
 * Created by rhinodesktop on 2017-02-06.
 */

public interface RetrieveWorkExperiencesCallback {

    void handleWorkExperiencesRetrieved(List<WorkExperience> jobs);
}
