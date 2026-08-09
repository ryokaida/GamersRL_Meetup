package com.example.gamersrl_meetup.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gamersrl_meetup.R;
import com.example.gamersrl_meetup.model.Message;

import java.util.List;

/**
 * Adapter_Message class
 *
 * Displays chat messages in the RecyclerView.
 */
public class Adapter_Message
        extends RecyclerView.Adapter<Adapter_Message.ViewHolder>
{
    // Initialize the list of Messages
    private List<Message> messages;

    /**
     * Construct a new Message Adapter.
     *
     * @param messages The Messages to display.
     */
    public Adapter_Message(List<Message> messages)
    {
        this.messages = messages;
    }

    /**
     * ViewHolder class
     *
     * Holds the UI elements for a single Message.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView mSender;
        TextView mMessageText;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);

            // Instantiate the UI elements
            mSender = itemView.findViewById(R.id.message_sender);
            mMessageText = itemView.findViewById(R.id.message_text);
        }
    }

    /**
     * Create a ViewHolder for a single Message.
     *
     * @param parent The ViewGroup the Message view will be added to.
     * @param viewType The type of View to create.
     *
     * @return The ViewHolder for the Message.
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType)
    {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(
                        R.layout.item_message,
                        parent,
                        false
                );

        return new ViewHolder(view);
    }

    /**
     * Bind a Message to the current RecyclerView item.
     *
     * @param holder The ViewHolder to populate.
     * @param position The position of the Message in the list.
     */
    @Override
    public void onBindViewHolder(
            @NonNull ViewHolder holder,
            int position)
    {
        Message message = messages.get(position);

        holder.mSender.setText(message.getSenderName());
        holder.mMessageText.setText(message.getText());
    }

    /**
     * Retrieve the number of Messages.
     *
     * @return The number of Messages.
     */
    @Override
    public int getItemCount()
    {
        return messages.size();
    }
}