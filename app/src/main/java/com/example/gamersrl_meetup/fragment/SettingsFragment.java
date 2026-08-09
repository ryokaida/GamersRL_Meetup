package com.example.gamersrl_meetup.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.gamersrl_meetup.R;
import com.example.gamersrl_meetup.utility.SharedPreferencesHelper;

/**
 * SettingsFragment class
 *
 * Displays the Settings page.
 * The user can save their whether the app should be in dark mode or light mode to the Shared Services.
 */
public class SettingsFragment extends Fragment implements View.OnClickListener
{
    // Initialize the UI Elements
    private Button mLightModeButton, mDarkModeButton, mSaveButton, mClearButton;

    // Initialize Shared Preferences Helper [56] [57] [58]
    SharedPreferencesHelper sharedPreferencesHelper;

    /**
     * Inflate the layout for the Settings Fragment.
     *
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return The Settings Fragment view
     */
    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this Fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        // Instantiate the Shared Preferences Helper and determine the appearance mode to use [56] [57] [58]
        sharedPreferencesHelper = new SharedPreferencesHelper(this.getContext());

        // Instantiate the UI Elements
        mLightModeButton = view.findViewById(R.id.lightmode_button);
        mDarkModeButton = view.findViewById(R.id.darkmode_button);
        mSaveButton = view.findViewById(R.id.save_button);
        mClearButton = view.findViewById(R.id.clear_button);

        // Set OnClick Listeners on the buttons
        mLightModeButton.setOnClickListener(this);
        mDarkModeButton.setOnClickListener(this);
        mSaveButton.setOnClickListener(this);
        mClearButton.setOnClickListener(this);

        return view;
    }

    /**
     * Handle the user's button clicks.
     * Use the host activity as the context [41]
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v)
    {
        // Get the ID of the button that was clicked
        int id = v.getId();

        /**
         * If the basic user clicked the Light Mode button, then switch the page to light mode and set isDarkMode to FALSE for saving
         * If the basic user clicked the Dark Mode button, then switch the page to dark mode and set isDarkMode to TRUE for saving
         * If the user clicked the Save button, then save the mode to the Shared Preferences.
         */
        if (id == R.id.lightmode_button)
        {
            sharedPreferencesHelper.saveAppearanceToSharedPreferences(false);
        }
        else if (id == R.id.darkmode_button)
        {
            sharedPreferencesHelper.saveAppearanceToSharedPreferences(true);
        }
        else if (id == R.id.save_button)
        {
            sharedPreferencesHelper.setAppearanceFromSharedPreferences();
        }
        else if (id == R.id.clear_button)
        {
            sharedPreferencesHelper.clearSharedPreferences();
        }

        //sharedPreferencesHelper.setAppearanceFromSharedPreferences();
    }
}