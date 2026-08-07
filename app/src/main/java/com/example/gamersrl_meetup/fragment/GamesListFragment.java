package com.example.gamersrl_meetup.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gamersrl_meetup.R;
import com.example.gamersrl_meetup.activity.AddGameRequestActivity;
import com.example.gamersrl_meetup.activity.TempAdminTester;
import com.example.gamersrl_meetup.adapter.Adapter_Game;
import com.example.gamersrl_meetup.database.DatabaseHelper_Game;
import com.example.gamersrl_meetup.model.Game;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

/**
 * GamesListFragment class
 *
 * Displays a RecyclerView with Games.
 * The user can select a Game to go to its details page by clicking its View button.
 */
public class GamesListFragment extends Fragment implements View.OnClickListener
{
    // Set up the Log tag [26]
    private final String LOG_TAG = "GAMES LIST ACTIVITY - ";

    // Initialize the UI elements
    private TextView mHeader, mFilterHeader;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager layoutManager;
    private Button mAddGameRequestButton, mFilterButton, mResetButton;

    // Initialize variables for the Approved attribute and Number of Players Spinners [12]
    private Spinner mSpinnerApproved, mSpinnerMinPlayers, mSpinnerMaxPlayers;
    private String FILTER_OPTION_APPROVED = "option_selected";
    private final String DEFAULT_SPINNER_OPTION = "---";
    private LinearLayout mGroupForNumberOfPlayersSPinners;
    private String FILTER_OPTION_MINPLAYERS = "option_selected";
    private String FILTER_OPTION_MAXPLAYERS = "option_selected";
    private final String MIN_PLAYERS_QUERY_CLAUSE = "<minPlayersClause>";
    private final String MAX_PLAYERS_QUERY_CLAUSE = "<maxPlayersClause>";

    // Initialize the database helper and the adapter
    private DatabaseHelper_Game dbHelper;
    private Adapter_Game adapter;

    // Initialize the boolean to determine the user's role
    private boolean isAdmin;

    /**
     * Inflate the layout for the Games List Fragment.
     *
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return The Games List Fragment view
     */
    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this Fragment
        View view = inflater.inflate(R.layout.list_page_layout, container, false);

        // TODO - Actually get admin role from user
        isAdmin = new TempAdminTester().getIsAdmin();

        /**
         * Instantiate the RecyclerView, the dividers indicating the start and end the RecyclerView, and the Button.
         * Use the hosting activity as the context.
         */
        Log.d(LOG_TAG, "Instantiating UI elements for the Games List page");
        mRecyclerView = view.findViewById(R.id.recycler_view);

        // Instantiate the header and set its text
        mHeader = view.findViewById(R.id.list_header);
        mHeader.setText(R.string.frag_gameslist_header);

        // Initialize UI elements for filtering
        mFilterHeader = view.findViewById(R.id.filter_header);
        mSpinnerApproved = view.findViewById(R.id.spinner_filter_approved); // For filtering by Approved attribute
        mResetButton = view.findViewById(R.id.reset_button);
        mFilterButton = view.findViewById(R.id.filter_button);
        mGroupForNumberOfPlayersSPinners = view.findViewById(R.id.number_of_players_filters); // For filtering by Number of Players
        mSpinnerMinPlayers = view.findViewById(R.id.spinner_filter_minplayers); // For filtering by Number of Players
        mSpinnerMaxPlayers = view.findViewById(R.id.spinner_filter_maxplayers); // For filtering by Number of Players

        // Set the OnClick Listener for the filter buttons
        mFilterButton.setOnClickListener(this);
        mResetButton.setOnClickListener(this);

        // Make a Database Helper to manipulate the database
        Log.d(LOG_TAG, "Making database helper");
        dbHelper = new DatabaseHelper_Game(this.getContext());

        // Make an empty list of Games
        List<Game> games;

        /**
         * If the database is empty, then populate it.
         * Otherwise, do nothing.
         *
         * Retrieve the table name from the adapter since the isTableEmpty() method is actually called from the abstract DatabaseHelper class,
         * and the abstract class needs to know what table to check.
         */
        if (dbHelper.isTableEmpty("game"))
        {
            Log.d(LOG_TAG, "Game table is empty");
            dbHelper.populateDatabase();
        }
        else
        {
            Log.d(LOG_TAG, "Game table is populated");
        }

