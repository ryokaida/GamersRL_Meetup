package com.example.gamersrl_meetup.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gamersrl_meetup.R;
import com.example.gamersrl_meetup.database.DatabaseHelper_Game;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * AbstractAddUpdateGameActivity class
 *
 * Abstract class to
 * Enables the user to enter information for a Game and request that it be added to the System.
 * The game is technically added to the database but in the hidden mode (approved = "N").  The Admins must set it to the approved mode (approved = "Y").
 */
public abstract class AbstractAddUpdateGameActivity extends AppCompatActivity
{
    // Set up the Log tag [26]
    private final String LOG_TAG = "ABSTRACT ADD/UPDATE GAME ACTIVITY - ";

    // Initialize the UI elements
    public EditText mTitleEditText, mDescriptionEditText, mReleaseDateEditText, mDeveloperEditText, mPublisherEditText, mMinPlayersEditText, mMaxPlayersEditText, mApprovedEditText;
    public Button mSubmitButton, mResetButton, mBackButton;

    // Initialize the database helper
    public DatabaseHelper_Game dbHelper;

    public final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy");

    /**
     * Create the page for the Add Game Request/Update Game pages.
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
        Log.d(LOG_TAG, "Creating the page view");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_update_game);

        // Instantiating the UI elements is done by the concrete inheritors of this abstract class

        // Make a Database Helper to manipulate the database
        Log.d(LOG_TAG, "Making database helper");
        dbHelper = new DatabaseHelper_Game(this);
    }

    /**
     * Clear the input fields to refresh the page for new information to be entered.
     */
    public void clearFields()
    {
        Log.d(LOG_TAG, "Clearing fields for Game Details");
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
    public void showSnackbar(View v, String message)
    {
        Snackbar snackbar = Snackbar.make(v, message, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }

    /**
     * Verify that Min/Max Players has a valid format.
     *
     * @param numberOfPlayers Min/Max Players
     */
    public void validateNumberOfPlayers(String numberOfPlayers)
    {
        /**
         * The logic for retrieving the entered data and validating it is in a TryCatch, so the code can be cleaner (not having a bunch of nested If Statements).
         * Inside the TryCatch, if there is invalid data, an exception is thrown.
         * The exception is then handled by rethrowing it to the calling method.
         */
        try
        {
            Log.d(LOG_TAG, "Validating Number of Players");
            // Initialize min/max players as integers
            int intNumberOfPlayers;

            /**
             * Verify that the number of players entries are numbers by attempting to parse them into integers.
             * If this fails, catch the NumberFormatException and inform the user to use numbers.
             */
            try
            {
                intNumberOfPlayers = Integer.parseInt(numberOfPlayers);
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
            if (intNumberOfPlayers < 1)
            {
                Log.e(LOG_TAG, "Invalid amount of min/max players");
                throw new Exception("Please enter at least 1 player for the minimum and maximum number of players!");
            }

            Log.d(LOG_TAG, "Min and max number of players are at least 1");

            Log.d(LOG_TAG, "Valid format for min/max players");
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * Verify that max players is not less than min players.
     */
    public void validateMinAndMaxPlayers(int intMinPlayers, int intMaxPlayers)
    {
        /**
         * The logic for retrieving the entered data and validating it is in a TryCatch, so the code can be cleaner (not having a bunch of nested If Statements).
         * Inside the TryCatch, if there is invalid data, an exception is thrown.
         * The exception is then handled by rethrowing it to the calling method.
         */
        try
        {
            Log.d(LOG_TAG, "Validating that Max Players is not less than Min Players");
            /**
             * Verify that the max number of players is not less than the min number of players.
             * If the max number of players is less than the min number of players, then inform the user to put a max number of players that is greater than the min number of players.
             */
            if (intMinPlayers > intMaxPlayers)
            {
                Log.e(LOG_TAG, "max players is greater than min players");
                throw new Exception("Please ensure that the maximum number of players is greater than minimum number of players!");
            }
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * Validate that the release date has a valid format and is not in the future.
     *
     * @param releaseDate The release date for the Game
     */
    public void validateReleaseDate(String releaseDate)
    {
        /**
         * The logic for retrieving the entered data and validating it is in a TryCatch, so the code can be cleaner (not having a bunch of nested If Statements).
         * Inside the TryCatch, if there is invalid data, an exception is thrown.
         * The exception is then handled by rethrowing it to the calling method.
         */
        try
        {
            Log.d(LOG_TAG, "Validating Release Date");
            // Initialize release date as a date
            Date dateRelaseDate = new Date();

            /**
             * Try to convert birthdate to date [2] [35].
             * If this fails, inform the user that they need to use the correct date format.
             */
            try
            {
                dateRelaseDate = DATE_FORMAT.parse(releaseDate);
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
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
}