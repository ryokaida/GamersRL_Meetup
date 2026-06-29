package com.example.gamersrl_meetup;


import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


/**
 * Class Name: Game Details Page
 * Description: Displays the details for the pertinent game.
 */
public class GameDetailsPageActivity extends AppCompatActivity
{
    // Source for constant syntax: https://www.w3schools.com/java/java_variables_final.asp
    final String strLogTag = "GameDetailsPage - ";

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

        // Set the UI elements
        mGameDetailsPageTextView = findViewById(R.id.game_details_page_header);

        // Set header text
        mGameDetailsPageTextView.setText(R.string.game_details_page_activity_name);
    }
}