package com.rhinodesktop.foreorder_golf_consumer.adapters;

import android.app.Activity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.rhinodesktop.activityanimatorutility.activityutils.externalactivities.ExternalActivity;
import com.rhinodesktop.activityanimatorutility.activityutils.externalactivities.activitieswithoutresult.ExternalDirectionsActivity;
import com.rhinodesktop.activityanimatorutility.activityutils.externalactivities.activitieswithoutresult.ExternalPhoneActivity;
import com.rhinodesktop.foreorder_golf_consumer.R;
import com.rhinodesktop.foreorder_golf_consumer.models.Club;
import com.rhinodesktop.foreorder_golf_consumer.utils.ForeOrderResourceUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by rhinodesktop on 2017-03-16.
 */

public class CourseAdapter extends RecyclerView.Adapter<CourseViewHolder> {

    private Activity activity;
    private List<Club> clubs;

    public CourseAdapter(Activity activity, List<Club> clubs) {
        this.activity = activity;
        this.clubs = createListOfOnlyValidClubs(clubs);
    }

    public void setClubs(List<Club> clubs) {
        this.clubs = createListOfOnlyValidClubs(clubs);
    }

    private List<Club> createListOfOnlyValidClubs(List<Club> clubs) {
        List<Club> validClubs = new ArrayList<>();
        for (Club club : clubs) {
            if (club.getValid()) {
                validClubs.add(club);
            }
        }
        return validClubs;
    }

    @Override
    public CourseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.course_item_view, parent, false);
        return new CourseViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CourseViewHolder holder, int position) {
        Club club = clubs.get(position);
        holder.getCourseNameTextview().setText(club.getName());
        if (club.getDist() == null) {
            holder.getDistanceAwayTextview().setText("");
        } else {
            String km = ForeOrderResourceUtils.getInstance().strRes(R.string.km);
            holder.getDistanceAwayTextview().setText(String.format(Locale.ENGLISH, "%.1f %s", club.getDist(), km));
        }
        setClickListeners(club, holder);
    }

    @Override
    public int getItemCount() {
        return clubs.size();
    }

    private void setClickListeners(Club club, CourseViewHolder holder) {
        setCallClickListener(club, holder);
        setDirectionsClickListener(club, holder);
    }

    private void setCallClickListener(final Club club, final CourseViewHolder holder) {
        holder.getCallButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ExternalActivity phoneActivity = new ExternalPhoneActivity((AppCompatActivity) activity, club.getPhoneNumber());
                phoneActivity.start();
            }
        });
    }

    private void setDirectionsClickListener(final Club club, CourseViewHolder holder) {
        holder.getDirectionsButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ExternalActivity directionsActivity = new ExternalDirectionsActivity((AppCompatActivity) activity, club.getAddress());
                directionsActivity.start();
            }
        });
    }
}
