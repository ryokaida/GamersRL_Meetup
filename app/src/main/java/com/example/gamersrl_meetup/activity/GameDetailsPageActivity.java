package com.example.gamersrl_meetup.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gamersrl_meetup.R;
import com.example.gamersrl_meetup.model.Game;

/**
 * Class Name: Game Details Page
 * Description: Displays the details for the pertinent game.
 */
public class GameDetailsPageActivity extends AppCompatActivity
{
    // Set up the Log tag [26]
    final String strLogTag = "GAME DETAILS PAGE - ";

    private TextView mGameDetailsPageTextView;

    /**
     * Create the view.
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        Log.d(strLogTag, (strLogTag + "creating view"));

        // Set the view
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_details_page_layout);

        // Instantiate the UI elements
        mGameDetailsPageTextView = findViewById(R.id.game_details_page_header);

        // Set header text
        //mGameDetailsPageTextView.setText(R.string.game_details_page_activity_name);

        // Get item to display from the intent extras
        Game game = getIntent().getParcelableExtra("itemDetails");

        mGameDetailsPageTextView.setText(game.getTitle());
    }
}