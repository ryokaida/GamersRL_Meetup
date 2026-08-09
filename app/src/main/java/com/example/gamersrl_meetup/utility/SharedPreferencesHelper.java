package com.example.gamersrl_meetup.utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.appcompat.app.AppCompatDelegate;

public class SharedPreferencesHelper
{
    // Set up the Log tag [26]
    final String LOG_TAG = "SHARED PREFERENCES HELPER - ";

    private final String DARK_MODE_TAG = "isDarkMode";
    // Initialize the Shared Preferences and its editor [56]
    private SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    /**
     * Construct the Shared Preferences Helper,
     * and actually create the Shared Preferences in the Private Mode and its editor [58]
     * @param context The context to create the Shared Preferences in
     */
    public SharedPreferencesHelper(Context context)
    {
        Log.d(LOG_TAG, "Creating Shared Preferences Helper in Private Mode and its Editor");
        sharedPreferences = context.getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    /**
     * Set whether the app should be in dark mode or not into the Shared Services [56].
     *
     * @param isDarkMode Whether the app should be in dark mode or not
     */
    public void saveAppearanceToSharedPreferences(Boolean isDarkMode)
    {
        Log.d(LOG_TAG, "Saving appearance to Shared Preferences");
        editor.putBoolean(DARK_MODE_TAG, isDarkMode);
        editor.commit();
    }

    /**
     * Retrieve the appearance from the Shared Preferences [56] [57] [58].
     */
    public Boolean getAppearanceFromSharedPreferences()
    {
        Log.d(LOG_TAG, "Retrieving appearance from Shared Preferences");
        return sharedPreferences.getBoolean(DARK_MODE_TAG, false);
    }

    /**
     * Set the app's appearance according to the Shared Preferences [56] [57] [58].
     */
    public void setAppearanceFromSharedPreferences()
    {
        /**
         * If dark mode is set, then set the app to dark mode.
         * Otherwise, set the app to light mode.
         */
        if (getAppearanceFromSharedPreferences())
        {
            Log.d(LOG_TAG, "Setting app to dark mode");
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        else
        {
            Log.d(LOG_TAG, "Setting app to light mode");
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    /**
     * Clear out the Shared Preferences to reset it to default [57].
     * This is more used for testing.
     */
    public void clearSharedPreferences()
    {
        Log.d(LOG_TAG, "Clearing Shared Preferences");
        editor.clear();
        editor.commit();
    }
}