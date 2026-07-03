package com.example.gamersrl_meetup.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gamersrl_meetup.R;
import com.example.gamersrl_meetup.model.Game;

/**
 * GameDetailsActivity class
 *
 * Displays the details for the pertinent game.
 */
public class GameDetailsActivity extends AppCompatActivity
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
    private TextView mGameDetailsLabelId;
    private TextView mGameDetailsLabelDeveloper;
    private TextView mGameDetailsLabelPublisher;
    private TextView mGameDetailsLabelReleaseDate;
    private TextView mGameDetailsLabelDescription;
    private TextView mGameDetailsLabelMinPlayers;
    private TextView mGameDetailsLabelMaxPlayers;

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
        mGameDetailsLabelId = findViewById(R.id.game_details_label_id);
        mGameDetailsLabelDeveloper = findViewById(R.id.game_details_label_developer);
        mGameDetailsLabelPublisher = findViewById(R.id.game_details_label_publisher);
        mGameDetailsLabelReleaseDate = findViewById(R.id.game_details_label_releasedate);
        mGameDetailsLabelDescription = findViewById(R.id.game_details_label_description);
        mGameDetailsLabelMinPlayers = findViewById(R.id.game_details_label_minplayers);
        mGameDetailsLabelMaxPlayers = findViewById(R.id.game_details_label_maxplayers);

        // Get item to display from the intent extras
        Log.d(LOG_TAG, "Retrieving the selected Game from the intent extras");
        Game game = getIntent().getParcelableExtra("itemDetails");

        /**
         * Set the text to the correct data
         */
        Log.d(LOG_TAG, "Setting Game title: " + game.getTitle());
        mGameDetailsTitle.setText(game.getTitle());
        Log.d(LOG_TAG, "Setting Game ID: " + String.valueOf(game.getId()));
        mGameDetailsId.setText(String.valueOf(game.getId()));
        Log.d(LOG_TAG, "Setting Game developer: " + game.getDeveloper());
        mGameDetailsDeveloper.setText(game.getDeveloper());
        Log.d(LOG_TAG, "Setting Game publisher: " + game.getPublisher());
        mGameDetailsPublisher.setText(game.getPublisher());
        Log.d(LOG_TAG, "Setting Game release date: " + game.getReleaseDate().toString());
        mGameDetailsReleaseDate.setText(game.getReleaseDate().toString());
        Log.d(LOG_TAG, "Setting Game description: " + game.getDescription());
        mGameDetailsDescription.setText(game.getDescription());
        Log.d(LOG_TAG, "Setting Game min players: " + String.valueOf(game.getMinPlayers()));
        mGameDetailsMinPlayers.setText(String.valueOf(game.getMinPlayers()));
        Log.d(LOG_TAG, "Setting Game max players: " + String.valueOf(game.getMaxPlayers()));
        mGameDetailsMaxPlayers.setText(String.valueOf(game.getMaxPlayers()));
        // Populate the image with the correct Product icon [22]
        Log.d(LOG_TAG, "Setting Game image");
        mGameDetailsImage.setImageResource(game.getPictureURI());
    }
}