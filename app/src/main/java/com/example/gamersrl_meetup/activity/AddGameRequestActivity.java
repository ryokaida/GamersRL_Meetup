package com.example.gamersrl_meetup.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gamersrl_meetup.R;

/**
 * AddGameRequestActivity class
 *
 * Enables the user to enter information for a Game and request that it be added to the System.
 * The game is technically added to the database but in the hidden mode (approved = "N").  The Admins must set it to the approved mode (approved = "Y").
 */
public class AddGameRequestActivity extends AppCompatActivity implements View.OnClickListener
{
    // Set up the Log tag [26]
    private final String LOG_TAG = "ADD GAME REQUEST ACTIVITY - ";

    // Initialize the UI elements
    private TextView mAddGameInstructions;
    private EditText mTitleEditText, mDescriptionEditText, mReleaseDateEditText, mDeveloperEditText, mPublisherEditText, mMinPlayersEditText, mMaxPlayersEditText;
    private Button mSubmitButton;

    /**
     * Create the page for the Add Game Request page.
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
        setContentView(R.layout.add_game_request_page);

        // Instantiate the UI elements
        mAddGameInstructions = findViewById(R.id.addgame_instructions);
        mTitleEditText = findViewById(R.id.input_title);
        mDescriptionEditText = findViewById(R.id.input_description);
        mReleaseDateEditText = findViewById(R.id.input_releasedate);
        mDeveloperEditText = findViewById(R.id.input_developer);
        mPublisherEditText = findViewById(R.id.input_publisher);
        mMinPlayersEditText = findViewById(R.id.input_minplayers);
        mMaxPlayersEditText = findViewById(R.id.input_minplayers);
        mSubmitButton = findViewById(R.id.submit_button);

        // Set the OnClick Listener for the Submit button
        mSubmitButton.setOnClickListener(this);
    }

    /**
     * Validate the input and then add the new Game to the database in the hidden mode when the Submit button is clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v)
    {

    }

    /**
     * Clear the input fields to refresh the page for new information to be entered.
     */
    private void clearFields()
    {
        mTitleEditText.setText("");
        mDescriptionEditText.setText("");
        mReleaseDateEditText.setText("");
        mDeveloperEditText.setText("");
        mPublisherEditText.setText("");
        mMinPlayersEditText.setText("");
        mMaxPlayersEditText.setText("");
    }
}
//        this.id = id;
//        this.title = title;
//        this.description = description;
//        this.developer = developer;
//        this.publisher = publisher;
//        this.releaseDate = releaseDate;
//        this.minPlayers = minPlayers;
//        this.maxPlayers = maxPlayers;
//        this.pictureURI = pictureURI;
//        this.approved = approved;