package com.example.gamersrl_meetup.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.gamersrl_meetup.R;
import com.example.gamersrl_meetup.model.Game;

/**
 * AddGameRequestActivity class
 *
 * Enables the user to enter information for a Game and request that it be added to the System.
 * The game is technically added to the database but in the hidden mode (approved = "N").  The Admins must set it to the approved mode (approved = "Y").
 */
public class AddGameRequestActivity extends AbstractAddUpdateGameActivity  implements View.OnClickListener
{
    // Set up the Log tag [26]
    private final String LOG_TAG = "ADD GAME REQUEST ACTIVITY - ";

    // Use the parent AbstractAddUpdateGameActivity's logic to initialize UI elements

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
        setContentView(R.layout.activity_add_update_game);

        // Use the parent AbstractAddUpdateGameActivity's logic to instantiate UI elements
        Log.d(LOG_TAG, "Instantiating UI elements for the Add Game Request page");
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

            // Validate the entered data
            validateEnteredData(minPlayers, maxPlayers, releaseDate);
            Log.d(LOG_TAG, "Entered data is valid");

            /**
             * Create the new Game and add it to the database.
             * Ensure that Release Date is converted to Date before processing in the database [2] [35]
             */
            Game newGame = new Game(title, description, developer, publisher, DATE_FORMAT.parse(releaseDate), Integer.parseInt(minPlayers), Integer.parseInt(maxPlayers), R.drawable.neckdefender, "N");
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
    @Override
    public void clearFields()
    {
        super.clearFields();
    }

    /**
     * Helper method to display a snackbar [38].
     *
     * @param v The View to display the snackbar in
     * @param message The message to display
     */
    @Override
    public void showSnackbar(View v, String message)
    {
        super.showSnackbar(v, message);
    }

    /**
     * Validate that the entered data fulfills the requirements for the Game to be added.
     *
     * @param minPlayers The minimum number of players for the Game
     * @param maxPlayers The maximum number of players for the Game
     * @param releaseDate The release date for the Game
     */
    @Override
    public void validateEnteredData(String minPlayers, String maxPlayers, String releaseDate)
    {
        super.validateEnteredData(minPlayers, maxPlayers, releaseDate);
    }
}