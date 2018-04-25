package com.tylerlienhardt.practicetime;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class EditActivity extends AppCompatActivity {

    private EditText nameField;
    private EditText timeField;

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
                intent.putExtra("name", nameField.getText());

                //long time = Timer.stringToTime(timeField.getText());
                intent.putExtra("time", timeField.getText());

                System.out.println(timeField.getText());

                setResult(MainActivity.EDIT_EXERCISE_RESULT_SAVE, intent);
                finish();

            default:
                return true;
        }

    }
}
