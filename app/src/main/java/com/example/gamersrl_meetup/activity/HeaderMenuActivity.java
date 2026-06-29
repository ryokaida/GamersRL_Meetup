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
//    HeaderMenuActivity headerMenuActivity;
//
//
//    // Constructor
//    // Source for syntax: https://www.geeksforgeeks.org/java/constructors-in-java/
//    HeaderMenuActivity()
//    {
//    }


    /**
     * Create the view.
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected  void onCreate(Bundle savedInstanceState)
    {
        Log.d(strLogTag, (strLogTag + "creating view"));
        // Set the view
        super.onCreate(savedInstanceState);
        setContentView(R.layout.header_menu_layout);
    }


    /**
     * Inflate the Header Menu resource.
     *
     * Sources:
     * https://www.geeksforgeeks.org/android/how-to-implement-options-menu-in-android/
     * https://developer.android.com/develop/ui/views/components/menus#java
     * https://developer.android.com/guide/topics/resources/menu-resource#java
     *
     * @param menu The options menu in which you place your items.
     *
     * @return That the menu was inflated
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.header_menu, menu);
        return true;
    }


    /**
     * Wrapper method to handle Header Menu selection while overriding onOptionsItemSelected().
     *
     * Sources:
     * https://www.geeksforgeeks.org/android/how-to-implement-options-menu-in-android/
     * https://developer.android.com/develop/ui/views/components/menus#java
     * https://developer.android.com/guide/topics/resources/menu-resource#java\
     *
     * @param item The menu item that was selected.
     * @return The result of selecting an item
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getItemId()==R.id.main_activity)
        {
            Toast.makeText(HeaderMenuActivity.this, "bees", Toast.LENGTH_SHORT).show();
            return true;
        }
        else if (item.getItemId()==R.id.game_page)
        {
            // Create Intent for the Game Page and start it
            // Source: https://www.geeksforgeeks.org/android/creating-multiple-screen-applications-in-android/
            // Intent GamePageIntent = new Intent(in_Context, GamePageActivity.class);
            //startActivity(GamePageIntent);
            startActivity(new Intent(HeaderMenuActivity.this, GamePageActivity.class));
            return true;
        }
        else
        {
            return super.onOptionsItemSelected(item);
        }
    }
}
