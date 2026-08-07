package com.example.gamersrl_meetup.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.gamersrl_meetup.R;
import com.example.gamersrl_meetup.model.Game;

import java.util.ArrayList;
import java.util.List;

/**
 * UpdateGameActivity class
 *
 * Enables the user to enter information for a Game and update it in the System.
 */
public class UpdateGameActivity extends AbstractAddUpdateGameActivity implements View.OnClickListener
{
    // Set up the Log tag [26]
    private final String LOG_TAG = "UPDATE GAME ACTIVITY - ";

    // Use the parent AbstractAddUpdateGameActivity's logic to initialize UI elements

    // Initialize the Game to update
    Game mGame;

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
        // Use the parent AbstractAddUpdateGameActivity's logic to Load the Saved Instance State and set the layout
        super.onCreate(savedInstanceState);

        // Instantiate UI elements
        Log.d(LOG_TAG, "Instantiating UI elements for the Add Game Request page");
        mTitleEditText = findViewById(R.id.input_title);
        mDescriptionEditText = findViewById(R.id.input_description);
        mReleaseDateEditText = findViewById(R.id.input_releasedate);
        mDeveloperEditText = findViewById(R.id.input_developer);
        mPublisherEditText = findViewById(R.id.input_publisher);
        mMinPlayersEditText = findViewById(R.id.input_minplayers);
        mMaxPlayersEditText = findViewById(R.id.input_maxplayers);
        mSubmitButton = findViewById(R.id.submit_button);
        mResetButton = findViewById(R.id.reset_button);
        mBackButton = findViewById(R.id.back_button);

        // Set the OnClick Listener for the buttons
        mSubmitButton.setOnClickListener(this);
        mResetButton.setOnClickListener(this);
        mBackButton.setOnClickListener(this);

        // Enable the Approved status EditText
        mApprovedEditText = findViewById(R.id.input_approved);
        mApprovedEditText.setVisibility(View.VISIBLE);

