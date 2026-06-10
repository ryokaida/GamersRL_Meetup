package com.example.gamersrl_meetup;

import androidx.appcompat.app.AppCompatActivity;
// Sources for logging in Android Studio with Log.d:
// https://stackoverflow.com/a/16783890
// https://developer.android.com/studio/debug
import android.content.Intent;
import android.util.Log;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
{
    private EditText mStartPageEditText;
    private Button mStartPageButton;
    private Menu mNavMenu;

    public final int intGamePageMenuItemID = R.id.game_page;
    public final int intHomePageMenuItemID = R.id.main_activity;

    // Source for constant syntax: https://www.w3schools.com/java/java_variables_final.asp
    final String strLogTag = "StartPage - ";

    @Override
    protected  void onCreate(Bundle savedInstanceState)
    {
        Log.d(strLogTag, (strLogTag + "creating view"));
        // Set the view
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        // Set the UI elements
        mStartPageEditText = findViewById(R.id.startpage_field);
        mStartPageButton = findViewById(R.id.startpage_button);

        // Set onClick Listener
        mStartPageButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                PopupMenu popup = new PopupMenu(MainActivity.this, mStartPageButton);
                popup.getMenuInflater().inflate(R.menu.nav_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(menuItem -> {
                    if (menuItem.getItemId() == R.id.main_activity)
                    {
                        // Create Intent for the Game Page and start it
                        // Source: https://www.geeksforgeeks.org/android/creating-multiple-screen-applications-in-android/
//                            Intent GamePageIntent = new Intent(MainActivity.this, GamePageActivity.class);
//                            startActivity(GamePageIntent);
                        Toast.makeText(MainActivity.this, "bees", Toast.LENGTH_SHORT).show();
                        return true;
                    }
                    else if (menuItem.getItemId() == R.id.game_page)
                    {
                        // Create Intent for the Game Page and start it
                        // Source: https://www.geeksforgeeks.org/android/creating-multiple-screen-applications-in-android/
                        Intent GamePageIntent = new Intent(MainActivity.this, GamePageActivity.class);
                        startActivity(GamePageIntent);
                        return true;
                    }
                    else
                    {
                        return false;
                    }
                });
                popup.show();
            }
        });
    }

    // Inflate menu resource
    // Source: https://www.geeksforgeeks.org/android/how-to-implement-options-menu-in-android/
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.header_menu, menu);
        return true;
    }
}
