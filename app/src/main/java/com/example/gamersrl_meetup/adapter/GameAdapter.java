package com.example.gamersrl_meetup.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.gamersrl_meetup.R;
import com.example.gamersrl_meetup.model.Game;

import java.util.List;

public class GameAdapter extends RecyclerView.Adapter<GameAdapter.ViewHolder>
{
    // Set up the Log tag
    private final String LOG_TAG = "GAME ADAPTER - ";

    // Initialize a List of Games
    private List<Game> games;

    /**
     * Construct a new Adapter with the list of Games to display.
     *
     * @param games The list of Games to display
     */
    public GameAdapter(List<Game> games)
    {
        this.games = games;
    }

    /**
     * Create a ViewHolder for a single Game in the List.
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType The view type of the new View - The ID of the layout to inflate.
     * @return the ViewHolder for a single Product in the list
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        // Inflate the view for a single Product in the list
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        // Use the inflated view for the Product to make its ViewHolder
        return new ViewHolder(view);
    }

    /**
     * Bind the Game's data to its view in the list item.
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

        // Get the selected Game
        Game game = games.get(position2);

        // Populate the views with the data
        holder.titleTextView.setText(game.getTitle());
        holder.developerLabel.setText(R.string.games_list_page_developer_label);
        holder.developerTextView.setText(game.getDeveloper());
        holder.descriptionLabel.setText(R.string.list_item_description_label);
        holder.descriptionTextView.setText(game.getDescription());
        holder.idLabel.setText(R.string.list_item_id_label);
        holder.idTextView.setText(String.valueOf(game.getId()));

        // Populate the image with the correct Product icon [22]
        holder.imageView.setImageResource(game.getPictureURI());

        //holder.viewButton.set
    }

    /**
     * Retrieve the number of Games in the list.
     *
     * @return The number of Games in the list
     */
    @Override
    public int getItemCount()
    {
        return games.size();
    }

    /**
     * ViewHolder class
     *
     * Holds the Views for the Game's data and image.
     */
    public class ViewHolder extends RecyclerView.ViewHolder
    {
        // Initialize the TextViews for the Game attributes and the list item dividers
        public TextView titleTextView, developerTextView, descriptionTextView, idTextView, developerLabel, descriptionLabel, idLabel;
        public View listItemDivider;

        // Initialize the View button for each list item
        public Button viewButton;

        // Initialize the ImageView for the ImageView for the Game image [22]
        public ImageView imageView;

        /**
         * Construct a new ViewHolder with the Views needed for the Game's data, icon, and View button.
         *
         * @param itemView the View of the Game in the list
         */
        public ViewHolder(View itemView)
        {
            // Set the itemView
            super(itemView);

            // Instantiate the UI elements for the list item
            titleTextView = itemView.findViewById(R.id.nameTextView);
            developerLabel = itemView.findViewById(R.id.label_1);
            developerTextView = itemView.findViewById(R.id.subtitle_1);
            descriptionLabel = itemView.findViewById(R.id.label_2);
            descriptionTextView = itemView.findViewById(R.id.subtitle_2);
            idLabel = itemView.findViewById(R.id.label_id);
            idTextView = itemView.findViewById(R.id.idTextView);
            imageView = itemView.findViewById(R.id.list_item_icon);
            listItemDivider = itemView.findViewById(R.id.list_item_divider_bottom);
            viewButton = itemView.findViewById(R.id.viewButton);
        }
    }
}
