package com.rhinodesktop.foreorder_golf_consumer.adapters;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.rhinodesktop.foreorder_golf_consumer.R;
import com.rhinodesktop.foreorder_golf_consumer.managers.apirequestmanagers.MembershipApiManager;
import com.rhinodesktop.foreorder_golf_consumer.models.Membership;
import com.rhinodesktop.foreorder_golf_consumer.utils.ForeOrderDialogUtils;

import java.util.List;

import lombok.NonNull;

/**
 * Created by sungwook on 2018-04-05.
 */

public class MembershipListAdapter extends RecyclerView.Adapter<MembershipListAdapter.MembershipListViewHolder> {

    private List<Membership> clubs;
    private Context context;
    private int userId;

    public MembershipListAdapter(Context context, List<Membership> clubs, int userId) {
        this.context = context;
        this.clubs = clubs;
        this.userId = userId;
    }

    @NonNull
    @Override
    public MembershipListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.course_list_item, parent, false);
        return new MembershipListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MembershipListViewHolder holder, final int position) {
        holder.textCourseName.setText(clubs.get(position).getClub().getName());
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runConfirmationPopup(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return clubs.size();
    }

    private void runConfirmationPopup(final int position) {
        ForeOrderDialogUtils.getInstance()
                .buildDialog(context, context.getString(R.string.dialog_remove_membership_title))
                .content(R.string.dialog_remove_membership_content)
                .negativeText(R.string.cancel)
                .positiveText(R.string.remove)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        removeMembership(position);
                    }
                })
                .show();
    }

    private void removeMembership(final int position) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                MembershipApiManager.removeMembership(clubs.get(position).getMembershipId(), userId);
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

    class MembershipListViewHolder extends RecyclerView.ViewHolder {
        private TextView textCourseName;
        private ImageView deleteButton;

        private MembershipListViewHolder(View itemView) {
            super(itemView);
            textCourseName = itemView.findViewById(R.id.text_course_name);
            deleteButton = itemView.findViewById(R.id.image_delete_membership);
        }
    }
}
