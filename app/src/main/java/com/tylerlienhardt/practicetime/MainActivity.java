package com.tylerlienhardt.practicetime;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ArrayList<Exercise> exerciseList = new ArrayList<Exercise>();
    private RecyclerView recyclerView;
    private ExerciseAdapter recyclerAdapter;
    private RecyclerView.LayoutManager layoutManager;

    TextView tempoDisplay;
    TextView timerDisplay;

    Exercise exercise;
    Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        createSampleExercises();

        // RECYCLER VIEW
        recyclerView = (RecyclerView) findViewById(R.id.exercise_list);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        recyclerAdapter = new ExerciseAdapter(this, exerciseList);
        recyclerView.setAdapter(recyclerAdapter);

        //For use with timer display and listener
        exercise = exerciseList.get(recyclerAdapter.getPosition());
        timer = new Timer(exercise.getStartTime(), 1000, this);

        // DRAG REORDORING
        DragHelper dragHelper = new DragHelper(recyclerAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(dragHelper);
        recyclerAdapter.setTouchHelper(touchHelper);
        touchHelper.attachToRecyclerView(recyclerView);


        //DISPLAYS
        //load data from first exercise by default
        timerDisplay = findViewById(R.id.timer_display);
        timerDisplay.setText(timer.timeToString(exercise.getStartTime()));

        tempoDisplay = findViewById(R.id.tempo_display);
        tempoDisplay.setText(String.valueOf(exercise.getTempo()));

        // BUTTON LISTENERS
        // timer buttons
        ImageButton timerPauseButton = (ImageButton)findViewById(R.id.timer_play_button);
        timerPauseButton.setOnClickListener(this);

        ImageButton timerResetButton = (ImageButton)findViewById(R.id.timer_reset_button);
        timerResetButton.setOnClickListener(this);
        timerResetButton.setOnLongClickListener(new View.OnLongClickListener() {

            // Long press on reset button resets timer to original time
            @Override
            public boolean onLongClick(View v) {
                timer.cancelTimer();
                exercise = exerciseList.get(recyclerAdapter.getPosition());
                timer = new Timer(exercise.getStartTime(), 1000, MainActivity.this);
                timerDisplay.setText(timer.timeToString(timer.getStartTime()));
                return true;
            }
        });

        Button timerSetButton = (Button)findViewById(R.id.timer_set_button);
        timerSetButton.setOnClickListener(this);

        // metronome buttons
        ImageButton plus5Button = (ImageButton)findViewById(R.id.plus5_button);
        plus5Button.setOnClickListener(this);

        ImageButton plus1Button = (ImageButton)findViewById(R.id.plus1_button);
        plus1Button.setOnClickListener(this);

        ImageButton minus1Button = (ImageButton)findViewById(R.id.minus1_button);
        minus1Button.setOnClickListener(this);

        ImageButton minus5Button = (ImageButton)findViewById(R.id.minus5_button);
        minus5Button.setOnClickListener(this);

        Button metroSoundButton = (Button)findViewById(R.id.metro_sound_button);
        metroSoundButton.setOnClickListener(this);

        //creating the time signature spinner
        Spinner spinner = (Spinner) findViewById(R.id.time_sig_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.time_sigs, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(spinnerAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public void onClick(View v) {

        int position = recyclerAdapter.getPosition();
        Exercise exercise = exerciseList.get(position);

        switch (v.getId()) {
            case R.id.timer_play_button :
                if (timer.isCounting()){
                    timer.cancelTimer();
                    exercise.setRemainingTime(timer.getRemainingTime());
                }
                else {
                    timer = new Timer(exercise.getRemainingTime(), 1000, this);
                    timer.startTimer();
                }

                break;

            //short press of reset button adds 30 seconds
            case R.id.timer_reset_button :
                boolean wasCountingWhenPressed = timer.isCounting();
                timer.cancelTimer();
                timer = new Timer(timer.getRemainingTime() + 30000, 1000, this);
                timerDisplay.setText(Timer.timeToString(timer.getRemainingTime()));
                exercise.setRemainingTime(timer.getRemainingTime());
                if (wasCountingWhenPressed) {
                    timer.startTimer();
                }
                break;

            case R.id.timer_set_button :
                break;

            case R.id.plus5_button :
                exercise.changeTempo(5);
                recyclerAdapter.notifyItemChanged(position);
                tempoDisplay.setText(String.valueOf(exercise.getTempo()));
                break;

            case R.id.plus1_button :
                exercise.changeTempo(1);
                recyclerAdapter.notifyItemChanged(position);
                tempoDisplay.setText(String.valueOf(exercise.getTempo()));
                break;

            case R.id.minus1_button :
                exercise.changeTempo(-1);
                recyclerAdapter.notifyItemChanged(position);
                tempoDisplay.setText(String.valueOf(exercise.getTempo()));
                break;

            case R.id.minus5_button :
                exercise.changeTempo(-5);
                recyclerAdapter.notifyItemChanged(position);
                tempoDisplay.setText(String.valueOf(exercise.getTempo()));
                break;

            case R.id.metro_sound_button :
                break;

            default :
        }
    }

    //FIXME remaingTime is being saved to the wrong exercise.
    //Position is being updated before this method call, but should be after
    public void switchExerciseForTimer(int prevPosition, int newPosition) {
        timer.cancelTimer();

        Exercise prevExercise = exerciseList.get(prevPosition);
        prevExercise.setRemainingTime(timer.getRemainingTime());

        Exercise newExercise = exerciseList.get(newPosition);
        timer = new Timer(newExercise.getRemainingTime(), 1000, this);
    }

    private void createSampleExercises() {
        exerciseList.add(new Exercise("Bb scales asc/desc", 65, 4000));
        exerciseList.add(new Exercise("Am arpeggios", 80, 900000));
        exerciseList.add(new Exercise("Am scales asc/desc", 88, 300000));
        exerciseList.add(new Exercise("Turkey in the straw", 120, 150000));
        exerciseList.add(new Exercise("Hava Nagila", 145, 150000));
    }

}
