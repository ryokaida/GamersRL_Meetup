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

import androidx.appcompat.app.AppCompatActivity;

import com.example.gamersrl_meetup.R;
import com.example.gamersrl_meetup.database.DatabaseHelper_Game;
import com.example.gamersrl_meetup.model.Game;

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
    private Button mApproveGameRequestButton;

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
        setContentView(R.layout.game_details_page_layout);

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

        // Make a Database Helper to manipulate the database
        Log.d(LOG_TAG, "Making database helper");
        dbHelper = new DatabaseHelper_Game(this);

        // Get item to display from the intent extras
        Log.d(LOG_TAG, "Retrieving the selected Game from the intent extras");
        mGame = getIntent().getParcelableExtra("itemDetails");

        // Get the user role from the intent extras to determine what UI elements and actions should be allowed
        isAdmin = getIntent().getBooleanExtra("isAdmin", false);

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
        Log.d(LOG_TAG, "Setting Game release date: " + mGame.getReleaseDate().toString());
        mGameDetailsReleaseDate.setText(mGame.getReleaseDate().toString());
        Log.d(LOG_TAG, "Setting Game description: " + mGame.getDescription());
        mGameDetailsDescription.setText(mGame.getDescription());
        Log.d(LOG_TAG, "Setting Game min players: " + mGame.getMinPlayers());
        mGameDetailsMinPlayers.setText(String.valueOf(mGame.getMinPlayers()));
        Log.d(LOG_TAG, "Setting Game max players: " + mGame.getMaxPlayers());
        mGameDetailsMaxPlayers.setText(String.valueOf(mGame.getMaxPlayers()));
        // Populate the image with the correct Product icon [22]
        Log.d(LOG_TAG, "Setting Game image");
        mGameDetailsImage.setImageResource(mGame.getPictureURI());

        isAdmin = true;
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

            // Enable button and logic to approve the Game Request
            createApproveGameRequestButton();
        }
        else
        {
            // Hide Game Approved status
            Log.d(LOG_TAG, "Setting Game Approved to invisible");
            mGameDetailsApproved.setVisibility(View.GONE);
            mGameDetailsApprovedLabel.setVisibility(View.GONE);
        }
    }

    /**
     * Set up the Approve Game Request button and add it to the page [27] [28] [29] [30] [31] [32] [33] [34].
     */
    private void createApproveGameRequestButton()
    {
        Log.d(LOG_TAG, "Creating Approve Game Request button");
        // Assign the bottom region LinearLayout as a variable so the button can be added to the page [27] [28] [29] [210] [31] [32] [33].
        LinearLayout bottomRegion = findViewById(R.id.layout_horizontal_bottom);
        // Create the new button
        mApproveGameRequestButton = new Button(this);
        // Set the text, elevation, gravity, background color, and text color of the button
        Log.d(LOG_TAG, "Setting attributes for Approve Game Request button");
        mApproveGameRequestButton.setText(R.string.game_details_page_approvegamerequest_button_text);
        mApproveGameRequestButton.setElevation(20.0F);
        mApproveGameRequestButton.setGravity(Gravity.CENTER);
        mApproveGameRequestButton.setBackgroundColor(getColor(R.color.purple_500));
        mApproveGameRequestButton.setTextColor(getColor(R.color.white));
        // Make a new LayoutParams to set the button width to MATCH_PARENT and WRAP_CONTENT and to set the margins
        Log.d(LOG_TAG, "Setting Layout Params for Approve Game Request button");
        LinearLayout.LayoutParams paramsForButton = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        // Set the margins
        paramsForButton.setMargins(60, 100, 60, 0);
        // Assign the Layout Params to the button
        mApproveGameRequestButton.setLayoutParams(paramsForButton);
        // Add the button to the page
        Log.d(LOG_TAG, "Adding the Approve Game Request button to the page");
        bottomRegion.addView(mApproveGameRequestButton);

        // Set OnClick Listener on the Approve Game Request button.
        mApproveGameRequestButton.setOnClickListener(this);
    }

    /**
     * Handle the logic to approve the Game Request when the Approve Game Request button is clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v)
    {
        //
        dbHelper.updateItemInDB(String.valueOf(mGame.getId()), "", "", "", "", null, -1, -1, -1, "Y");

        // Navigate back to the Games List page
        Log.d(LOG_TAG, "Navigating back to Games List page");
        Intent intent = new Intent(GameDetailsActivity.this, GamesListActivity.class);
        startActivity(intent);
    }
}