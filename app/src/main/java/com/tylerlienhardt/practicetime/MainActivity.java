package com.tylerlienhardt.practicetime;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    static final int EDIT_EXERCISE_REQUEST_CODE = 5;
    static final int EDIT_EXERCISE_RESULT_SAVE = 4;

    private ArrayList<Exercise> exerciseList = new ArrayList<Exercise>();
    private RecyclerView recyclerView;
    private ExerciseAdapter recyclerAdapter;
    private RecyclerView.LayoutManager layoutManager;
    Spinner spinner;

    TextView timerDisplay;
    TextView nameDisplay;
    TextView tempoDisplay;
    ImageButton timerPlayButton;
    ImageButton doublePlayButton;

    Timer timer;
    Metronome metro;

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

        // DRAG REORDORING
        DragHelper dragHelper = new DragHelper(recyclerAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(dragHelper);
        recyclerAdapter.setTouchHelper(touchHelper);
        touchHelper.attachToRecyclerView(recyclerView);

        //DISPLAYS
        nameDisplay = findViewById(R.id.name_display);
        timerDisplay = findViewById(R.id.timer_display);
        tempoDisplay = findViewById(R.id.tempo_display);

        //load data from the first exercise in the list by default
        Exercise exercise = getSelectedExercise();

        createBeatSpinner();
        setButtonListeners();
        setAllToExercise(exercise);
    }

    @Override
    public void onClick(View v) {

        Exercise exercise = getSelectedExercise();

        switch (v.getId()) {

            //TIMER BUTTONS

            case R.id.timer_play_button :

                if (timer.isCounting()){
                    timer.cancelTimer();
                }
                else {
                    setExerciseForTimer(exercise);
                    timer.startTimer();
                }

                break;

            //short press adds 30 seconds
            case R.id.timer_reset_button :

                boolean wasCountingWhenPressed = timer.isCounting();

                timer.cancelTimer();

                //adding 30 seconds
                exercise.setRemainingTime(exercise.getRemainingTime() + 30000);
                setExerciseForTimer(exercise);

                //adding 30 seconds does not stop the counter
                if (wasCountingWhenPressed) {
                    timer.startTimer();
                }

                break;

            case R.id.timer_set_button :

                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                builder.setMessage("Coming soon!")
                        .setTitle("Set New Start Time");

                AlertDialog dialog = builder.create();

                dialog.show();

                break;

            //METRO BUTTONS

            case R.id.metro_play_button :

                if (metro.isRunning() == false) {
                    setExerciseForMetro(exercise);
                    metro.start();
                }
                else {
                    metro.stopRunning();
                }
                break;

            case R.id.plus5_button :

                metro.adjustTempo(5);
                updateTempoDisplays(exercise);
                break;

            case R.id.plus1_button :

                metro.adjustTempo(1);
                updateTempoDisplays(exercise);
                break;

            case R.id.minus1_button :

                metro.adjustTempo(-1);
                updateTempoDisplays(exercise);
                break;

            case R.id.minus5_button :

                metro.adjustTempo(-5);
                updateTempoDisplays(exercise);
                break;

            case R.id.metro_sound_button :
                AlertDialog.Builder soundButtonBuilder = new AlertDialog.Builder(this);

                soundButtonBuilder.setMessage("Coming soon!")
                        .setTitle("Change Metronome Sound");

                AlertDialog soundButtonDialog = soundButtonBuilder.create();

                soundButtonDialog.show();

                break;

            default :
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == EDIT_EXERCISE_REQUEST_CODE) {
            if (resultCode == EDIT_EXERCISE_RESULT_SAVE){

                String name = intent.getStringExtra("name");
                long time = intent.getLongExtra("time", 9000);
                int tempo = intent.getIntExtra("tempo", 99);
                int measure = intent.getIntExtra("measure", 9);

                Exercise exercise = getSelectedExercise();

                exercise.setName(name);
                exercise.setTempo(tempo);
                exercise.setStartTime(time);
                exercise.setMeasure(measure);

                setAllToExercise(exercise);
            }
        }

    }

    public Exercise getSelectedExercise() {
        int position = recyclerAdapter.getPosition();
        return exerciseList.get(position);
    }

    public void setAllToExercise(Exercise exercise) {
        setNameDisplay(exercise);
        spinner.setSelection(exercise.getMeasure() - 1);
        setExerciseForTimer(exercise);
        setExerciseForMetro(exercise);

    }

    public void setNameDisplay(Exercise exercise) {
        nameDisplay.setText(exercise.getName());
    }

    public void setExerciseForTimer(Exercise exercise) {
        stopTimerIfRunning();
        timer = new Timer(exercise.getRemainingTime(), exercise, this);
        updateTimerDisplays(exercise);
    }

    public void setExerciseForMetro(Exercise exercise) {
        stopMetroIfRunning();
        metro = new Metronome(exercise, this);

        updateTempoDisplays(exercise);
    }

    public void updateTimerDisplays(Exercise exercise) {
        recyclerAdapter.notifyItemChanged(recyclerAdapter.getPosition());
        timerDisplay.setText(Timer.timeToString(exercise.getRemainingTime()));
    }

    public void updateTempoDisplays(Exercise exercise) {
        recyclerAdapter.notifyItemChanged(recyclerAdapter.getPosition());
        tempoDisplay.setText(String.valueOf(exercise.getTempo()));
    }

    public void doublePlayAction() {
        if (!timer.isCounting() && !metro.isRunning()) {
            timer.startTimer();
            metro.start();
        }
        else if (!timer.isCounting() && metro.isRunning()){
            timer.startTimer();
        }
        else if (timer.isCounting() && !metro.isRunning()) {
            metro.start();
        }
    }

    public void doublePauseAction(){
        if (timer.isCounting() && metro.isRunning()) {
            timer.cancelTimer();
            metro.stopRunning();
        }
        else if (!timer.isCounting() && metro.isRunning()){
            metro.stopRunning();
        }
        else if (timer.isCounting() && !metro.isRunning()) {
            timer.cancelTimer();
        }
    }

    public void stopTimerIfRunning() {
        if (timer != null && timer.isCounting()) {
            timer.cancelTimer();
        }
    }

    public void stopMetroIfRunning() {
        if (metro != null && metro.isRunning()) {
            metro.stopRunning();
        }
    }

    public void notifyTimerFinished() {
        stopMetroIfRunning();
        Exercise exercise = getSelectedExercise();
        exercise.resetRemainingTime();
        setExerciseForTimer(getSelectedExercise());
        Toast.makeText(this, "Done!", Toast.LENGTH_SHORT).show();
    }

    private void createBeatSpinner() {
        Exercise exercise = getSelectedExercise();

        spinner = (Spinner) findViewById(R.id.time_sig_spinner);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.time_sigs, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);

        spinner.setSelection(exercise.getMeasure() - 1);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                // derive the measure from the selected item string
                String measureStr = (String)parent.getItemAtPosition(position);
                measureStr = measureStr.substring(0, 1);
                int measure = Integer.parseInt(measureStr);
                Exercise exercise = getSelectedExercise();
                exercise.setMeasure(measure);
                metro.updateMeasure();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setButtonListeners() {

        // TIMER BUTTONS

        timerPlayButton = (ImageButton)findViewById(R.id.timer_play_button);
        timerPlayButton.setOnClickListener(this);

        ImageButton timerResetButton = (ImageButton)findViewById(R.id.timer_reset_button);
        timerResetButton.setOnClickListener(this);

        // long press on reset button pauses timer and resets to start time
        timerResetButton.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                if (timer.isCounting()){
                    timer.cancelTimer();
                }

                Exercise exercise = getSelectedExercise();
                exercise.resetRemainingTime();
                setExerciseForTimer(exercise);
                return true;
            }
        });

        Button timerSetButton = (Button)findViewById(R.id.timer_set_button);
        timerSetButton.setOnClickListener(this);

        // METRONOME BUTTONS

        ImageButton metroPlayButton = (ImageButton)findViewById(R.id.metro_play_button);
        metroPlayButton.setOnClickListener(this);

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

    }

    private void createSampleExercises() {
        exerciseList.add(new Exercise("Bb scale", 65, 1, 300000));
        exerciseList.add(new Exercise("Em arpeggios", 80, 1,900000));
        exerciseList.add(new Exercise("Em pentatonic scale", 88, 1, 3000));
        exerciseList.add(new Exercise("Minuet in G", 120, 3,150000));
        exerciseList.add(new Exercise("Hava Nagila", 145, 2, 150000));
        exerciseList.add(new Exercise("All of me in C", 110, 4, 240000));
    }

}
