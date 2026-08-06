package com.example.gamersrl_meetup.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gamersrl_meetup.R;
import com.example.gamersrl_meetup.database.DatabaseHelper_Game;
import com.example.gamersrl_meetup.model.Game;

import java.text.SimpleDateFormat;

/**
 * GameDetailsActivity class
 *
 * Displays the details for the pertinent game.
 */
public class GameDetailsActivity extends AppCompatActivity implements View.OnClickListener
{
    // Set up the Log tag [26]
    final String LOG_TAG = "GAME DETAILS PAGE - ";

    // Initialize the UI elements
    private TextView mGameDetailsTitle;
    private TextView mGameDetailsId;
    private TextView mGameDetailsDeveloper;
    private TextView mGameDetailsPublisher;
    private TextView mGameDetailsReleaseDate;
    private TextView mGameDetailsDescription;
    private TextView mGameDetailsMinPlayers;
    private TextView mGameDetailsMaxPlayers;
    private ImageView mGameDetailsImage;
    private TextView mGameDetailsApproved, mGameDetailsApprovedLabel;
    private Button mApproveGameRequestButton, mDeleteGameButton;

    // Initialize the boolean to determine the user's role
    private Boolean isAdmin;

    // Initialize the database helper
    private DatabaseHelper_Game dbHelper;

    // Initialize the selected Game
    Game mGame;

    /**
     * Create the page for the Game details and populate the data.
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        // Set the view
        Log.d(LOG_TAG, "Creating the Game Details view");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_details);

        // Instantiate the UI elements
        mGameDetailsTitle = findViewById(R.id.game_details_title);
        mGameDetailsId = findViewById(R.id.game_details_id);
        mGameDetailsDeveloper = findViewById(R.id.game_details_developer);
        mGameDetailsPublisher = findViewById(R.id.game_details_publisher);
        mGameDetailsReleaseDate = findViewById(R.id.game_details_releasedate);
        mGameDetailsDescription = findViewById(R.id.game_details_description);
        mGameDetailsMinPlayers = findViewById(R.id.game_details_minplayers);
        mGameDetailsMaxPlayers = findViewById(R.id.game_details_maxplayers);
        mGameDetailsImage = findViewById(R.id.game_details_image);
        mGameDetailsApproved = findViewById(R.id.game_details_approved);
        mGameDetailsApprovedLabel = findViewById(R.id.game_details_label_approved);
        mApproveGameRequestButton = findViewById(R.id.approvegame_button);
        mDeleteGameButton = findViewById(R.id.deletegame_button);

        // Make a Database Helper to manipulate the database
        Log.d(LOG_TAG, "Making database helper");
        dbHelper = new DatabaseHelper_Game(this);

        // Get item to display from the intent extras
        Log.d(LOG_TAG, "Retrieving the selected Game from the intent extras");
        mGame = getIntent().getParcelableExtra("itemDetails");

        // Get the user role from the intent extras to determine what UI elements and actions should be allowed
        //isAdmin = getIntent().getBooleanExtra("isAdmin", false);
        // TODO - Pass admin role in via intent
        isAdmin = new TempAdminTester().getIsAdmin();

        /**
         * Set the text to the correct data
         */
        Log.d(LOG_TAG, "Setting Game title: " + mGame.getTitle());
        mGameDetailsTitle.setText(mGame.getTitle());
        Log.d(LOG_TAG, "Setting Game ID: " + String.valueOf(mGame.getId()));
        mGameDetailsId.setText(String.valueOf(mGame.getId()));
        Log.d(LOG_TAG, "Setting Game developer: " + mGame.getDeveloper());
        mGameDetailsDeveloper.setText(mGame.getDeveloper());
        Log.d(LOG_TAG, "Setting Game publisher: " + mGame.getPublisher());
        mGameDetailsPublisher.setText(mGame.getPublisher());

        // Format the Release Date to a nicer format for display and then set the text [40]
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        String strReleaseDateForDisplay = dateFormat.format(mGame.getReleaseDate());
        Log.d(LOG_TAG, "Setting Game release date: " + strReleaseDateForDisplay);
        mGameDetailsReleaseDate.setText(strReleaseDateForDisplay);

