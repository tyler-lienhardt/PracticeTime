package com.tylerlienhardt.practicetime;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Tyler on 4/20/2018.
 */

public class RecordingAdapter extends RecyclerView.Adapter<RecordingAdapter.ViewHolder>{

    private ArrayList<Recording> recordingList;
    private int position = RecyclerView.NO_POSITION;
    private int selectedPos = RecyclerView.NO_POSITION;

    public RecordingAdapter (ArrayList<Recording> recordingList) {
        this.recordingList = recordingList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView nameView;
        private TextView dateView;

        public ViewHolder (View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            nameView = itemView.findViewById(R.id.recording_item_name);
            dateView = itemView.findViewById(R.id.recording_item_date);
        }

        public void bind (Recording recording) {
            nameView.setText(recording.getName());
            dateView.setText(recording.getDateString());
        }

        @Override
        public void onClick(View v) {
            //notifying to change view state to 'selected', changing colors
            notifyItemChanged(selectedPos);
            selectedPos = getLayoutPosition();
            notifyItemChanged(selectedPos);
        }
    }

    @Override
    public RecordingAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View cellView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recording_item, parent, false);

        ViewHolder holder = new ViewHolder(cellView);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecordingAdapter.ViewHolder holder, int position) {
        holder.itemView.setSelected(selectedPos == position);

        Recording recording = recordingList.get(position);

        holder.bind(recording);
    }

    @Override
    public int getItemCount() {
        return recordingList.size();
    }
}
