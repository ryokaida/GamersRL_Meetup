package com.example.gamersrl_meetup.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gamersrl_meetup.R;
import com.example.gamersrl_meetup.adapter.Adapter_Game;
import com.example.gamersrl_meetup.database.DatabaseHelper_Game;
import com.example.gamersrl_meetup.model.Game;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

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

    // Initialize the database helper
    private DatabaseHelper_Game dbHelper;

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
        Log.d(LOG_TAG, "Instantiating UI elements for the Add Game Request page");
        mAddGameInstructions = findViewById(R.id.addgame_instructions);
        mTitleEditText = findViewById(R.id.input_title);
        mDescriptionEditText = findViewById(R.id.input_description);
        mReleaseDateEditText = findViewById(R.id.input_releasedate);
        mDeveloperEditText = findViewById(R.id.input_developer);
        mPublisherEditText = findViewById(R.id.input_publisher);
        mMinPlayersEditText = findViewById(R.id.input_minplayers);
        mMaxPlayersEditText = findViewById(R.id.input_maxplayers);
        mSubmitButton = findViewById(R.id.submit_button);

        // Set the OnClick Listener for the Submit button
        mSubmitButton.setOnClickListener(this);

        // Make a Database Helper to manipulate the database
        Log.d(LOG_TAG, "Making database helper");
        dbHelper = new DatabaseHelper_Game(this);
    }

    /**
     * Validate the input and then add the new Game to the database in the hidden mode when the Submit button is clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v)
    {
        /**
         * The logic for retrieving the entered data and validating it is in a TryCatch, so the code can be cleaner (not having a bunch of nested If Statements).
         * Inside the TryCatch, if there is invalid data, an exception is thrown.
         * The exception is then handled by showing a Toast to the user about the error.
         */
        try
        {
            // Retrieve entered Game information
            String title = mTitleEditText.getText().toString().trim();
            String description = mDescriptionEditText.getText().toString().trim();
            String developer = mDeveloperEditText.getText().toString().trim();
            String publisher = mPublisherEditText.getText().toString().trim();
            String releaseDate = mReleaseDateEditText.getText().toString().trim();
            String minPlayers = mMinPlayersEditText.getText().toString().trim();
            String maxPlayers = mMaxPlayersEditText.getText().toString().trim();

            // Initialize min and max players as integers
            int intMinPlayers;
            int intMaxPlayers;

            // Initialize release date as a date
            Date dateRelaseDate = new Date();

            /**
             * Verify that the fields are not empty.
             * If they are, inform the user to fill in the fields.
             */
            if (title.isEmpty() || description.isEmpty() || developer.isEmpty() || publisher.isEmpty() || releaseDate.isEmpty() || minPlayers.isEmpty() || maxPlayers.isEmpty())
            {
                Log.e(LOG_TAG, "Empty input data");
                throw new Exception("Please fill in all fields!");
            }

            Log.d(LOG_TAG, "All fields are populated");

            /**
             * Verify that the number of players entries are numbers by attempting to parse them into integers.
             * If this fails, catch the NumberFormatException and inform the user to use numbers.
             */
            try
            {
                intMinPlayers = Integer.parseInt(minPlayers);
                intMaxPlayers = Integer.parseInt(maxPlayers);
            }
            catch (NumberFormatException e)
            {
                Log.e(LOG_TAG, "Error parsing integer from min/max players: " + e.getMessage());
                throw new Exception("Please enter a number for the minimum and maximum number of players!");
            }

            Log.d(LOG_TAG, "Min and max number of players are numbers");

            /**
             * Verify that the min and max number of players are at least 1.
             * If either is less than 1, then inform the user to put in at least 1 for the min and max number of players.
             */
            if (intMinPlayers < 1 || intMaxPlayers < 1)
            {
                Log.e(LOG_TAG, "Invalid amount of min/max players");
                throw new Exception("Please enter at least 1 player for the minimum and maximum number of players!");
            }

            Log.d(LOG_TAG, "Valid min and max number of players");

            /**
             * Try to convert birthdate to date [2] [35].
             * If this fails, inform the user that they need to use the correct date format.
             */
            try
            {
                SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
                dateRelaseDate = dateFormat.parse(releaseDate);
            }
            catch (Exception e)
            {
                Log.e(LOG_TAG, "Error parsing release date: " + e.getMessage());
                throw new Exception("Please use format 'MM/DD/YYYY' for the release date!");
            }

            Log.d(LOG_TAG, "Valid release date format");

            // Get the current date
            Date currentDate = new Date();
            Log.d(LOG_TAG, "Current date: " + currentDate);

            /**
             * Verify that the Game being entered has actually been released [37].
             * If not, then inform the user that the date must not be in the future.
             */
            if (dateRelaseDate.after(currentDate))
            {
                Log.d(LOG_TAG, "Game is not released yet");
                throw new Exception("Please enter a release date that is not in the future!");
            }

            Log.d(LOG_TAG, "Release date is in a valid timeframe");

            // Create the new Game and add it to the database
            Game newGame = new Game(title, description, developer, publisher, dateRelaseDate, intMinPlayers, intMaxPlayers, R.drawable.neckdefender, "N");
            dbHelper.addToDatabase(newGame);
            showSnackbar(mSubmitButton, "Successfully sent request to add new game!");

            // Reset the page for a new Game to be added
            clearFields();
        }
        catch (Exception e)
        {
            showSnackbar(mSubmitButton, "Error requesting for game to be added: " + e.getMessage());
        }
    }

    /**
     * Clear the input fields to refresh the page for new information to be entered.
     */
    private void clearFields()
    {
        Log.d(LOG_TAG, "Clearing fields for Add Game Request page");
        mTitleEditText.setText("");
        mDescriptionEditText.setText("");
        mReleaseDateEditText.setText("");
        mDeveloperEditText.setText("");
        mPublisherEditText.setText("");
        mMinPlayersEditText.setText("");
        mMaxPlayersEditText.setText("");
    }

    /**
     * Helper method to display a snackbar [38].
     *
     * @param v The View to display the snackbar in
     * @param message The message to display
     */
    private void showSnackbar(View v, String message)
    {
        Snackbar snackbar = Snackbar.make(v, message, Snackbar.LENGTH_SHORT);
        snackbar.show();
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