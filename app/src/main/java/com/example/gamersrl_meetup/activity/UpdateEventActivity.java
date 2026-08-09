package com.example.gamersrl_meetup.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gamersrl_meetup.R;
import com.example.gamersrl_meetup.database.DatabaseHelper_Event;
import com.example.gamersrl_meetup.model.Event;

/**
 * UpdateEventActivity class
 *
 * Allows the owner of an Event to update
 * the Event's information.
 */
public class UpdateEventActivity extends AppCompatActivity
{
    // Initialize the UI elements
    private EditText mTitleEditText;
    private EditText mDescriptionEditText;
    private EditText mAddressEditText;
    private EditText mCityEditText;
    private EditText mStateEditText;

    private Button mUpdateButton;
    private Button mCancelButton;

    // Initialize the selected Event
    private Event mEvent;

    // Initialize the database helper
    private DatabaseHelper_Event dbHelper;

    /**
     * Create the Update Event page.
     *
     * @param savedInstanceState The previously saved Activity state,
     *                           if one exists.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_update_event
        );

        // Instantiate the UI elements
        mTitleEditText =
                findViewById(
                        R.id.update_event_title_edittext
                );

        mDescriptionEditText =
                findViewById(
                        R.id.update_event_description_edittext
                );

        mAddressEditText =
                findViewById(
                        R.id.update_event_address_edittext
                );

        mCityEditText =
                findViewById(
                        R.id.update_event_city_edittext
                );

        mStateEditText =
                findViewById(
                        R.id.update_event_state_edittext
                );

        mUpdateButton =
                findViewById(
                        R.id.update_event_save_button
                );

        mCancelButton =
                findViewById(
                        R.id.update_event_cancel_button
                );

        // Create the database helper
        dbHelper =
                new DatabaseHelper_Event(this);

        // Retrieve the Event passed from EventDetailsActivity
        mEvent =
                getIntent()
                        .getParcelableExtra(
                                "eventToUpdate"
                        );

        if (mEvent == null)
        {
            Toast.makeText(
                    this,
                    "Unable to load Event.",
                    Toast.LENGTH_SHORT
            ).show();

            finish();
            return;
        }

        // Populate the form with the current Event information
        mTitleEditText.setText(
                mEvent.getTitle()
        );

        mDescriptionEditText.setText(
                mEvent.getDescription()
        );

        mAddressEditText.setText(
                mEvent.getAddress()
        );

        mCityEditText.setText(
                mEvent.getCity()
        );

        mStateEditText.setText(
                mEvent.getState()
        );

        // Save the updated Event
        mUpdateButton.setOnClickListener(v ->
        {
            updateEvent();
        });

        // Cancel without changing the Event
        mCancelButton.setOnClickListener(v ->
        {
            finish();
        });
    }

    /**
     * Validate the form and update the Event
     * in the SQLite database.
     */
    private void updateEvent()
    {
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

        // Make sure all Event fields are populated
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

        // Update the Event in SQLite
        dbHelper.updateEvent(
                String.valueOf(mEvent.getId()),
                title,
                description,
                address,
                city,
                state
        );

        Toast.makeText(
                this,
                "Event updated.",
                Toast.LENGTH_SHORT
        ).show();

        /**
         * Close both UpdateEventActivity and the old
         * EventDetailsActivity so the user returns
         * to the refreshed Events list.
         */
        finish();
    }
}