package com.example.gamersrl_meetup.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gamersrl_meetup.R;
import com.example.gamersrl_meetup.database.DatabaseHelper_Event;
import com.example.gamersrl_meetup.model.Event;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import android.location.Address;
import android.location.Geocoder;
import android.os.Build;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

/**
 * EventDetailsActivity class
 *
 * Displays the details for the selected Event.
 * Users can join Events.
 * Event owners can update or delete their own Events.
 */
public class EventDetailsActivity extends AppCompatActivity
{
    // Initialize the UI elements
    private TextView mTitle;
    private TextView mDescription;
    private TextView mAddress;
    private TextView mCityState;

    private Button mJoinButton;
    private Button mUpdateButton;
    private Button mDeleteButton;
    private Button mBackButton;

    // Initialize the selected Event
    private Event mEvent;

    // Initialize the database helper
    private DatabaseHelper_Event dbHelper;
    private boolean isJoined;

    /**
     * Create the Event Details page.
     *
     * @param savedInstanceState The previously saved Activity state,
     *                           if one exists.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_event_details
        );

        // Instantiate the UI elements
        mTitle =
                findViewById(
                        R.id.event_details_title
                );

        mDescription =
                findViewById(
                        R.id.event_details_description
                );

        mAddress =
                findViewById(
                        R.id.event_details_address
                );

        mCityState =
                findViewById(
                        R.id.event_details_city_state
                );

        mJoinButton =
                findViewById(
                        R.id.join_event_button
                );

        mUpdateButton =
                findViewById(
                        R.id.update_event_button
                );

        mDeleteButton =
                findViewById(
                        R.id.delete_event_button
                );

        mBackButton =
                findViewById(
                        R.id.back_button
                );

        // Create the Event database helper
        dbHelper =
                new DatabaseHelper_Event(this);

        // Retrieve the selected Event from the Intent
        mEvent =
                getIntent()
                        .getParcelableExtra(
                                "itemDetails"
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

        // Display the Event information
        mTitle.setText(
                mEvent.getTitle()
        );

        mDescription.setText(
                mEvent.getDescription()
        );

        mAddress.setText(
                mEvent.getAddress()
        );

        mCityState.setText(
                mEvent.getCity() +
                        ", " +
                        mEvent.getState()
        );

        // Set up the Event location map
        setupEventMap();

        // Configure the page based on Event ownership
        setupOwnerControls();

/**
 * Join or leave the Event depending on
 * the current membership state.
 */
        mJoinButton.setOnClickListener(v ->
        {
            FirebaseUser currentUser =
                    FirebaseAuth
                            .getInstance()
                            .getCurrentUser();

            if (currentUser == null)
            {
                return;
            }

            String userUid =
                    currentUser.getUid();

            if (isJoined)
            {
                // Leave the Event
                dbHelper.leaveEvent(
                        mEvent.getId(),
                        userUid
                );

                isJoined = false;

                Toast.makeText(
                        EventDetailsActivity.this,
                        "You left " +
                                mEvent.getTitle() +
                                ".",
                        Toast.LENGTH_SHORT
                ).show();
            }
            else
            {
                // Join the Event
                dbHelper.joinEvent(
                        mEvent.getId(),
                        userUid
                );

                isJoined = true;

                Toast.makeText(
                        EventDetailsActivity.this,
                        "You joined " +
                                mEvent.getTitle() +
                                "!",
                        Toast.LENGTH_SHORT
                ).show();
            }

            // Refresh the button text
            updateJoinButton();
        });

        /**
         * Navigate to the Update Event page.
         */
        mUpdateButton.setOnClickListener(v ->
        {
            Intent intent =
                    new Intent(
                            EventDetailsActivity.this,
                            UpdateEventActivity.class
                    );

            intent.putExtra(
                    "eventToUpdate",
                    mEvent
            );

            startActivity(intent);
        });

        /**
         * Delete the Event if the current user owns it.
         */
        mDeleteButton.setOnClickListener(v ->
        {
            dbHelper.deleteEvent(
                    String.valueOf(
                            mEvent.getId()
                    )
            );

            Toast.makeText(
                    EventDetailsActivity.this,
                    "Event deleted.",
                    Toast.LENGTH_SHORT
            ).show();

            finish();
        });

