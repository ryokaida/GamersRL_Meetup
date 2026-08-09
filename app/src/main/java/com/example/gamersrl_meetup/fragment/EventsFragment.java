package com.example.gamersrl_meetup.fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gamersrl_meetup.R;
import com.example.gamersrl_meetup.activity.AddEventActivity;
import com.example.gamersrl_meetup.adapter.Adapter_Event;
import com.example.gamersrl_meetup.database.DatabaseHelper_Event;
import com.example.gamersrl_meetup.model.Event;

import java.util.ArrayList;
import java.util.List;

/**
 * EventsFragment class
 *
 * Displays a RecyclerView containing Events.
 * Users can view Events, create new Events, filter Events
 * by city, and reset the Event list.
 */
public class EventsFragment extends Fragment
{
    // Set up the Log tag
    private final String LOG_TAG = "EVENTS FRAGMENT - ";

    // Initialize the UI elements
    private TextView mHeader;
    private RecyclerView mRecyclerView;
    private Button mFilterButton;
    private Button mResetButton;
    private Button mCreateEventButton;

    // Initialize the RecyclerView components
    private LinearLayoutManager layoutManager;
    private Adapter_Event adapter;

    // Initialize the database helper
    private DatabaseHelper_Event dbHelper;

    // Create a list of Events that the page can use
    private List<Event> mEvents;

    // Default option for the location filter
    private final String DEFAULT_FILTER_OPTION = "---";

