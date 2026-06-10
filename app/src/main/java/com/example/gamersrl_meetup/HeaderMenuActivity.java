package com.example.gamersrl_meetup;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class HeaderMenuActivity extends AppCompatActivity
{
    // Source for constant syntax: https://www.w3schools.com/java/java_variables_final.asp
    final String strLogTag = "HeaderMenu - ";
    HeaderMenuActivity headerMenuActivity;


    // Constructor
    // Source for syntax: https://www.geeksforgeeks.org/java/constructors-in-java/
    HeaderMenuActivity()
    {
    }


    @Override
    protected  void onCreate(Bundle savedInstanceState)
    {
        Log.d(strLogTag, (strLogTag + "creating view"));
        // Set the view
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
    }

    /***
     * Handle Header Menu selection.  Each selection navigates the user to the pertinent page.
     *
     * Sources:
     * https://www.geeksforgeeks.org/android/how-to-implement-options-menu-in-android/
     * https://developer.android.com/develop/ui/views/components/menus#java
     * https://developer.android.com/guide/topics/resources/menu-resource#java\
     *
     * @param in_Context The activity Context (e.g. MainActivity.this)
     * @param in_MenuItem The menu item that was selected
     * @return
     */
    public boolean handleHeaderMenuSelection(Context in_Context, MenuItem in_MenuItem)
    {
        if (in_MenuItem.getItemId()==R.id.main_activity)
        {
            Toast.makeText(in_Context, "bees", Toast.LENGTH_SHORT).show();
            return true;
        }
        else if (in_MenuItem.getItemId()==R.id.game_page)
        {
            // Create Intent for the Game Page and start it
            // Source: https://www.geeksforgeeks.org/android/creating-multiple-screen-applications-in-android/
            //Intent GamePageIntent = new Intent(in_Context, GamePageActivity.class);
            //startActivity(GamePageIntent);
            startActivity(new Intent(in_Context, GamePageActivity.class));
            return true;
        }
        else
        {
            return super.onOptionsItemSelected(in_MenuItem);
        }
    }
}