        // Get item to display from the intent extras
        Log.d(LOG_TAG, "Retrieving the Game to update from the intent extras");
        mGame = getIntent().getParcelableExtra("gameToUpdate");
    }

    /**
     * Validate the input and then update the Game in the database when the Submit button is clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v)
    {
        // Get the ID of the clicked button
        int id = v.getId();

        /**
         * If the user clicked the Submit button, then handle updating the Game.
         * If the user clicked the Reset button, then clear the fields.
         * If the user clicked the Back button, then navigate back to the Game Details page.
         */
        if (id == R.id.submit_button)
        {
            handleUpdatingIteminDB();
        }
        else if (id == R.id.reset_button)
        {
            clearFields();
        }
        else if (id == R.id.back_button)
        {
            Intent intent = new Intent(UpdateGameActivity.this, GameDetailsActivity.class);
            intent.putExtra("updatedGame", mGame);
            intent.putExtra("parentActivity", "UpdateGameActivity");
            startActivity(intent);
        }
    }

    /**
     * Do the logic to validate the input and submit the update Game.
     */
    private void handleUpdatingIteminDB()
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
            String approved = mApprovedEditText.getText().toString().trim().toUpperCase();

            /**
             * Convert the release date from the DB to match the format entered by the user [40].
             * This is done to make input validation and updating in the database easier.
             */
            String strReleaseDateFromDb = DATE_FORMAT.format(mGame.getReleaseDate());
            Log.d(LOG_TAG, "Setting Game release date from DB: " + strReleaseDateFromDb);

            /**
             * If any of the entered fields are blank, then use the data that is already on the Game.
             * Otherwise, use the entered data (ensure that all data is converted to a String for input validation).
             * This is done to ensure that any data that the user does not want to update stays the same.
             * [46]
             */
            title = (title.isEmpty()) ? mGame.getTitle() : title;
            description = (description.isEmpty()) ? mGame.getDescription() : description;
            developer = (developer.isEmpty()) ? mGame.getDeveloper() : developer;
            publisher = (publisher.isEmpty()) ? mGame.getPublisher() : publisher;
            releaseDate = (releaseDate.isEmpty()) ? strReleaseDateFromDb : releaseDate;
            minPlayers = (minPlayers.isEmpty()) ? String.valueOf(mGame.getMinPlayers()) : minPlayers;
            maxPlayers = (maxPlayers.isEmpty()) ? String.valueOf(mGame.getMaxPlayers()) : maxPlayers;
            approved = (approved.isEmpty()) ? mGame.getApproved() : approved;

            Log.d(LOG_TAG, "All fields are populated");

            /**
             * If the release date was changed, then validate it.
             * Otherwise, don't revalidate it.
             */
            if (!releaseDate.equals(strReleaseDateFromDb))
            {
                validateReleaseDate(releaseDate);
            }

            /**
             * If the min players was changed, then validate it and set the minPlayersChanged Boolean variable to indicate that it was changed.
             * Otherwise, don't revalidate it.
             */
            boolean minPlayersChanged = false;
            if (!minPlayers.equals(String.valueOf(mGame.getMinPlayers())))
            {
                validateNumberOfPlayers(minPlayers);
                minPlayersChanged = true;
            }

            /**
             * If the max players was changed, then validate it and set the maxPlayersChanged Boolean variable to indicate that it was changed.
             * Otherwise, don't revalidate it.
             */
            boolean maxPlayersChanged = false;
            if (!maxPlayers.equals(String.valueOf(mGame.getMaxPlayers())))
            {
                validateNumberOfPlayers(maxPlayers);
                maxPlayersChanged = true;
            }

            /**
             * If either min players or max players was changed, then validate that max players is not less than min players.
             * Otherwise, don't revalidate.
             */
            if (minPlayersChanged || maxPlayersChanged)
            {
                validateMinAndMaxPlayers(Integer.parseInt(minPlayers), Integer.parseInt(maxPlayers));
            }

            /**
             * If the approved status was changed, then validate that it is "Y" or "N".
             * Otherwise, don't revalidate it.
             */
            if (!approved.equals(mGame.getApproved()))
            {
                /**
                 * If the Approved status is Y or N, then continue.
                 * Otherwise, inform the user that they must use Y or N.
                 */
                if (approved.equals("Y") || approved.equals("N"))
                {
                    Log.d(LOG_TAG, "Approved status is valid");
                }
                else
                {
                    Log.e(LOG_TAG, "Invalid Approved status");
                    throw new Exception("Please enter \"Y\" or \"N\" for the Approved status!");
                }
            }

            Log.d(LOG_TAG, "Entered data is valid");

            /**
             * Update the selected Game to be approved
             * Ensure that Release Date is converted to Date before processing in the database [2] [35]
             */
            dbHelper.updateItemInDB(String.valueOf(mGame.getId()), title, description, developer, publisher, DATE_FORMAT.parse(releaseDate), 1, 1, mGame.getPictureURI(), approved);
            Toast.makeText(this, "min: " + minPlayers + " | max: " + maxPlayers, Toast.LENGTH_SHORT).show();

            // Get the updated Game from the database, so it can be passed back to the Game Details page when the user clicks the Back button
            mGame = getUpdatedGameFromDB(String.valueOf(mGame.getId()));

            // Reset the page for a new Game to be added
            clearFields();
        }
        catch (Exception e)
        {
            Log.e(LOG_TAG, "Error updating game: " + e.getMessage());
            showSnackbar(mSubmitButton, e.getMessage());
        }
    }

    /**
     * Retrieve the updated Game after submitting updates.
     *
     * @param id The ID of the Game
     * @return The updated Game
     */
    private Game getUpdatedGameFromDB(String id)
    {
        // Make a new List of Games
        List<Game> games = new ArrayList<>();
        /** Filter the database by category to find the items that match the selected filter option */
        Log.d(LOG_TAG, "Getting the updated Game from DB using ID: " + id);
        // Make the query to get data
        String selectQuery = "SELECT * FROM " + dbHelper.getTableName() + " WHERE id = ?";
        games = dbHelper.getItemsFromDB(selectQuery, new String[]{id});

        // Return the resulting Game (should only be 1 result in the list)
        return games.get(0);
    }

    /**
     * Clear the input fields to refresh the page for new information to be entered, as well as the Approved EditText.
     */
    @Override
    public void clearFields()
    {
        super.clearFields();

        // Also clear the Approved EditText
        mApprovedEditText.setText("");
    }
}