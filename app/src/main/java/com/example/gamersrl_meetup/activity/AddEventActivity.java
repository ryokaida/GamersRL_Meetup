package com.example.gamersrl_meetup.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gamersrl_meetup.R;
import com.example.gamersrl_meetup.database.DatabaseHelper_Event;
import com.example.gamersrl_meetup.model.Event;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * AddEventActivity class
 *
 * Allows the current user to create a new Event.
 * The current Firebase user's UID is saved as the
 * owner of the Event.
 */
public class AddEventActivity extends AppCompatActivity
{
    // Initialize the UI elements
    private EditText mTitleEditText;
    private EditText mDescriptionEditText;
    private EditText mAddressEditText;
    private EditText mCityEditText;
    private EditText mStateEditText;

    private Button mCreateButton;
    private Button mCancelButton;

    // Initialize the Event database helper
    private DatabaseHelper_Event dbHelper;

    /**
     * Create the Add Event page.
     *
     * @param savedInstanceState The previously saved Activity state,
     *                           if one exists.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_add_event
        );

        // Instantiate the UI elements
        mTitleEditText =
                findViewById(
                        R.id.event_title_edittext
                );

        mDescriptionEditText =
                findViewById(
                        R.id.event_description_edittext
                );

        mAddressEditText =
                findViewById(
                        R.id.event_address_edittext
                );

        mCityEditText =
                findViewById(
                        R.id.event_city_edittext
                );

        mStateEditText =
                findViewById(
                        R.id.event_state_edittext
                );

        mCreateButton =
                findViewById(
                        R.id.create_event_button
                );

        mCancelButton =
                findViewById(
                        R.id.cancel_event_button
                );

        // Create the Event database helper
        dbHelper =
                new DatabaseHelper_Event(this);

        /**
         * Create the Event when the Create button is clicked.
         */
        mCreateButton.setOnClickListener(v ->
        {
            createEvent();
        });

        /**
         * Return to the Events List without creating
         * an Event when Cancel is clicked.
         */
        mCancelButton.setOnClickListener(v ->
        {
            finish();
        });
    }

    /**
     * Validate the Event form and add the Event
     * to the SQLite database.
     */
    private void createEvent()
    {
        // Retrieve the form values
        String title =
                mTitleEditText
                        .getText()
                        .toString()
                        .trim();

        String description =
                mDescriptionEditText
                        .getText()
                        .toString()
                        .trim();

        String address =
                mAddressEditText
                        .getText()
                        .toString()
                        .trim();

        String city =
                mCityEditText
                        .getText()
                        .toString()
                        .trim();

        String state =
                mStateEditText
                        .getText()
                        .toString()
                        .trim();

        /**
         * Verify that all required Event fields
         * have been entered.
         */
        if (title.isEmpty() ||
                description.isEmpty() ||
                address.isEmpty() ||
                city.isEmpty() ||
                state.isEmpty())
        {
            Toast.makeText(
                    this,
                    "Please complete all Event fields.",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        // Retrieve the currently logged-in Firebase user
        FirebaseUser currentUser =
                FirebaseAuth
                        .getInstance()
                        .getCurrentUser();

        if (currentUser == null)
        {
            Toast.makeText(
                    this,
                    "You must be logged in to create an Event.",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        // Use the Firebase UID to identify the Event owner
        String ownerUid =
                currentUser.getUid();

        // Create the Event object
        Event event =
                new Event(
                        title,
                        description,
                        address,
                        city,
                        state,
                        ownerUid
                );

        // Add the Event to SQLite
        dbHelper.addToDatabase(event);

        Toast.makeText(
                this,
                "Event created.",
                Toast.LENGTH_SHORT
        ).show();

        // Return to the Events List
        finish();
    }
}