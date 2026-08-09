package com.example.gamersrl_meetup.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gamersrl_meetup.R;
import com.example.gamersrl_meetup.fragment.SingleChatFragment;
import com.example.gamersrl_meetup.model.ChatUser;

import java.util.List;

/**
 * Adapter_ChatUser class
 *
 * Displays a list of users that the current user can chat with.
 */
public class Adapter_ChatUser extends RecyclerView.Adapter<Adapter_ChatUser.ViewHolder>
{
    // List of users
    private List<ChatUser> users;

    /**
     * Construct a new adapter.
     *
     * @param users The users to display.
     */
    public Adapter_ChatUser(List<ChatUser> users)
    {
        this.users = users;
    }

    /**
     * ViewHolder class
     *
     * Holds references to the UI elements for a single user
     * displayed in the RecyclerView.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView mUserName;
        TextView mUserEmail;

        /**
         * Construct a ViewHolder for a single user.
         *
         * @param itemView The View containing the UI elements
         *                 for the current RecyclerView item.
         */
        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);

            // Instantiate the UI elements for the user
            mUserName = itemView.findViewById(R.id.chat_user_name);
            mUserEmail = itemView.findViewById(R.id.chat_user_email);
        }
    }

    /**
     * Create a ViewHolder for a single user in the list.
     *
     * @param parent The ViewGroup that the new View will be added to.
     * @param viewType The type of View to create.
     *
     * @return The ViewHolder for a single user.
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType)
    {
        Context context = parent.getContext();

        // Inflate the layout for a single user
        View view = LayoutInflater.from(context)
                .inflate(
                        R.layout.item_chat_user,
                        parent,
                        false
                );

        return new ViewHolder(view);
    }

    /**
     * Bind a user's information to the current RecyclerView item.
     *
     * @param holder The ViewHolder to populate.
     * @param position The position of the current user in the list.
     */
    @Override
    public void onBindViewHolder(
            @NonNull ViewHolder holder,
            int position)
    {
        // Retrieve the user for the current RecyclerView position
        ChatUser user = users.get(position);

        // Populate the list item with the user's information
        holder.mUserName.setText(user.getName());
        holder.mUserEmail.setText(user.getEmail());

        /**
         * Navigate to the Chats Fragment when the selected user is clicked.
         * Pass the selected user's UID and name to the Chats Fragment.
         */
        holder.itemView.setOnClickListener(v ->
        {
            // Create a Bundle containing the selected user's information
            Bundle bundle = new Bundle();
            bundle.putString(
                    "receiverUid",
                    user.getUid()
            );
            bundle.putString(
                    "receiverName",
                    user.getName()
            );

            // Create the Single Chats Fragment and provide it with the selected user
            SingleChatFragment singleChatFragment = new SingleChatFragment();
            singleChatFragment.setArguments(bundle);

            FragmentActivity activity =
                    (FragmentActivity) v.getContext();

            activity.getSupportFragmentManager()
                    .beginTransaction()
                    .replace(
                            R.id.fragment_container,
                            singleChatFragment
                    )
                    .addToBackStack(null)
                    .commit();
        });
    }

    /**
     * Retrieve the number of users in the list.
     *
     * @return The number of users.
     */
    @Override
    public int getItemCount()
    {
        return users.size();
    }
}