        /**
         * Return to the previous page.
         */
        mBackButton.setOnClickListener(v ->
        {
            finish();
        });
    }

    /**
     * Show Update/Delete for Event owners.
     * Show Join/Leave controls for all other users.
     */
    private void setupOwnerControls()
    {
        FirebaseUser currentUser =
                FirebaseAuth
                        .getInstance()
                        .getCurrentUser();

        if (currentUser == null)
        {
            return;
        }

        boolean isOwner =
                currentUser
                        .getUid()
                        .equals(
                                mEvent.getOwnerUid()
                        );

        if (isOwner)
        {
            // Owners can Update/Delete
            mUpdateButton.setVisibility(
                    View.VISIBLE
            );

            mDeleteButton.setVisibility(
                    View.VISIBLE
            );

            // Owners do not Join their own Event
            mJoinButton.setVisibility(
                    View.GONE
            );
        }
        else
        {
            // Non-owners can Join or Leave
            mJoinButton.setVisibility(
                    View.VISIBLE
            );

            // Non-owners cannot Update/Delete
            mUpdateButton.setVisibility(
                    View.GONE
            );

            mDeleteButton.setVisibility(
                    View.GONE
            );

            // Determine whether the user already joined
            isJoined =
                    dbHelper.isUserJoined(
                            mEvent.getId(),
                            currentUser.getUid()
                    );

            updateJoinButton();
        }
    }

    /**
     * Update the Join button text based on the
     * current membership state.
     */
    private void updateJoinButton()
    {
        if (isJoined)
        {
            mJoinButton.setText(
                    "LEAVE EVENT"
            );
        }
        else
        {
            mJoinButton.setText(
                    "JOIN EVENT"
            );
        }
    }

    /**
     * Set up the Google Map used to display
     * the Event location.
     */
    private void setupEventMap()
    {
        SupportMapFragment mapFragment =
                (SupportMapFragment)
                        getSupportFragmentManager()
                                .findFragmentById(
                                        R.id.event_map_container
                                );

        if (mapFragment != null)
        {
            mapFragment.getMapAsync(googleMap ->
            {
                showEventLocation(googleMap);
            });
        }
    }

    /**
     * Convert the Event's address into coordinates and
     * display the location on the Google Map.
     *
     * @param googleMap The Google Map to update.
     */
    private void showEventLocation(GoogleMap googleMap)
    {
        String fullAddress =
                mEvent.getAddress() +
                        ", " +
                        mEvent.getCity() +
                        ", " +
                        mEvent.getState();

        Geocoder geocoder =
                new Geocoder(this);

        /**
         * Android 13 and newer provide an asynchronous
         * Geocoder method.
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
        {
            geocoder.getFromLocationName(
                    fullAddress,
                    1,
                    addresses ->
                    {
                        if (addresses != null &&
                                !addresses.isEmpty())
                        {
                            Address address =
                                    addresses.get(0);

                            runOnUiThread(() ->
                            {
                                displayMapLocation(
                                        googleMap,
                                        address
                                );
                            });
                        }
                    }
            );
        }
        else
        {
            /**
             * Older Android versions use the synchronous
             * Geocoder method, so run it on a background thread.
             */
            new Thread(() ->
            {
                try
                {
                    List<Address> addresses =
                            geocoder.getFromLocationName(
                                    fullAddress,
                                    1
                            );

                    if (addresses != null &&
                            !addresses.isEmpty())
                    {
                        Address address =
                                addresses.get(0);

                        runOnUiThread(() ->
                        {
                            displayMapLocation(
                                    googleMap,
                                    address
                            );
                        });
                    }
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }).start();
        }
    }

    /**
     * Place a marker on the Event location and
     * move the map camera to it.
     *
     * @param googleMap The Google Map.
     * @param address The geocoded Event address.
     */
    private void displayMapLocation(
            GoogleMap googleMap,
            Address address)
    {
        LatLng eventLocation =
                new LatLng(
                        address.getLatitude(),
                        address.getLongitude()
                );

        // Remove any previous markers
        googleMap.clear();

        // Add the Event marker
        googleMap.addMarker(
                new MarkerOptions()
                        .position(eventLocation)
                        .title(mEvent.getTitle())
        );

        // Move the camera to the Event location
        googleMap.moveCamera(
                CameraUpdateFactory.newLatLngZoom(
                        eventLocation,
                        14f
                )
        );
    }
}