        /**
         * If the user is an admin, then show the unapproved games so that they can be approved.
         * Otherwise, only show the approved games and the button to request that a new game be added.
         */
        if (isAdmin)
        {
            // Get all the items for the list
            Log.d(LOG_TAG, "Populating list with all unapproved games");
            //games = dbHelper.getAllUnapprovedGames();
            games = dbHelper.getAllGames();

            Log.d(LOG_TAG, "Creating Approved attribute filter spinner");
            // Show the spinner to filter by the Approved attribute
            mFilterHeader.setText(R.string.activity_gamedetails_filter_approved_header);
            mSpinnerApproved.setVisibility(View.VISIBLE);
            // Hide the Number of Players spinners
            mGroupForNumberOfPlayersSPinners.setVisibility(View.GONE);
        }
        else
        {
            // Get all the items for the list
            Log.d(LOG_TAG, "Populating list with only approved games");
            games = dbHelper.getAllApprovedGames();

            // Add the Request to Add Game button to the bottom of the screen
            createAddGameRequestButton(view);

            Log.d(LOG_TAG, "Creating Number of Players filter spinners");
            // Show the spinner to filter by the Number of Players
            mFilterHeader.setText(R.string.activity_gamedetails_filter_numberofplayers_header);
            mGroupForNumberOfPlayersSPinners.setVisibility(View.VISIBLE);
            // Hide the Approved attribute spinners
            mSpinnerApproved.setVisibility(View.GONE);
        }

        // Make the adapter and set it onto the RecyclerView
        Log.d(LOG_TAG, "Making adapter");
        adapter = new Adapter_Game(games);
        mRecyclerView.setAdapter(adapter);

