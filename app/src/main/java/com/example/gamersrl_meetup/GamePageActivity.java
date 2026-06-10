package com.example.gamersrl_meetup;

import androidx.appcompat.app.AppCompatActivity;
// Sources for logging in Android Studio with Log.d:
// https://stackoverflow.com/a/16783890
// https://developer.android.com/studio/debug
import android.content.Intent;
import android.util.Log;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class GamePageActivity extends AppCompatActivity
{
    private TextView mGamePageTextView;
    private EditText mGamePageEditText;
    private Button mBackButton;

    // Source for constant syntax: https://www.w3schools.com/java/java_variables_final.asp
    final String strLogTag = "GamePage - ";

    @Override
    protected  void onCreate(Bundle savedInstanceState)
    {
        Log.d(strLogTag, (strLogTag + "creating view"));
        // Set the view
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_page_layout);

        // Set the UI elements
        mGamePageTextView = findViewById(R.id.game_page_header);
        mGamePageEditText = findViewById(R.id.game_page_field);
        mBackButton = findViewById(R.id.game_page_back_button);

        // Set onClick Listener
        mBackButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // Create Intent for the Game Page and start it
                // Source: https://www.geeksforgeeks.org/android/creating-multiple-screen-applications-in-android/
                Intent GamePageIntent = new Intent(GamePageActivity.this, MainActivity.class);
                startActivity(GamePageIntent);
            }
        });
    }
}