    /**
     * Inflate the layout for the Events Fragment.
     *
     * @param inflater The LayoutInflater object that can be used to inflate
     *                 Views in the Fragment.
     * @param container If non-null, this is the parent View that the
     *                  Fragment's UI should be attached to.
     * @param savedInstanceState If non-null, this Fragment is being
     *                           reconstructed from a previous saved state.
     *
     * @return The Events Fragment view.
     */
    @NonNull
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState)
    {
        // Inflate the same list layout used by Games
        Log.d(LOG_TAG, "Creating Events page view");

        View view = inflater.inflate(
                R.layout.list_page_layout,
                container,
                false
        );

        // Instantiate the UI elements
        mRecyclerView = view.findViewById(R.id.recycler_view);
        mHeader = view.findViewById(R.id.list_header);
        mFilterButton = view.findViewById(R.id.filter_button);
        mResetButton = view.findViewById(R.id.reset_button);

        // Set the page title
        mHeader.setText("Events");

        // Create the Event database helper
        dbHelper = new DatabaseHelper_Event(requireContext());

        /**
         * If the Event table is empty, populate it with
         * demonstration Events.
         */
        if (dbHelper.isTableEmpty("event"))
        {
            Log.d(
                    LOG_TAG,
                    "Event table is empty. Populating database."
            );

            dbHelper.populateDatabase();
        }

        // Retrieve all Events
        mEvents = dbHelper.getAllEvents();

        // Make a LinearLayoutManager to display Events vertically
        layoutManager = new LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.VERTICAL,
                false
        );

        mRecyclerView.setLayoutManager(layoutManager);

        // Create and assign the Event adapter
        adapter = new Adapter_Event(mEvents);
        mRecyclerView.setAdapter(adapter);

        // Create the Create Event button
        createAddEventButton(view);

        /**
         * Open the Event filter when the Filter button
         * is clicked.
         */
        mFilterButton.setOnClickListener(v ->
        {
            showFilterDialog();
        });

        /**
         * Reset the list to show every Event when the
         * Reset button is clicked.
         */
        mResetButton.setOnClickListener(v ->
        {
            Log.d(
                    LOG_TAG,
                    "Resetting Events list"
            );

            mEvents = dbHelper.getAllEvents();
            updateRecyclerView(mEvents);

            Toast.makeText(
                    requireContext(),
                    "Event filters reset",
                    Toast.LENGTH_SHORT
            ).show();
        });

        return view;
    }

    /**
     * Create the button used to navigate to the
     * Create Event page.
     *
     * @param view The current Fragment View.
     */
    private void createAddEventButton(View view)
    {
        // Retrieve the bottom region from the shared list page
        LinearLayout bottomRegion =
                view.findViewById(
                        R.id.layout_horizontal_bottom
                );

        // Create the button
        mCreateEventButton =
                new Button(requireContext());

        // Set button text
        mCreateEventButton.setText("CREATE EVENT");

        // Set button appearance
        mCreateEventButton.setElevation(20.0F);
        mCreateEventButton.setGravity(Gravity.CENTER);
        mCreateEventButton.setBackgroundColor(
                requireContext()
                        .getColor(R.color.colorPrimary)
        );
        mCreateEventButton.setTextColor(
                requireContext()
                        .getColor(R.color.textColor)
        );

        // Set the button dimensions and margins
        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

        params.setMargins(
                60,
                20,
                60,
                0
        );

        mCreateEventButton.setLayoutParams(params);

        // Add the button to the shared bottom region
        bottomRegion.addView(mCreateEventButton);

        /**
         * Navigate to the Create Event page.
         */
        mCreateEventButton.setOnClickListener(v ->
        {
            Intent intent = new Intent(
                    requireContext(),
                    AddEventActivity.class
            );

            startActivity(intent);
        });
    }

    /**
     * Display a simple filter dialog that allows the
     * user to filter Events by city.
     */
    private void showFilterDialog()
    {
        Log.d(
                LOG_TAG,
                "Opening Event location filter"
        );

        // Create a vertical layout for the dialog
        LinearLayout dialogLayout =
                new LinearLayout(requireContext());

        dialogLayout.setOrientation(
                LinearLayout.VERTICAL
        );

        dialogLayout.setPadding(
                50,
                30,
                50,
                20
        );

        // Create the City label
        TextView cityLabel =
                new TextView(requireContext());

        cityLabel.setText("Location");
        cityLabel.setTextSize(18);

        dialogLayout.addView(cityLabel);

        // Create the City Spinner
        Spinner citySpinner =
                new Spinner(requireContext());

        dialogLayout.addView(citySpinner);

        // Populate the City filter options
        populateCitySpinner(citySpinner);

        /**
         * Build the Filter dialog.
         */
        AlertDialog dialog =
                new AlertDialog.Builder(requireContext())
                        .setTitle("Filter Events")
                        .setView(dialogLayout)
                        .setPositiveButton(
                                "APPLY",
                                null
                        )
                        .setNegativeButton(
                                "CANCEL",
                                (dialogInterface, which) ->
                                {
                                    dialogInterface.dismiss();
                                }
                        )
                        .create();

        /**
         * Override the Apply button behavior so the
         * dialog stays open if no filter was selected.
         */
        dialog.setOnShowListener(dialogInterface ->
        {
            Button applyButton =
                    dialog.getButton(
                            AlertDialog.BUTTON_POSITIVE
                    );

            applyButton.setOnClickListener(v ->
            {
                String selectedCity =
                        citySpinner
                                .getSelectedItem()
                                .toString();

                if (selectedCity.equals(
                        DEFAULT_FILTER_OPTION))
                {
                    Toast.makeText(
                            requireContext(),
                            "Select a location first.",
                            Toast.LENGTH_SHORT
                    ).show();

                    return;
                }

                // Retrieve Events matching the selected city
                mEvents =
                        dbHelper.getEventsByCity(
                                selectedCity
                        );

                // Refresh the Event list
                updateRecyclerView(mEvents);

                Log.d(
                        LOG_TAG,
                        "Filtered Events by city: " +
                                selectedCity
                );

                dialog.dismiss();
            });
        });

        dialog.show();
    }

    /**
     * Populate the location Spinner using cities that
     * currently exist in the Event database.
     *
     * @param citySpinner The Spinner to populate.
     */
    private void populateCitySpinner(
            Spinner citySpinner)
    {
        // Create the list used by the Spinner
        List<String> cities =
                new ArrayList<>();

        // Add the default blank option
        cities.add(DEFAULT_FILTER_OPTION);

        // Retrieve all Events
        List<Event> allEvents =
                dbHelper.getAllEvents();

        /**
         * Add each unique Event city to the Spinner.
         */
        for (Event event : allEvents)
        {
            String city = event.getCity();

            if (city != null &&
                    !city.isEmpty() &&
                    !cities.contains(city))
            {
                cities.add(city);
            }
        }

        // Create the Spinner adapter
        ArrayAdapter<String> spinnerAdapter =
                new ArrayAdapter<>(
                        requireContext(),
                        android.R.layout.simple_spinner_item,
                        cities
                );

        spinnerAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        citySpinner.setAdapter(spinnerAdapter);
    }

    /**
     * Refresh the RecyclerView with the current
     * Event list.
     *
     * @param events The Events to display.
     */
    private void updateRecyclerView(
            @Nullable List<Event> events)
    {
        Log.d(
                LOG_TAG,
                "Refreshing Events RecyclerView"
        );

        adapter = new Adapter_Event(events);
        mRecyclerView.setAdapter(adapter);
    }

    /**
     * Refresh the Events list when returning to this
     * Fragment from another page.
     */
    @Override
    public void onResume()
    {
        super.onResume();

        /**
         * onResume can run before onCreateView has
         * initialized the database helper.
         */
        if (dbHelper != null &&
                mRecyclerView != null)
        {
            mEvents = dbHelper.getAllEvents();
            updateRecyclerView(mEvents);
        }
    }
}