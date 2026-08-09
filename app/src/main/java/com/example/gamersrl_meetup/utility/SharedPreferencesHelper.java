package com.example.gamersrl_meetup.utility;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatDelegate;

public class SharedPreferencesHelper
{
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
        editor.putBoolean(DARK_MODE_TAG, isDarkMode);
        editor.commit();
    }

    /**
     * Retrieve the appearance from the Shared Preferences [56] [57] [58].
     */
    public Boolean getAppearanceFromSharedPreferences()
    {
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
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        else
        {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    /**
     * Clear out the Shared Preferences to reset it to default [57].
     * This is more used for testing.
     */
    public void clearSharedPreferences()
    {
        editor.clear();
        editor.commit();
    }
}