        // Make a LinearLayoutManager to draw the objects and set it onto the RecyclerView
        Log.d(LOG_TAG, "Making linear layout manager");
        layoutManager = new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);

        return view;
    }

    /**
     * Set up the Request to Add Game button and add it to the page [27] [28] [29] [30] [31] [32] [33] [34].
     * Use the incoming view as the context to find the Views by ID.
     * @param v The view to use to set up the UI elements
     */
    private void createAddGameRequestButton(View v)
    {
        Log.d(LOG_TAG, "Creating Add Game Request button");
        // Assign the bottom region LinearLayout as a variable so the button can be added to the page [27] [28] [29] [210] [31] [32] [33].
        LinearLayout bottomRegion = v.findViewById(R.id.layout_horizontal_bottom);
        // Create the new button
        mAddGameRequestButton = new Button(this.getContext());
        // Set the ID of the button [42] [43]
        mAddGameRequestButton.setId(R.id.addgamerequest_button);
        // Set the text, elevation, gravity, background color, and text color of the button
        Log.d(LOG_TAG, "Setting attributes for Add Game Request button");
        mAddGameRequestButton.setText(R.string.frag_gameslist_addgamerequest_button_text);
        mAddGameRequestButton.setElevation(20.0F);
        mAddGameRequestButton.setGravity(Gravity.CENTER);
        mAddGameRequestButton.setBackgroundColor(v.getContext().getColor(R.color.purple_500)); // Use the View's context to get the color
        mAddGameRequestButton.setTextColor(v.getContext().getColor(R.color.white)); // Use the View's context to get the color
        // Make a new LayoutParams to set the button width to MATCH_PARENT and WRAP_CONTENT and to set the margins
        Log.d(LOG_TAG, "Setting Layout Params for Add Game Request button");
        LinearLayout.LayoutParams paramsForButton = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        // Set the margins
        paramsForButton.setMargins(60, 100, 60, 0);
        // Assign the Layout Params to the button
        mAddGameRequestButton.setLayoutParams(paramsForButton);
        // Add the button to the page
        Log.d(LOG_TAG, "Adding the Add Game Request button to the page");
        bottomRegion.addView(mAddGameRequestButton);

        // Set OnClick Listener on the Add Game Request button.
        mAddGameRequestButton.setOnClickListener(this);
    }

    /**
     * Navigate to the Add Game Request page when the Add Game Request button is clicked.
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
         * If the basic user clicked the Add Game Request button, then navigate to the Add Game Request page.
         * If the user clicked the filter button, then handle the Approved/Number of Players filters based on user role.
         * If the user clicked the reset button, then reset the list of Games based on user role.
         */
        if (id == R.id.addgamerequest_button) {
            Log.d(LOG_TAG, "Navigating to Add Game Request page");
            Intent intent = new Intent(getActivity(), AddGameRequestActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.filter_button)
        {
            /**
             * If the user is an admin, then filter based on Approved status.
             * Otherwise, filter based on Number of Players.
             */
            if (isAdmin)
            {
                handleAdminFilter();
            }
            else
            {
                handleMinMaxPlayersFilter();
            }
        }
        else if (id == R.id.reset_button)
        {
            /**
             * If the user is an admin, then reset the list to all Games.
             * Otherwise, reset the list to only approved Games.
             */
            if (isAdmin)
            {
                updateRecyclerView(dbHelper.getAllGames());
            }
            else
            {
                updateRecyclerView(dbHelper.getAllApprovedGames());
            }
        }
    }

    /**
     * Handle filtering on Approved status by admins.
     */
    private void handleAdminFilter()
    {
        // Retrieve the selected filter option [12]
        FILTER_OPTION_APPROVED = mSpinnerApproved.getSelectedItem().toString();

        /**
         * If the filter option is the default blank one, then inform the user to select a valid option.
         * Otherwise, continue.
         */
        if (FILTER_OPTION_APPROVED.equals(DEFAULT_SPINNER_OPTION))
        {
            showSnackbar(mFilterButton, "Select a valid Approved status!");
        }
        else
        {
            // Make an empty list of Games
            List<Game> games = new ArrayList<>();

            /**
             * Set the list of Games to empty before doing the filter.
             * This is done so that the app shows an empty list if are no Games in a certain category.
             */
            updateRecyclerView(games);

            /** Filter the database by category to find the items that match the selected filter option */
            Log.d(LOG_TAG, "Filtering to get all games with Approved status: " + FILTER_OPTION_APPROVED);
            // Make the query to get data
            String selectQuery = "SELECT * FROM " + dbHelper.getTableName() + " WHERE approved = ?";
            games = dbHelper.getItemsFromDB(selectQuery, new String[]{FILTER_OPTION_APPROVED});

            // Reset the RecyclerView with the resulting list of Games
            updateRecyclerView(games);
        }
    }

    private void handleMinMaxPlayersFilter()
    {
        // Retrieve the selected filter options [12]
        FILTER_OPTION_MINPLAYERS = mSpinnerMinPlayers.getSelectedItem().toString();
        FILTER_OPTION_MAXPLAYERS = mSpinnerMaxPlayers.getSelectedItem().toString();

        /**
         * The logic for retrieving the entered data and validating it is in a TryCatch, so the code can be cleaner (not having a bunch of nested If Statements).
         * Inside the TryCatch, if there is invalid data, an exception is thrown.
         * The exception is then handled by showing a Toast to the user about the error.
         */
        try
        {
            /**
             * Verify that a valid option was selected for the Min Players filter.
             * If the default blank option was selected, then inform the user to select a valid option.
             */
            if (FILTER_OPTION_MINPLAYERS.equals(DEFAULT_SPINNER_OPTION))
            {
                throw new Exception("Select a valid Approved option for Min Players!");
            }

            /**
             * Verify that a valid option was selected for the Max Players filter.
             * If the default blank option was selected, then inform the user to select a valid option.
             */
            if (FILTER_OPTION_MAXPLAYERS.equals(DEFAULT_SPINNER_OPTION))
            {
                throw new Exception("Select a valid Approved option for Max Players!");
            }

            // Remove the "+" from the selected number of players if it is present to make input validations and querying the database easier
            FILTER_OPTION_MINPLAYERS = FILTER_OPTION_MINPLAYERS.replace("+", "");
            FILTER_OPTION_MAXPLAYERS = FILTER_OPTION_MAXPLAYERS.replace("+", "");

            // Initialize Min and Max Players as integers
            int intMinPlayers;
            int intMaxPlayers;

            // Retrieve the selected Min and Max Players as integers
            intMinPlayers = Integer.parseInt(FILTER_OPTION_MINPLAYERS);
            intMaxPlayers = Integer.parseInt(FILTER_OPTION_MAXPLAYERS);

            /**
             * Verify that the number of Min Players is not greater than the number of Max Players.
             * If this is the case, then inform the user of the error.
             */
            if (intMinPlayers > intMaxPlayers)
            {
                throw new Exception("The number of Min Players should not be greater than the number of Max Players!");
            }

            Log.d(LOG_TAG, "Valid options chosen for the Number of Players filters");

            // Make an empty list of Games
            List<Game> games = new ArrayList<>();

            /**
             * Set the list of Games to empty before doing the filter.
             * This is done so that the app shows an empty list if are no Games in a certain category.
             */
            updateRecyclerView(games);

            /** Filter the database by category to find the items that match the selected filter option */
            Log.d(LOG_TAG, "Filtering to get all games with with a Number of Players within the range: " + FILTER_OPTION_MINPLAYERS + "-" + FILTER_OPTION_MAXPLAYERS);

            // Construct the filter query
            String selectQuery = getNumberOfPlayersFilterQuery(intMinPlayers, intMaxPlayers);
            //showSnackbar(mFilterButton, selectQuery);
            // Run the query
            games = dbHelper.getItemsFromDB(selectQuery, new String[]{FILTER_OPTION_MINPLAYERS, FILTER_OPTION_MAXPLAYERS});

            // Reset the RecyclerView with the resulting list of Games
            updateRecyclerView(games);
        }
        catch (Exception e)
        {
            showSnackbar(mFilterButton, e.getMessage());
        }
    }

    /**
     * Helper method to construct the filter query for Number of Players based on what the user selects for the Min/Max Players spinners.
     *
     * @param intMinPlayers
     * @param intMaxPlayers
     * @return The query to filter by Number of Players
     */
    @NonNull
    private String getNumberOfPlayersFilterQuery(int intMinPlayers, int intMaxPlayers)
    {
        // Initialize and start constructing the query
        String selectQuery = "SELECT * FROM " + dbHelper.getTableName() + " WHERE " + MIN_PLAYERS_QUERY_CLAUSE + " AND " + MAX_PLAYERS_QUERY_CLAUSE;

        /**
         * If the user selected the 5+ option, set the query to pull Games where min_players is greater than or equal to the 5.
         * Otherwise, set the query to pull Games where the min_players is equal to the selected number.
         */
        if (intMinPlayers == 5)
        {
            selectQuery = selectQuery.replace(MIN_PLAYERS_QUERY_CLAUSE, "min_players >= ?");
        }
        else
        {
            selectQuery = selectQuery.replace(MIN_PLAYERS_QUERY_CLAUSE, "min_players = ?");
        }

        /**
         * If the user selected the 5+ option, set the query to pull Games where max_players is greater than or equal to the 5.
         * Otherwise, set the query to pull Games where the max_players is equal to the selected number.
         */
        if (intMaxPlayers == 5)
        {
            selectQuery = selectQuery.replace(MAX_PLAYERS_QUERY_CLAUSE, "max_players <= ?");
        }
        else
        {
            selectQuery = selectQuery.replace(MAX_PLAYERS_QUERY_CLAUSE, "max_players = ?");
        }

        return selectQuery;
    }

    /**
     * Update the RecyclerView with the current list of Games.
     * Used to change the list whenever the user filters or resets the list.
     *
     * @param games The list of Games to display
     */
    private void updateRecyclerView(@Nullable List<Game> games)
    {
        adapter = new Adapter_Game(games);
        mRecyclerView.setAdapter(adapter);
    }

    /**
     * Helper method to display a snackbar [38].
     *
     * @param v The View to display the snackbar in
     * @param message The message to display
     */
    private void showSnackbar(View v, String message)
    {
        Snackbar snackbar = Snackbar.make(v, message, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }
}