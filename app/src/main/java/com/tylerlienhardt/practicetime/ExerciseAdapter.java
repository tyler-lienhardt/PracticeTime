package com.tylerlienhardt.practicetime;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.ArrayList;

/**
 * Created by Tyler on 4/1/2018.
 */

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ViewHolder> {

    ArrayList<Exercise> exerciseList = new ArrayList<Exercise>();
    Context context;
    private int position = 0;

    public ExerciseAdapter (Context context, ArrayList<Exercise> exerciseList) {
        this.exerciseList = exerciseList;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView iconView;
        private TextView nameView;
        private TextView timeView;
        private TextView tempoView;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            iconView = itemView.findViewById(R.id.item_icon);
            nameView = itemView.findViewById(R.id.item_name);
            timeView = itemView.findViewById(R.id.item_time);
            tempoView = itemView.findViewById(R.id.item_tempo);
        }

        public void bind(Exercise exercise) {
            nameView.setText(exercise.getName());
            timeView.setText(String.valueOf(Timer.timeToString(exercise.getStartTime())));
            tempoView.setText(String.valueOf(exercise.getTempo()));

        }

        @Override
        public void onClick(View v) {
            MainActivity mainActivity = (MainActivity)context;
            mainActivity.switchExerciseForTimer(position, getAdapterPosition());

            position = getAdapterPosition();
            Exercise exercise = exerciseList.get(position);

            //setting the main timer display
            TextView timerDisplay = (TextView) ((Activity) context).findViewById(R.id.timer_display);
            timerDisplay.setText(Timer.timeToString(exercise.getRemainingTime()));

            //setting the main metro display
            TextView tempoDisplay = (TextView) ((Activity) context).findViewById(R.id.tempo_display);
            tempoDisplay.setText(String.valueOf(exercise.getTempo()));
        }
    }

    @Override
    public ExerciseAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View cellView = LayoutInflater.from(parent.getContext()).inflate(R.layout.exercise_item, parent, false);

        ViewHolder holder = new ViewHolder(cellView);
        return holder;
    }

    @Override
    public void onBindViewHolder(ExerciseAdapter.ViewHolder holder, int position) {
        Exercise exercise = exerciseList.get(position);

        holder.bind(exercise);
    }

    @Override
    public int getItemCount() {
        return exerciseList.size();
    }

    public int getPosition(){
        return position;
    }

}
