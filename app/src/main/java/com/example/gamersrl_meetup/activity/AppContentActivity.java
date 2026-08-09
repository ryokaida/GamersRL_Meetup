package com.example.gamersrl_meetup.activity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.gamersrl_meetup.R;
import com.example.gamersrl_meetup.fragment.ChatsFragment;
import com.example.gamersrl_meetup.fragment.EventsFragment;
import com.example.gamersrl_meetup.fragment.GamesListFragment;
import com.google.android.material.navigation.NavigationView;
import com.example.gamersrl_meetup.fragment.MapFragment;
import android.content.Intent;
import com.google.firebase.auth.FirebaseAuth;

/**
 * AppContentActivity class
 *
 * Displays the content of the app by hosting them as Fragments.
 */
public class AppContentActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
    // Create reference to the DrawerLayout
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private NavigationView mNavigationView;

    /**
     * Create the MainActivity page and generate the correct Fragment based on what Navigation Menu Item was chosen.
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        // Load the saved instance state and set the main layout
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appcontent);

        // Instantiate the Navigation Drawer and  the NavigationView object
        mDrawerLayout = findViewById(R.id.nav_drawer_layout);
        mNavigationView = findViewById(R.id.nav_view);

        // Set the NavigationItemSelected Listener on the NavigationView object
        mNavigationView.setNavigationItemSelectedListener(this);;

        // Set up the Action Bar Drawer Toggle
        mActionBarDrawerToggle = new ActionBarDrawerToggle(
                this, // The activity/context for the Action Bar Drawer Toggle
                mDrawerLayout, // The Drawer Layout
                R.string.navigation_drawer_open, // String to open
                R.string.navigation_drawer_close // String to close
        );

        // Include the Action Bar Drawer Toggle as the Listener to the Drawer Layout
        mDrawerLayout.addDrawerListener(mActionBarDrawerToggle);
        // The syncState method is used to sync the state of the Navigation Drawer
        mActionBarDrawerToggle.syncState();

        if (getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        /**
         * Set default Fragment to PirateListFragment
         * by making a new PirateListFragment and replacing the FrameLayout in activity_appcontent with it
         */
        navigateToEventsFragment();
        navigateToGamesListFragment();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        if (mActionBarDrawerToggle.onOptionsItemSelected(item))
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Handle when the user selects items in the Navigation Drawer Menu.
     *
     * @param item The selected Navigation Menu Item
     * @return That navigation was successful
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        // Retrieve the chosen Navigation Menu Item
        int id = item.getItemId();

        /**
         * Inflate the correct Fragment based on the chosen Navigation Menu Item
         * If the user selects Events, then open the Events Fragment.
         * If the user selects Games List, then open the Games List Fragment.
         */
        if (id == R.id.nav_events)
        {
            navigateToEventsFragment();
        }
        else if (id == R.id.nav_gameslist)
        {
            navigateToGamesListFragment();
        }
        else if (id == R.id.nav_map)
        {
            navigateToMapFragment();
        }
        else if (id == R.id.nav_chats)
        {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new ChatsFragment())
                    .commit();
        }
        else if (id == R.id.nav_logout)
        {
            logoutUser();
        }

        // Close Navigation Drawer
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

//    /**
//     * Inflate the Menu Items - Add items to the Action Bar if it is present.
//     *
//     * @param menu The options menu in which you place your items.
//     *
//     * @return That inflating the Menu Items was successful
//     */
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu)
//    {
//        getMenuInflater().inflate(R.menu.nav_drawer_items, menu);
//        return true;
//    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        mActionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    /**
     * Helper method to set Fragment to EventsFragment
     * by making a new EventsFragment and replacing the FrameLayout in activity_appcontent with it
     */
    private void navigateToEventsFragment()
    {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new EventsFragment()).commit();
    }

    /**
     * Helper method to set Fragment to GamesListFragment
     * by making a new GamesListFragment and replacing the FrameLayout in activity_appcontent with it
     */
    private void navigateToGamesListFragment()
    {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new GamesListFragment()).commit();
    }

//    Helper method to set Fragment to MapFragment
    private void navigateToMapFragment()
    {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new MapFragment())
                .commit();
    }

    /**
     * Sign out the current Firebase user and return to the start page.
     */
    private void logoutUser()
    {
        FirebaseAuth.getInstance().signOut();

        Intent intent = new Intent(
                AppContentActivity.this,
                MainActivity.class
        );

        /**
         * Clear the activity stack so that the user cannot press
         * the Back button and return to authenticated app content.
         */
        intent.addFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK
        );

        startActivity(intent);
        finish();
    }
}
