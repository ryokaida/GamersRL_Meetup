package com.example.gamersrl_meetup.adapter;

import android.content.Context;
import android.os.Parcelable;
import android.util.Log;
import android.view.ViewGroup;


import com.example.gamersrl_meetup.R;
import com.example.gamersrl_meetup.activity.GameDetailsPageActivity;
import com.example.gamersrl_meetup.model.Game;

import java.util.List;

/**
 * Adapter_Game class
 *
 * Provides an intermediary between the data and the list item views for Games.
 * Extends the Adapter abstract class to dynamically populate the list item views with data.
 */
public class Adapter_Game extends Adapter
{
    // Set up the Log tag [26]
    private final String LOG_TAG = "GAME ADAPTER - ";

    // Initialize a List of items
    private List<Game> games;

    /**
     * Construct a new Adapter with the list of items to display.
     *
     * @param games The list of items to display
     */
    public Adapter_Game(List<Game> games) {
        super();
        this.games = games;
    }

    /**
     * Dynamically retrieve the labels for the data in the list item.
     * These abstract helper methods are used so that labels can be retrieved for Games, Users, etc.
     *
     * @param context The context that the label is being retrieved for
     * @return The developer, description, and ID labels
     */
    @Override
    public String getLabel1(Context context)
    {
        // Retrieve the Developer Label from the String resources [23]
        String label1 = context.getResources().getString(R.string.games_list_page_developer_label);
        Log.d(LOG_TAG, "Retrieved Label1: " + label1);
        return label1;
    }
    @Override
    public String getLabel2(Context context)
    {
        // Retrieve the Description Label from the String resources [23]
        String label2 = context.getResources().getString(R.string.list_item_description_label);
        Log.d(LOG_TAG, "Retrieved Label2: " + label2);
        return label2;
    }
    @Override
    public String getIdLabel(Context context)
    {
        // Retrieve the ID Label from the String resources [23]
        String idLabel = context.getResources().getString(R.string.list_item_id_label);
        Log.d(LOG_TAG, "Retrieved ID Label: " + idLabel);
        return idLabel;
    }

    /**
     * Dynamically retrieve the data to be displayed in the list item.
     * These abstract helper methods are used so that data can be retrieved for Games, Users, etc.
     *
     * @param position The position of the current item in the list
     * @return The Game's title, developer, description, ID, and picture URI
     */
    @Override
    public String getNameText(int position)
    {
        String nameText = games.get(position).getTitle();
        Log.d(LOG_TAG, "Retrieved name text: " + nameText);
        return nameText;
    }
    @Override
    public String getSubtitle1(int position)
    {
        String subtitle1 = games.get(position).getDeveloper();
        Log.d(LOG_TAG, "Retrieved subtitle 1: " + subtitle1);
        return subtitle1;
    }
    @Override
    public String getSubtitle2(int position)
    {
        String subtitle2 = games.get(position).getDescription();
        Log.d(LOG_TAG, "Retrieved subtitle 2: " + subtitle2);
        return subtitle2;
    }
    @Override
    public String getIdText(int position)
    {
        String idText = String.valueOf(games.get(position).getId());
        Log.d(LOG_TAG, "Retrieved ID text: " + idText);
        return idText;
    }
    @Override
    public int getImage(int position)
    {
        int imageID = games.get(position).getPictureURI();
        Log.d(LOG_TAG, "Retrieved image ID: " + String.valueOf(imageID));
        return imageID;
    }

    /**
     * Create a ViewHolder for a single item in the List.
     * Uses the Adapter abstract class's onCreateViewHolder method.
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType The view type of the new View - The ID of the layout to inflate.
     * @return the ViewHolder for a single Product in the list
     */
    @Override
    public Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        Log.d(LOG_TAG, "Creating View Holder");
        return super.onCreateViewHolder(parent, viewType);
    }

    /**
     * Bind the list item's data to its view in the list.
     * Uses the Adapter abstract class's onBindViewHolder method (the parent method is set up to use the abstract data retrieval methods to populate the views).
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        Log.d(LOG_TAG, "Binding data to View Holder");
        super.onBindViewHolder(holder, position);
    }

    /**
     * Retrieve the number of items in the list.
     *
     * @return The number of items in the list
     */
    @Override
    public int getItemCount()
    {
        int numItemsInList = games.size();
        Log.d(LOG_TAG, "Retrieved number of items in list: " + String.valueOf(numItemsInList));
        return numItemsInList;
    }

    /**
     * Retrieve the correct activity to navigate to.
     *
     * @return The Activity to navigate to
     */
    @Override
    public Class<?> getActivityToGoTo()
    {
        return GameDetailsPageActivity.class;
    }

    /**
     * Retrieve the selected item, so its details can be displayed on its details page.
     * This will feed into the intent that is used to navigate to the details page when the View button is clicked.
     * The item is returned as a Parcelable, so it can be added to the intent.
     *
     * @param in_Position The position of the selected item in the list
     * @return The item to display details for as a Parcelable
     */
    @Override
    public Parcelable getItemToDisplayDetailsFor(int in_Position)
    {
        return games.get(in_Position);
    }
}