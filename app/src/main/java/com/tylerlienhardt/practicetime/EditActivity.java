package com.tylerlienhardt.practicetime;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class EditActivity extends AppCompatActivity {

    private EditText nameField;
    private EditText timeField;
    private EditText tempoField;
    private EditText measureField;

    private ArrayList<Recording> recordingList = new ArrayList<Recording>();

    private RecyclerView recyclerView;
    private RecordingAdapter recordingAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // creating the "up" button to return to main activity
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        //POPULATING DATA FIELDS
        Intent intent = getIntent();

        nameField = findViewById(R.id.edit_name_field);
        nameField.setText(intent.getStringExtra("name"));

        timeField = findViewById(R.id.edit_time_field);
        long startTime = intent.getLongExtra("startTime", -1);
        timeField.setText(Timer.timeToString(startTime));

        tempoField = findViewById(R.id.edit_tempo_field);
        tempoField.setText(String.valueOf(intent.getIntExtra("tempo", -1)));

        measureField = findViewById(R.id.edit_measure_field);
        measureField.setText(String.valueOf(intent.getIntExtra("measure", -1)));

        createSampleRecordings();

        //RECORDING RECYCLER VIEW
        recyclerView = (RecyclerView)findViewById(R.id.record_recycler);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        recordingAdapter = new RecordingAdapter(recordingList);
        recyclerView.setAdapter(recordingAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.edit_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Toast.makeText(this, "Canceled", Toast.LENGTH_SHORT).show();
                NavUtils.navigateUpFromSameTask(this);
                return true;

            case R.id.menu_save_button:
                //checking that name field is not empty
                if (nameField.getText().toString().matches("")) {
                    Toast.makeText(this, "You must enter an exercise name.", Toast.LENGTH_SHORT).show();

                    return true;
                }

                Intent intent = new Intent(EditActivity.this, MainActivity.class);

                intent.putExtra("name", nameField.getText().toString());
                long time = Timer.stringToTime(timeField.getText().toString());
                intent.putExtra("time", time);
                intent.putExtra("tempo", Integer.valueOf(tempoField.getText().toString()).intValue());
                intent.putExtra("measure", Integer.valueOf(measureField.getText().toString()).intValue());

                setResult(MainActivity.EDIT_EXERCISE_RESULT_SAVE, intent);
                Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
                finish();
                return true;

            default:
                return true;
        }

    }

    public void createSampleRecordings() {
        Recording rec1 = new Recording("Recording 1", "5/1/18");
        Recording rec2 = new Recording("Recording 2", "5/2/18");
        Recording rec3 = new Recording("Recording 3", "5/2/18");
        Recording rec4 = new Recording("Recording 4", "5/3/18");
        Recording rec5 = new Recording("Recording 5", "5/4/18");
        Recording rec6 = new Recording("Recording 6", "5/4/18");
        Recording rec7 = new Recording("Recording 7", "5/5/18");
        Recording rec8 = new Recording("Recording 8", "5/7/18");

        recordingList.add(rec1);
        recordingList.add(rec2);
        recordingList.add(rec3);
        recordingList.add(rec4);
        recordingList.add(rec5);
        recordingList.add(rec6);
        recordingList.add(rec7);
        recordingList.add(rec8);

    }
}
