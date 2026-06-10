package com.example.gamersrl_meetup;

import androidx.appcompat.app.AppCompatActivity;
import com.example.gamersrl_meetup.HeaderMenuActivity;
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
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
{
    private EditText mStartPageEditText;
    private Button mStartPageButton;
    private Menu mNavMenu;
    private MenuItem mOpenNavMenu;

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


            }
        });
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
        HeaderMenuActivity headerMenu = new HeaderMenuActivity();
        Log.d(strLogTag, strLogTag + headerMenu.toString());
        return headerMenu.handleHeaderMenuSelection(MainActivity.this, item);
    }

}
