package com.rhinodesktop.foreorder_golf_consumer.adapters;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.rhinodesktop.foreorder_golf_consumer.R;

import lombok.Getter;

/**
 * Created by rhinodesktop on 2017-03-16.
 */

public class CourseViewHolder extends RecyclerView.ViewHolder {

    @Getter
    private TextView courseNameTextview;
    @Getter
    private TextView distanceAwayTextview;
    @Getter
    private ImageButton callButton;
    @Getter
    private ImageButton directionsButton;

    public CourseViewHolder(View view) {
        super(view);
        courseNameTextview = (TextView) itemView.findViewById(R.id.textview_course_name);
        distanceAwayTextview = (TextView) itemView.findViewById(R.id.textview_distance_from_you);
        callButton = (ImageButton) itemView.findViewById(R.id.button_call);
        directionsButton = (ImageButton) itemView.findViewById(R.id.button_directions);
    }
}
