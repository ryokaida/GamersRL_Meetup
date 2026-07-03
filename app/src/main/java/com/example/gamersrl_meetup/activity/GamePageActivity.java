//package com.example.gamersrl_meetup;
//
//
//import androidx.appcompat.app.AppCompatActivity;
//// Sources for logging in Android Studio with Log.d:
//// https://stackoverflow.com/a/16783890
//// https://developer.android.com/studio/debug
//import android.content.Intent;
//import android.util.Log;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//import android.widget.TextView;
//
//
///**
// * Class Name: Game Page
// * Description: Displays the list of all games.
// */
//public class GamePageActivity extends AppCompatActivity
//{
//    // Source for constant syntax: https://www.w3schools.com/java/java_variables_final.asp
//    final String strLogTag = "GamePage - ";
//
//    private TextView mGamePageTextView;
//    private Button mBackButton;
//    private Button mToGameDetailsPageButton;
//
//
//    /**
//     * Create the view.
//     *
//     * @param savedInstanceState If the activity is being re-initialized after
//     *     previously being shut down then this Bundle contains the data it most
//     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
//     *
//     */
//    @Override
//    protected  void onCreate(Bundle savedInstanceState)
//    {
//        Log.d(strLogTag, (strLogTag + "creating view"));
//
//        // Set the view
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.game_page_layout);
//
//        // Set the UI elements
//        mGamePageTextView = findViewById(R.id.game_page_header);
//        mBackButton = findViewById(R.id.game_page_back_button);
//        mToGameDetailsPageButton = findViewById(R.id.to_game_details_page_button);
//
//        // Set header text
//        mGamePageTextView.setText(R.string.game_page_activity_name);
//
//        // Set onClick Listener
//        mToGameDetailsPageButton.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                // Create Intent for the Game Page and start it
//                // Sources:
//                // https://www.geeksforgeeks.org/android/creating-multiple-screen-applications-in-android/
//                // https://stackoverflow.com/a/6308691
//                //startActivity(new Intent(GamePageActivity.this, GameDetailsPageActivity.class));
//            }
//        });
//
//        // Set onClick Listener for Back Button
//        mBackButton.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                // Create Intent for the Game Page and start it
//                // Sources:
//                // https://www.geeksforgeeks.org/android/creating-multiple-screen-applications-in-android/
//                // https://stackoverflow.com/a/6308691
//                startActivity(new Intent(GamePageActivity.this, MainActivity.class));
//            }
//        });
//    }
//}