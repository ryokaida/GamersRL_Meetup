package com.example.gamersrl_meetup.activity;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gamersrl_meetup.R;

public class AddGameRequestActivity extends AppCompatActivity
{
    // Set up the Log tag [26]
    private final String LOG_TAG = "ADD GAME REQUEST ACTIVITY - ";

    // Initialize the UI elements
    private TextView mAddGameHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        // Load the Saved Instance State and set the layout
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_game_request_page);

        // Instantiate the UI elements
        mAddGameHeader = findViewById(R.id.addgame_header);
    }
}
