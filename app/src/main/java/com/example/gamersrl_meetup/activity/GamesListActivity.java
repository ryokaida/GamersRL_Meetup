package com.example.gamersrl_meetup.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gamersrl_meetup.R;
import com.example.gamersrl_meetup.adapter.Adapter_Game;
import com.example.gamersrl_meetup.database.DatabaseHelper_Game;
import com.example.gamersrl_meetup.model.Game;

import java.util.List;

/**
 * GamesListActivity class
 *
 * Displays a RecyclerView with Games.
 * The user can select a Game to go to its details page by clicking its View button.
 */
public class GamesListActivity extends AppCompatActivity
{
    // Set up the Log tag [26]
    private final String LOG_TAG = "GAMES LIST ACTIVITY - ";

    // Initialize the UI elements
    private RecyclerView mRecyclerView;
    private LinearLayoutManager layoutManager;
    private Button mAddGameRequestButton;

    // Initialize the database helper and the adapter
    private DatabaseHelper_Game dbHelper;
    private Adapter_Game adapter;

    // Initialize the boolean to determine the user's role
    private boolean isAdmin = true;

    /**
     * Create the list of Games and populate the list.
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        // Load the Saved Instance State and set the View to the list_page_layout layout
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_page_layout);

        // Instantiate the RecyclerView, the dividers indicating the start and end the RecyclerView, and the Button
        Log.d(LOG_TAG, "Instantiating UI elements for the Games List page");
        mRecyclerView = findViewById(R.id.recycler_view);

        // Make a Database Helper to manipulate the database
        Log.d(LOG_TAG, "Making database helper");
        dbHelper = new DatabaseHelper_Game(this);

        // Make an empty list of Games
        List<Game> games;

        /**
         * If the database is empty, then populate it.
         * Otherwise, do nothing.
         *
         * Retrieve the table name from the adapter since the isTableEmpty() method is actually called from the abstract DatabaseHelper class,
         * and the abstract class needs to know what table to check.
         */
        if (dbHelper.isTableEmpty("game"))
        {
            Log.d(LOG_TAG, "Game table is empty");
            dbHelper.populateDatabase();
        }
        else
        {
            Log.d(LOG_TAG, "Game table is populated");
        }

        /**
         * If the user is an admin, then show the unapproved games so that they can be approved.
         * Otherwise, only show the approved games and the button to request that a new game be added.
         */
        if (isAdmin)
        {
            // Get all the items for the list
            Log.d(LOG_TAG, "Populating list with all unapproved games");
            games = dbHelper.getAllUnapprovedGames();
        }
        else
        {
            // Get all the items for the list
            Log.d(LOG_TAG, "Populating list with only approved games");
            games = dbHelper.getAllApprovedGames();

            // Add the Request to Add Game button to the bottom of the screen
            createAddGameRequestButton();
        }

        // Make the adapter and set it onto the RecyclerView
        Log.d(LOG_TAG, "Making adapter");
        adapter = new Adapter_Game(games);
        mRecyclerView.setAdapter(adapter);

        // Make a LinearLayoutManager to draw the objects and set it onto the RecyclerView
        Log.d(LOG_TAG, "Making linear layout manager");
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
    }

    /**
     * Set up the Request to Add Game button and add it to the page [27] [28] [29] [30] [31] [32] [33] [34].
     */
    private void createAddGameRequestButton()
    {
        Log.d(LOG_TAG, "Creating Add Game Request button");
        // Assign the bottom region LinearLayout as a variable so the button can be added to the page [27] [28] [29] [210] [31] [32] [33].
        LinearLayout bottomRegion = findViewById(R.id.layout_horizontal_bottom);
        // Create the new button
        mAddGameRequestButton = new Button(this);
        // Set the text, elevation, gravity, background color, and text color of the button
        Log.d(LOG_TAG, "Setting attributes for Add Game Request button");
        mAddGameRequestButton.setText(R.string.games_list_page_addgamerequest_button_text);
        mAddGameRequestButton.setElevation(20.0F);
        mAddGameRequestButton.setGravity(Gravity.CENTER);
        mAddGameRequestButton.setBackgroundColor(getColor(R.color.purple_500));
        mAddGameRequestButton.setTextColor(getColor(R.color.white));
        // Make a new LayoutParams to set the button width to MATCH_PARENT and WRAP_CONTENT and to set the margins
        Log.d(LOG_TAG, "Setting Layout Params for Add Game Request button");
        LinearLayout.LayoutParams paramsForButton = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        // Set the margins
        paramsForButton.setMargins(60, 100, 60, 0);
        // Assign the Layout Params to the button
        mAddGameRequestButton.setLayoutParams(paramsForButton);
        // Add the button to the page
        Log.d(LOG_TAG, "Adding the Add Game Request button to the page");
        bottomRegion.addView(mAddGameRequestButton);

        /**
         * Set OnClick Listener on the Add Game Request button.
         * The OnClick method is used for the button in an inline fashion since there is another button that also needs its own OnClick Listener.
         */
        mAddGameRequestButton.setOnClickListener(new View.OnClickListener()
        {
            /**
             * Navigate to the Add Game Request page when the Add Game Request button is clicked.
             *
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v)
            {
                Log.d(LOG_TAG, "Navigating to Add Game Request page");
                Intent intent = new Intent(GamesListActivity.this, AddGameRequestActivity.class);
                startActivity(intent);
            }
        });
    }
}