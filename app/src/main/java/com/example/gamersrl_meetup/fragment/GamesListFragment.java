package com.example.gamersrl_meetup.fragment;

import android.app.Dialog;
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
import com.example.gamersrl_meetup.activity.AddGameRequestActivity;
import com.example.gamersrl_meetup.utility.AdminRoleHelper;
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
    private TextView mHeader;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager layoutManager;
    private Button mAddGameRequestButton, mFilterButton, mResetButton;

    // Initialize the Filter Dialog UI elements [48]
    private Dialog mFilterDialog;
    private Button mFilterDialogOKButton, mFilterDialogCancelButton;

    // Initialize variables for the Filter Spinners [12]
    private Spinner mSpinnerDeveloper, mSpinnerPublisher, mSpinnerMinPlayers, mSpinnerMaxPlayers, mSpinnerApproved;
    private LinearLayout mGroupForApprovedFilter; // Used to turn the visibility of the Approved Filter UI elements on and off
    private final String DEFAULT_SPINNER_OPTION = "---";

    // Initialize the database helper and the adapter
    private DatabaseHelper_Game dbHelper;
    private Adapter_Game adapter;

    // Initialize the boolean to determine the user's role
    private boolean isAdmin;

    // Create a list of Games that the page can use
    private List<Game> mGames;

    /**
     * Inflate the layout for the Games List Fragment.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate
     *                           any views in the fragment,
     * @param container          If non-null, this is the parent view that the fragment's
     *                           UI should be attached to.  The fragment should not add the view itself,
     *                           but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     *                           from a previous saved state as given here.
     * @return The Games List Fragment view
     */
    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        // Inflate the layout for this Fragment
        View view = inflater.inflate(R.layout.list_page_layout, container, false);

        // TODO - Actually get admin role from user
        isAdmin = new AdminRoleHelper().getIsAdmin();

        /**
         * Instantiate the RecyclerView, the dividers indicating the start and end the RecyclerView, and the Button.
         * Use the hosting activity as the context.
         */
        Log.d(LOG_TAG, "Instantiating UI elements for the Games List page");
        mRecyclerView = view.findViewById(R.id.recycler_view);

        // Instantiate the header and set its text
        mHeader = view.findViewById(R.id.list_header);
        mHeader.setText(R.string.frag_gameslist_header);

        // Instantiate Filter/Reset buttons
        mResetButton = view.findViewById(R.id.reset_button);
        mFilterButton = view.findViewById(R.id.filter_button);

        // Set the OnClick Listener for the filter buttons
        mFilterButton.setOnClickListener(this);
        mResetButton.setOnClickListener(this);

        // Make a Database Helper to manipulate the database
        Log.d(LOG_TAG, "Making database helper");
        dbHelper = new DatabaseHelper_Game(this.getContext());

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
            mGames = dbHelper.getAllGames();
        }
        else
        {
            // Get all the items for the list
            Log.d(LOG_TAG, "Populating list with only approved games");
            mGames = dbHelper.getAllApprovedGames();

            // Add the Request to Add Game button to the bottom of the screen
            createAddGameRequestButton(view);
        }

        // Make the adapter and set it onto the RecyclerView
        Log.d(LOG_TAG, "Making adapter");
        adapter = new Adapter_Game(mGames);
        mRecyclerView.setAdapter(adapter);

        // Make a LinearLayoutManager to draw the objects and set it onto the RecyclerView
        Log.d(LOG_TAG, "Making linear layout manager");
        layoutManager = new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);

        // Instantiate the Filter Dialog [48]
        mFilterDialog = new Dialog(this.getContext());

        return view;
    }

    /**
     * Set up the Request to Add Game button and add it to the page [27] [28] [29] [30] [31] [32] [33] [34].
     * Use the incoming view as the context to find the Views by ID.
     *
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
        paramsForButton.setMargins(60, 60, 60, 0);
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
         * If the user clicked the filter button, then show the Filter Dialog.
         * If the user clicked the reset button, then reset the list of Games based on user role.
         */
        if (id == R.id.addgamerequest_button)
        {
            Log.d(LOG_TAG, "Navigating to Add Game Request page");
            Intent intent = new Intent(getActivity(), AddGameRequestActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.filter_button)
        {
            showFilterDialog();
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
        else if (id == R.id.filter_ok_button)
        {
            // Handle filtering and determine if it was successful or not
            boolean successfulFilter;
            successfulFilter = handleFiltering();

            /**
             * If the filter was successful, then dismiss the Filter Dialog.
             * Otherwise, leave it open.
             */
            if (successfulFilter)
            {
                mFilterDialog.dismiss();
            }
        }
        else if (id == R.id.filter_cancel_button)
        {
            // Close the filter [48]
            mFilterDialog.dismiss();
            Toast.makeText(this.getContext(), "Canceled filtering", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Show the Filter Dialog [48].
     */
    private void showFilterDialog()
    {
        // Set up the Filter Dialog and set its attributes
        mFilterDialog.setContentView(R.layout.dialog_game_filters);
        mFilterDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mFilterDialog.setCancelable(false);
        mFilterDialog.getWindow().getAttributes().windowAnimations = R.style.animation;

        // Instantiate the OK and Cancel buttons on the Filter Dialog
        mFilterDialogOKButton = mFilterDialog.findViewById(R.id.filter_ok_button);
        mFilterDialogCancelButton = mFilterDialog.findViewById(R.id.filter_cancel_button);

        // Set OnClick Listeners onto the Filter Dialog buttons
        mFilterDialogOKButton.setOnClickListener(this);
        mFilterDialogCancelButton.setOnClickListener(this);

        // Instantiate the filter dropdowns for Developer, Publisher, Min Players, and Max Players
        mSpinnerDeveloper = mFilterDialog.findViewById(R.id.spinner_filter_developer);
        mSpinnerPublisher = mFilterDialog.findViewById(R.id.spinner_filter_publisher);
        mSpinnerMinPlayers = mFilterDialog.findViewById(R.id.spinner_filter_minplayers);
        mSpinnerMaxPlayers = mFilterDialog.findViewById(R.id.spinner_filter_maxplayers);
        mGroupForApprovedFilter = mFilterDialog.findViewById(R.id.group_for_approved_filter); // Used to turn the visibility of the Approved Filter UI elements on and off
        mSpinnerApproved = mFilterDialog.findViewById(R.id.spinner_filter_approved);

        /**
         * If the user is an admin, then show the Approved Filter.
         * Otherwise, hide it.
         */
        if (isAdmin)
        {
            mGroupForApprovedFilter.setVisibility(View.VISIBLE);
        }
        else
        {
            mGroupForApprovedFilter.setVisibility(View.GONE);
        }

        // Populate the Developer filter
        populateFilter("developer", mSpinnerDeveloper);

        // Populate the Publisher filter
        populateFilter("publisher", mSpinnerPublisher);

        // Show the Filter Dialog
        mFilterDialog.show();
    }

    /**
     * Retrieve the attributes to fill the spinner with from the database,
     * and populate the spinner with them [49] [50] [51].
     *
     * @param filterAttribute   The attribute to populate the spinner with (e.g. Developer, Publisher, etc.)
     * @param spinnerToPopulate The spinner to populate with the attributes
     */
    private void populateFilter(String filterAttribute, Spinner spinnerToPopulate)
    {
        // Make a new List of String for the retrieved attributes
        List<String> attributesForFilter = new ArrayList<>();
        // Add the default Spinner Option as the first option
        attributesForFilter.add(DEFAULT_SPINNER_OPTION);

        // Retrieve all Games from the database
        List<Game> gamesForPopulatingFilter = dbHelper.getAllGames();

        // Iterate through the list of Games from the DB and retrieve the correct attribute for the List of Strings
        for (Game game : gamesForPopulatingFilter)
        {
            /**
             * If the attribute to populate is Developer, then retrieve the Developer from the Games.
             * If the attribute to populate is Developer, then retrieve the Publisher from the Games.
             */
            if (filterAttribute.equals("developer"))
            {
                // Get the Developer from the current Game
                String attributeToAdd = game.getDeveloper();
                /**
                 * If the Developer is not already in the list of attributes for the filter, then add it.
                 * Otherwise, do nothing.
                 */
                if (!attributesForFilter.contains(attributeToAdd))
                {
                    attributesForFilter.add(attributeToAdd);
                }
            } else if (filterAttribute.equals("publisher"))
            {
                // Get the Publisher from the current Game
                String attributeToAdd = game.getPublisher();
                /**
                 * If the Publisher is not already in the list of attributes for the filter, then add it.
                 * Otherwise, do nothing.
                 */
                if (!attributesForFilter.contains(attributeToAdd))
                {
                    attributesForFilter.add(attributeToAdd);
                }
            }
        }

        // Populate the Spinner with the actual values from the database [49] [50] [51]
        ArrayAdapter spinnerAdapter = new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_spinner_item, attributesForFilter);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerToPopulate.setAdapter(spinnerAdapter);
    }

    /**
     * Handle the filtering logic to filter by the Publisher, Developer, Number of Players, and/or Approved status.
     *
     * @return Whether the filtering was successful or not
     */
    private Boolean handleFiltering()
    {
        // Make an empty list of Games
        List<Game> gamesForFiltering = new ArrayList<>();

        // Initialize and start the filter query
        String selectQuery = "SELECT * FROM " + dbHelper.getTableName() + " WHERE ";

        // Initialize the where args for the filter query
        List<String> whereArgsForFilter = new ArrayList<>();

        // Initialize the where clauses for the filter options
        String developerWhereClause, publisherWhereClause, numberOfPlayersWhereClause, approvedWhereClause = "";

        // Initialize result of filtering to FALSE
        boolean successfulFilter = false;

        /**
         * Set the list of Games to empty before doing the filter.
         * This is done so that the app shows an empty list if are no Games in a certain category.
         */
        //updateRecyclerView(games);

        // Construct where clause for Developer filter
        developerWhereClause = filterByOneAttribute(mSpinnerDeveloper, "developer", whereArgsForFilter);
        Log.d(LOG_TAG, "Constructed where clause for developer filter: " + developerWhereClause);

        // Construct where clause for Publisher filter
        publisherWhereClause = filterByOneAttribute(mSpinnerPublisher, "publisher", whereArgsForFilter);
        Log.d(LOG_TAG, "Constructed where clause for publisher filter: " + publisherWhereClause);

        /**
         * Construct where clause for Number of Players filter
         * Use a custom where query building method since the filter for number of players works differently
         */
        numberOfPlayersWhereClause = filterByNumberOfPlayers(whereArgsForFilter);
        Log.d(LOG_TAG, "Constructed where clause for number of players filter: " + numberOfPlayersWhereClause);

        // Construct where clause for Approved filter
        approvedWhereClause = filterByOneAttribute(mSpinnerApproved, "approved", whereArgsForFilter);
        Log.d(LOG_TAG, "Constructed where clause for approved filter: " + approvedWhereClause);

        /**
         * If all the fields are empty, then inform the user of the error (the logic will also check the Approved Where Clause if the user is an admin; otherwise, it will not).
         * If the user put in invalid Number of Players options, inform the user of the error.
         * Otherwise, construct the entire query and run it in the database.
         */
        if (!isAdmin && developerWhereClause.isEmpty() && publisherWhereClause.isEmpty() && numberOfPlayersWhereClause.isEmpty())
        {
            showSnackbar(mFilterDialogOKButton, "Valid options must be chosen!");
        }
        else if (isAdmin && developerWhereClause.isEmpty() && publisherWhereClause.isEmpty() && numberOfPlayersWhereClause.isEmpty() && approvedWhereClause.isEmpty())
        {
            showSnackbar(mFilterDialogOKButton, "Valid options must be chosen!");
        }
        else if (numberOfPlayersWhereClause.equals("INVALID NUM PLAYERS OPTIONS"))
        {
            showSnackbar(mFilterDialogOKButton, "The number of Min Players should not be greater than the number of Max Players!");
        }
        else
        {
            // Make the query to get data
            selectQuery += developerWhereClause;
            selectQuery += publisherWhereClause;
            selectQuery += numberOfPlayersWhereClause;

            /**
             * If the user is an admin, also add the Approved Where Clause.
             * Otherwise, do nothing.
             */
            if (isAdmin)
            {
                selectQuery += approvedWhereClause;
            }

            // Remove the "AND" in between the "WHERE" and the first filter where clause
            selectQuery = selectQuery.replace("WHERE  AND ", "WHERE ");

            // Run the query for the filter
            Log.d(LOG_TAG, "Filtering to get all games with query: " + selectQuery);
            // Convert the List of Where Clauses to an array since the getItemsFromDB requires a String[] for the where args [52]
            String[] whereClauseAsArray = new String[whereArgsForFilter.size()];
            whereArgsForFilter.toArray(whereClauseAsArray);
            gamesForFiltering = dbHelper.getItemsFromDB(selectQuery, whereClauseAsArray);
            mGames = gamesForFiltering;

            // Reset the RecyclerView with the resulting list of Games
            updateRecyclerView(mGames);

            successfulFilter = true;
        }

        return successfulFilter;
    }

    /**
     * Retrieve the chosen filter option and add it to the query for filtering.
     *
     * @param filteringSpinner The spinner where the filter option was picked on
     * @param attributeToFilterOn The actual attribute to filter on (e.g. Developer, Publisher, Approved, etc.
     * @param io_listOfWhereArgsForFilter The list of where args for the query filter
     * @return The query clause for filtering on the chosen option
     */
    private String filterByOneAttribute(Spinner filteringSpinner, String attributeToFilterOn, List<String> io_listOfWhereArgsForFilter)
    {
        // Retrieve the selected filter option [12]
        String filterOptionChosen = filteringSpinner.getSelectedItem().toString();

        /**
         * If the filter option is the default blank one, then return the empty string.
         * Otherwise, add the chosen filter to the where args for the filter query and return a query clause to filter on the chosen option.
         */
        if (filterOptionChosen.equals(DEFAULT_SPINNER_OPTION))
        {
            return "";
        }
        else
        {
            // Add the filter option chosen to the list of where args
            io_listOfWhereArgsForFilter.add(filterOptionChosen);
            return " AND " + attributeToFilterOn + " = ?";
        }
    }

    /**
     * Handle filtering by Number of Players.
     * This is a custom filter method since the number filters work different than the other String filters.
     *
     * @param io_listOfWhereArgsForFilter The list of where args for the query filter
     * @return The query clause for filtering on the Min/Max Players
     */
    private String filterByNumberOfPlayers(List<String> io_listOfWhereArgsForFilter)
    {
        // Retrieve the selected filter options [12]
        String filterOptionChosenMinPlayers = mSpinnerMinPlayers.getSelectedItem().toString();
        String filterOptionChosenMaxPlayers = mSpinnerMaxPlayers.getSelectedItem().toString();

        /**
         * If both Min and Max Players was left empty, return the empty string.
         * Otherwise, processes the chosen options.
         */
        if (filterOptionChosenMinPlayers.equals(DEFAULT_SPINNER_OPTION) || filterOptionChosenMaxPlayers.equals(DEFAULT_SPINNER_OPTION))
        {
            return "";
        }
        else
        {
            // Remove the "+" from the selected number of players if it is present to make input validations and querying the database easier
            filterOptionChosenMinPlayers = filterOptionChosenMinPlayers.replace("+", "");
            filterOptionChosenMaxPlayers = filterOptionChosenMaxPlayers.replace("+", "");

            // Initialize Min and Max Players as integers
            int intMinPlayers;
            int intMaxPlayers;

            // Retrieve the selected Min and Max Players as integers
            intMinPlayers = Integer.parseInt(filterOptionChosenMinPlayers);
            intMaxPlayers = Integer.parseInt(filterOptionChosenMaxPlayers);

            /**
             * Verify that the number of Min Players is not greater than the number of Max Players.
             * If this is the case, then inform the user of the error.
             */
            if (intMinPlayers > intMaxPlayers)
            {
                return "INVALID NUM PLAYERS OPTIONS";
            }

            Log.d(LOG_TAG, "Valid options chosen for the Number of Players filters");

            // Initialize the where clause for the Number of Players filter
            String numberOfPlayersFilterWhereClause = "";

            /**
             * If the user selected the 5+ option, set the query to pull Games where min_players is greater than or equal to the 5.
             * Otherwise, set the query to pull Games where the min_players is equal to the selected number.
             */
            if (intMinPlayers == 5)
            {
                numberOfPlayersFilterWhereClause += " AND min_players >= ?";
            }
            else
            {
                numberOfPlayersFilterWhereClause += " AND min_players = ?";
            }

            /**
             * If the user selected the 5+ option, set the query to pull Games where max_players is greater than or equal to the 5.
             * Otherwise, set the query to pull Games where the max_players is equal to the selected number.
             */
            if (intMaxPlayers == 5)
            {
                numberOfPlayersFilterWhereClause += " AND max_players >= ?";
            }
            else
            {
                numberOfPlayersFilterWhereClause += " AND max_players = ?";
            }

            // Add the filter options chosen to the list of where args
            io_listOfWhereArgsForFilter.add(filterOptionChosenMinPlayers);
            io_listOfWhereArgsForFilter.add(filterOptionChosenMaxPlayers);

            return numberOfPlayersFilterWhereClause;
        }
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