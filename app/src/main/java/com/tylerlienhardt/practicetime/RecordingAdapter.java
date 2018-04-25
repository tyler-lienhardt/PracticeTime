package com.tylerlienhardt.practicetime;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by Tyler on 4/20/2018.
 */

public class RecordingAdapter extends RecyclerView.Adapter<RecordingAdapter.ViewHolder>{

    private ArrayList<Recording> recordingList;

    public RecordingAdapter (ArrayList<Recording> recordingList) {
        this.recordingList = recordingList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ViewHolder (View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
        }

        public void bind (Recording recording) {

        }

        @Override
        public void onClick(View v) {

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
        Recording recording = recordingList.get(position);

        holder.bind(recording);
    }

    @Override
    public int getItemCount() {
        return recordingList.size();
    }
}
