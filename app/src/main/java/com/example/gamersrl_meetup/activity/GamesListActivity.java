package com.example.gamersrl_meetup.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gamersrl_meetup.R;
import com.example.gamersrl_meetup.adapter.GameAdapter;
import com.example.gamersrl_meetup.database.DatabaseHelper_Game;
import com.example.gamersrl_meetup.model.Game;

import java.util.List;

public class GamesListActivity extends AppCompatActivity
{
    // Set up the Log tag
    private final String LOG_TAG = "GAMES LIST ACTIVITY - ";

    private RecyclerView mRecyclerView;
    private LinearLayoutManager layoutManager;
    private DatabaseHelper_Game dbHelper;
    private GameAdapter adapter;
    private View mStartOfRecyclerViewDivider;
    private View mEndOfRecyclerViewDivider;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        // Load the Saved Instance State and set the View to the list_page_layout layout
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_page_layout);

        // Instantiate the RecyclerView, the dividers indicating the start and end the RecyclerView, and the Button
        Log.d(LOG_TAG, "Instantiating UI elements");
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

        Log.d(LOG_TAG, "Populating list");
        games = dbHelper.getAllGames();

        // Make the adapter and set it onto the RecyclerView
        Log.d(LOG_TAG, "Making adapter");
        adapter = new GameAdapter(games);
        mRecyclerView.setAdapter(adapter);

        // Make a LinearLayoutManager to draw the objects and set it onto the RecyclerView
        Log.d(LOG_TAG, "Making linear layout manager");
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
    }
}
