package com.example.gamersrl_meetup.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.gamersrl_meetup.R;

/**
 * Adapter abstract class
 *
 * Provides a template for intermediaries between the data and the list item views.
 *
 * The format of each list item is as follows:
 *      nameText            ViewButton
 *              idLabel: idText
 *              label1: subtitle1
 *      Icon    label2:
 *              subtitle2
 *
 * The actual text in each element is dynamically populated using the abstract methods (except for the View button text).
 * Each list item has a divider at the bottom of it to separate it from the other items.
 */
public abstract class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder>
{
    // Set up the Log tag [26]
    private final String LOG_TAG = "ADAPTER - ";

    /**
     * Constructor for the abstract Adapter class
     */
    public Adapter() { }

    /**
     * Create a ViewHolder for a single item in the List.
     *
     * NOTE: You should only have to call return super.onCreateViewHolder(parent, viewType); in the specific Game/User/etc. Adapters
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType The view type of the new View - The ID of the layout to inflate.
     * @return the ViewHolder for a single Product in the list
     */
    @Override
    public Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {

        // Inflate the view for a single item in the list
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        // Use the inflated view for the item to make its ViewHolder
        return new Adapter.ViewHolder(view);
    }

    /**
     * Dynamically retrieve the label for the data in the list item.
     * These abstract helper methods are used so that labels can be retrieved for Games, Users, etc.
     *
     * NOTE: e.g. These methods can be used in the specific Game/User/etc. Adapters to get the specific labels like "Developer", "Location", etc.
     *
     * @param context The context that the label is being retrieved for
     * @return The label text that is supposed to populate the Label1, Label2, and ID Label TextViews
     */
    public abstract String getLabel1(Context context);
    public abstract String getLabel2(Context context);
    public abstract String getIdLabel(Context context);

    /**
     * Dynamically retrieve the data to be displayed in the list item.
     * These abstract helper methods are used so that data can be retrieved for Games, Users, etc.
     *
     * NOTE: e.g. These methods can be used in the specific Game/User/etc. Adapters to get the title, name, developer, etc.
     *
     * @param position The position of the current item in the list
     * @return The text that is supposed to populate the Name, Subtitle1, Subtitle2, ID text, and image views
     */
    public abstract String getNameText(int position);
    public abstract String getSubtitle1(int position);
    public abstract String getSubtitle2(int position);
    public abstract String getIdText(int position);
    public abstract int getImage(int position);

    /**
     * Bind the list item's data to its view in the list.
     *
     * NOTE: You should only have to call super.onBindViewHolder(holder, position); in the specific Game/User/etc. Adapters
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        // Assign position to a new variable so it can be used to populate the RecyclerView and for the OnClickListener [18] [19] [20] [21]
        int position2 = position;

        /**
         * Populate the views with the data.
         *
         * Uses the abstract helper methods (that are implemented by the specific Game/User/etc. Adapters during runtime)
         * to get the data so that data can be dynamically retrieved for Games, Users, etc.
         */
        Log.d(LOG_TAG, "Setting Name TextView");
        holder.nameTextView.setText(getNameText(position2));
        Log.d(LOG_TAG, "Setting Label 1 TextView");
        holder.label1TextView.setText(getLabel1(holder.label1TextView.getContext()));
        Log.d(LOG_TAG, "Setting Subtitle 1 TextView");
        holder.subitle1TextView.setText(getSubtitle1(position2));
        Log.d(LOG_TAG, "Setting Label 2 TextView");
        holder.label2TextView.setText(getLabel2(holder.label2TextView.getContext()));
        Log.d(LOG_TAG, "Setting Subtitle 2 TextView");
        holder.subtitle2TextView.setText(getSubtitle2(position2));
        Log.d(LOG_TAG, "Setting ID Label TextView");
        holder.idLabel.setText(getIdLabel(holder.idLabel.getContext()));
        Log.d(LOG_TAG, "Setting ID Text TextView");
        holder.idTextView.setText(getIdText(position2));
        // Populate the image with the correct Product icon [22]
        Log.d(LOG_TAG, "Setting ImageView");
        holder.imageView.setImageResource(getImage(position2));
        // Create any extra list information if needed
        createExtraListInformation(holder.extraInfoRegion, position2);

