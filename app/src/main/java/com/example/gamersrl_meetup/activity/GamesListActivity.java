package com.example.gamersrl_meetup.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

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
    private DatabaseHelper_Game dbHelper;
    private Adapter_Game adapter;
    private View mStartOfRecyclerViewDivider;
    private View mEndOfRecyclerViewDivider;

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
        mStartOfRecyclerViewDivider = findViewById(R.id.start_of_recyclerview_divider);
        mEndOfRecyclerViewDivider = findViewById(R.id.end_of_recyclerview_divider);

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

        // Get all the items for the list
        Log.d(LOG_TAG, "Populating list");
        games = dbHelper.getAllGames();

        // Make the adapter and set it onto the RecyclerView
        Log.d(LOG_TAG, "Making adapter");
        adapter = new Adapter_Game(games);
        mRecyclerView.setAdapter(adapter);

        // Make a LinearLayoutManager to draw the objects and set it onto the RecyclerView
        Log.d(LOG_TAG, "Making linear layout manager");
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
    }
}