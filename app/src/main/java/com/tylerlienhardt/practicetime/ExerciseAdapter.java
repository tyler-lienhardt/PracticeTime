package com.tylerlienhardt.practicetime;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.ArrayList;

/**
 * Created by Tyler on 4/1/2018.
 */

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ViewHolder> implements
        DragHelper.ActionCompletionContract {

    private ArrayList<Exercise> exerciseList = new ArrayList<Exercise>();
    private Context context;
    private ItemTouchHelper touchHelper;
    private int position = 0;
    private int selectedPos = 0;

    public ExerciseAdapter (Context context, ArrayList<Exercise> exerciseList) {
        this.exerciseList = exerciseList;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        private ImageView dragHandle;
        private TextView nameView;
        private TextView timeView;
        private TextView tempoView;
        private ImageButton doublePlayButton;
        private ImageButton doublePauseButton;

        public ViewHolder(View itemView) {
            super(itemView);

            dragHandle = itemView.findViewById(R.id.item_drag_handle);
            nameView = itemView.findViewById(R.id.item_name);
            timeView = itemView.findViewById(R.id.item_time);
            tempoView = itemView.findViewById(R.id.item_tempo);
            doublePlayButton = itemView.findViewById(R.id.double_play_button);
            doublePauseButton = itemView.findViewById(R.id.double_pause_button);

            doublePlayButton.setOnClickListener(this);
            doublePauseButton.setOnClickListener(this);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        public void bind(Exercise exercise) {
            nameView.setText(exercise.getName());
            timeView.setText(String.valueOf(Timer.timeToString(exercise.getStartTime())));
            tempoView.setText(String.valueOf(exercise.getTempo()));
        }

        @Override
        public void onClick(View v) {

            //notifying to change view state to 'selected', changing colors
            notifyItemChanged(selectedPos);
            selectedPos = getLayoutPosition();
            notifyItemChanged(selectedPos);

            Exercise exercise = exerciseList.get(getAdapterPosition());

            ((MainActivity)context).setAllToExercise(exercise);

            position = getAdapterPosition(); //updates position for the adapter

            if (v.getId() == R.id.double_play_button) {
                ((MainActivity)context).doublePlayAction();
            }

            if (v.getId() == R.id.double_pause_button) {
                ((MainActivity)context).doublePauseAction();
            }
        }

        //long click on item opens edit-item activity
        @Override
        public boolean onLongClick(View v) {
            onClick(v);

            Intent intent = new Intent(v.getContext(), EditActivity.class);

            Exercise exercise = exerciseList.get(position);

            intent.putExtra("name", exercise.getName());
            intent.putExtra("startTime", exercise.getStartTime());
            intent.putExtra("tempo", exercise.getTempo());
            intent.putExtra("measure", exercise.getMeasure());

            ((Activity) context).startActivityForResult(intent, MainActivity.EDIT_EXERCISE_REQUEST_CODE);

            return false;
        }
    }

    @Override
    public ExerciseAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View cellView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.exercise_item, parent, false);

        ViewHolder holder = new ViewHolder(cellView);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ExerciseAdapter.ViewHolder holder, int position) {
        holder.itemView.setSelected(selectedPos == position);

        Exercise exercise = exerciseList.get(position);

        //drag and drop reordering listener
        holder.dragHandle.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch (View view, MotionEvent event) {
                if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
                    touchHelper.startDrag(holder);
                }
                //once a draghandle is clicked, the item being dragged is selected
                //holder.onClick(view);
                return false;
            }

        });

        holder.bind(exercise);
    }

    @Override
    public int getItemCount() {
        return exerciseList.size();
    }

    public int getPosition(){
        return position;
    }


    public void onViewMoved(int oldPosition, int newPosition) {
        int currentPosition = position;
        Exercise currentExercise = exerciseList.get(currentPosition);

        Exercise targetExercise = exerciseList.get(oldPosition);
        exerciseList.remove(targetExercise);
        exerciseList.add(newPosition, targetExercise);
        notifyItemMoved(oldPosition, newPosition);

        position = exerciseList.indexOf(currentExercise);
        selectedPos = position;
    }

    public void setTouchHelper(ItemTouchHelper touchHelper){
        this.touchHelper = touchHelper;
    }

}