        // Set the onClick listener on the View buttons
        holder.viewButton.setOnClickListener(new View.OnClickListener()
        {
            /**
             * OnClick Listener to navigate to the correct details page when the View buttons are clicked.
             *
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v)
            {
                // Get the activity to go to and the item to display the details for
                Class<?> activityToGoTo = getActivityToGoTo();
                Log.d(LOG_TAG, "Retrieved activity to go to: " + getActivityToGoTo().getName());
                // Get the item to display the details for
                Parcelable itemToDisplayDetailsFor = getItemToDisplayDetailsFor(position2);
                Log.d(LOG_TAG, "Retrieved item to display details for: " + itemToDisplayDetailsFor.toString());

                // Construct the intent
                Log.d(LOG_TAG, "Constructing intent and navigating to the new page");
                Intent intent = new Intent(v.getContext(), activityToGoTo);
                intent.putExtra("itemDetails", itemToDisplayDetailsFor);

                // Navigate to the correct details page from the RecyclerView [25]
                v.getContext().startActivity(intent);
            }
        });
    }

    /**
     * Retrieve the number of items in the list.
     *
     * @return The number of items in the list
     */
    @Override
    public abstract int getItemCount();

    /**
     * Retrieve the correct activity to navigate to.
     * This is abstract since each specific Adapter (Game/User/etc.) will navigate to different activities when the View button is clicked.
     *
     * @return The Activity to navigate to
     */
    public abstract Class<?> getActivityToGoTo();

    /**
     * Retrieve the selected item, so its details can be displayed on its details page.
     * This will feed into the intent that is used to navigate to the details page when the View button is clicked.
     * The item is returned as a Parcelable, so it can be added to the intent.
     * This is abstract since each specific adapter (Game/User/etc.) will handle different items (Games/Users/etc.).
     *
     * @param in_Position The position of the selected item in the list
     * @return The item to display details for as a Parcelable
     */
    public abstract Parcelable getItemToDisplayDetailsFor(int in_Position);

    /**
     * Create any extra information for the list item after the default data pieces if needed.
     * e.g. adding min/max number of players for Games for basic users, adding the Approved attribute for Games for admins, etc.
     * If you do not need this method, please just use "return;" in the method when implementing it in the Specific Adapter class.
     *
     * @param v The View to make the UI elements in
     * @param position The position of the selected item in the list
     */
    public abstract void createExtraListInformation(View v, int position);

    /**
     * ViewHolder class
     *
     * Holds the Views for the list item's data and icon.
     *
     * NOTE: This does NOT need to be recalled in the specific Game/User/etc. Adapters.
     */
    public class ViewHolder extends RecyclerView.ViewHolder
    {
        // Initialize the TextViews for the attributes and the list item dividers
        public TextView nameTextView, subitle1TextView, subtitle2TextView, idTextView, label1TextView, label2TextView, idLabel;
        public View listItemDivider;

        // Initialize the View button for each list item
        public Button viewButton;

        // Initialize the ImageView for the list item's image [22]
        public ImageView imageView;

        // Initialize the region for any Extra Info
        public LinearLayout extraInfoRegion;

        /**
         * Construct a new ViewHolder with the views needed for the item's data, icon, and View button.
         *
         * @param itemView The view of the item in the list
         */
        public ViewHolder(View itemView)
        {
            // Set the itemView
            super(itemView);
            Log.d(LOG_TAG, "Setting up the itemView");

            // Instantiate the UI elements for the list item
            Log.d(LOG_TAG, "Instantiating UI elements for the list item");
            nameTextView = itemView.findViewById(R.id.nameTextView);
            label1TextView = itemView.findViewById(R.id.label_1);
            subitle1TextView = itemView.findViewById(R.id.subtitle_1);
            label2TextView = itemView.findViewById(R.id.label_2);
            subtitle2TextView = itemView.findViewById(R.id.subtitle_2);
            idLabel = itemView.findViewById(R.id.label_id);
            idTextView = itemView.findViewById(R.id.idTextView);
            imageView = itemView.findViewById(R.id.list_item_icon);
            listItemDivider = itemView.findViewById(R.id.list_item_divider_bottom);
            viewButton = itemView.findViewById(R.id.viewButton);

            // Assign the Extra Info LinearLayout as a variable so the UI elements can be added to the page [27] [28] [29] [210] [31] [32] [33].
            extraInfoRegion = itemView.findViewById(R.id.extra_listitem_information);
        }
    }
}