        Log.d(LOG_TAG, "Setting Game description: " + mGame.getDescription());
        mGameDetailsDescription.setText(mGame.getDescription());
        Log.d(LOG_TAG, "Setting Game min players: " + mGame.getMinPlayers());
        mGameDetailsMinPlayers.setText(String.valueOf(mGame.getMinPlayers()));
        Log.d(LOG_TAG, "Setting Game max players: " + mGame.getMaxPlayers());
        mGameDetailsMaxPlayers.setText(String.valueOf(mGame.getMaxPlayers()));
        // Populate the image with the correct Product icon [22]
        Log.d(LOG_TAG, "Setting Game image");
        mGameDetailsImage.setImageResource(mGame.getPictureURI());

        //isAdmin = true;
        /**
         * If the user is an admin, then show the Game Approved status.
         * Otherwise, do not show it.
         */
        if (isAdmin)
        {
            // Show Game Approved status
            Log.d(LOG_TAG, "Setting Game Approved status: " + mGame.getApproved());
            mGameDetailsApproved.setVisibility(View.VISIBLE);
            mGameDetailsApproved.setText(mGame.getApproved());

            /**
             * If the game is not approved yet, then enable button and logic to approve the Game Request
             * Otherwise, do nothing.
             */
            if (mGame.getApproved().equals("N"))
            {
                Log.d(LOG_TAG, "Admin needs to approve the game");
                // Set the Approve Game Request button to be visible and clickable
                mApproveGameRequestButton.setVisibility(View.VISIBLE);
                mApproveGameRequestButton.setClickable(true);
                // Set OnClick Listener on the Approve Game Request button.
                mApproveGameRequestButton.setOnClickListener(this);
            }
            else
            {
                Log.d(LOG_TAG, "Game already approved");
            }

            // Set the Delete button to be visible and clickable
            mDeleteGameButton.setVisibility(View.VISIBLE);
            mDeleteGameButton.setClickable(true);
            // Set OnClick Listener on the Approve Game Request button.
            mDeleteGameButton.setOnClickListener(this);
        }
        else
        {
            // Hide Game Approved status
            Log.d(LOG_TAG, "Setting Game Approved to invisible");
            mGameDetailsApproved.setVisibility(View.GONE);
            mGameDetailsApprovedLabel.setVisibility(View.GONE);

            // Hide the Approve Game Request button and prevent clicking
            mApproveGameRequestButton.setVisibility(View.GONE);
            mApproveGameRequestButton.setClickable(false);

            // Hide the Delete button and prevent clicking
            mDeleteGameButton.setVisibility(View.GONE);
            mDeleteGameButton.setClickable(false);
        }
    }

    /**
     * Handle the logic to approving/deleting the game when the pertinent buttons are clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v)
    {
        // Get the ID of the selected button
        int id = v.getId();

        /**
         * If the user clicked the Approve Game button, then approve the game.
         * If the user clicked the Delete Game button, then delete the game.
         */
        if (id == R.id.approvegame_button)
        {
            /**
             * Update the selected Game to be approved
             * Pass in the data that the Game already has to ensure that only the Approved attribute changes.
             */
            dbHelper.updateItemInDB(String.valueOf(mGame.getId()), mGame.getTitle(), mGame.getDescription(), mGame.getDeveloper(), mGame.getPublisher(), mGame.getReleaseDate(), mGame.getMinPlayers(), mGame.getMaxPlayers(), mGame.getPictureURI(), "Y");

            // Display a success Toast
            Toast.makeText(this, "Successfully approved game!", Toast.LENGTH_SHORT).show();

            navigateBackToGamesList();

        }
        else if (id == R.id.deletegame_button)
        {
            // Delete the selected Game
            dbHelper.deleteGameFromDB(String.valueOf(mGame.getId()));

            // Display a success Toast
            Toast.makeText(this, "Successfully removed game!", Toast.LENGTH_SHORT).show();

            navigateBackToGamesList();
        }
    }

    /**
     * Helper method to navigate back to the Games List page.
     */
    private void navigateBackToGamesList()
    {
        Log.d(LOG_TAG, "Navigating back to Games List page");
        Intent intent = new Intent(GameDetailsActivity.this, AppContentActivity.class);
        startActivity(intent);
    }
}