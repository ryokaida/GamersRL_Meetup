package com.example.gamersrl_meetup.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gamersrl_meetup.R;
import com.example.gamersrl_meetup.database.DatabaseHelper_Game;

public class UpdateGameActivity extends AppCompatActivity //implements View.OnClickListener
{
    // Set up the Log tag [26]
    private final String LOG_TAG = "UPDATE GAME ACTIVITY - ";

    // Initialize the UI elements
    private EditText mTitleEditText, mDescriptionEditText, mReleaseDateEditText, mDeveloperEditText, mPublisherEditText, mMinPlayersEditText, mMaxPlayersEditText;
    private Button mSubmitButton;
    private TextView mInstructions;

    // Initialize the database helper
    private DatabaseHelper_Game dbHelper;

    /**
     * Create the page for the Update Game page.
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        // Load the Saved Instance State and set the layout
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_update_game);

        // Instantiate the UI elements
        Log.d(LOG_TAG, "Instantiating UI elements for the Add Game Request page");
        mTitleEditText = findViewById(R.id.input_title);
        mDescriptionEditText = findViewById(R.id.input_description);
        mReleaseDateEditText = findViewById(R.id.input_releasedate);
        mDeveloperEditText = findViewById(R.id.input_developer);
        mPublisherEditText = findViewById(R.id.input_publisher);
        mMinPlayersEditText = findViewById(R.id.input_minplayers);
        mMaxPlayersEditText = findViewById(R.id.input_maxplayers);
        mSubmitButton = findViewById(R.id.submit_button);
        mInstructions = findViewById(R.id.addgame_instructions);

        // Make a Database Helper to manipulate the database
        Log.d(LOG_TAG, "Making database helper");
        //dbHelper = new DatabaseHelper_Game(this);
    }
}
