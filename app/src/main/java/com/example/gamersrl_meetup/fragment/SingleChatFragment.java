package com.example.gamersrl_meetup.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gamersrl_meetup.R;

/**
 * SingleChatFragment class
 *
 * Displays a one-on-one chat between the current user
 * and the selected user.
 */
public class SingleChatFragment extends Fragment
{
    // Initialize the UI elements
    private ImageButton mBackButton;
    private TextView mChatTitle;
    private RecyclerView mRecyclerView;
    private EditText mMessageEditText;
    private Button mSendButton;

    // Initialize the selected user's information
    private String receiverUid;
    private String receiverName;

    /**
     * Inflate the layout for the Single Chat Fragment.
     *
     * @param inflater The LayoutInflater object that can be used to inflate
     *                 any views in the fragment.
     * @param container If non-null, this is the parent view that the fragment's
     *                  UI should be attached to.
     * @param savedInstanceState If non-null, this fragment is being
     *                           re-constructed from a previous saved state.
     *
     * @return The Single Chat Fragment view.
     */
    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState)
    {
        // Inflate the layout for this Fragment
        View view = inflater.inflate(
                R.layout.fragment_single_chat,
                container,
                false
        );

        // Instantiate the UI elements
        mBackButton = view.findViewById(R.id.back_button);
        mChatTitle = view.findViewById(R.id.chat_title);
        mRecyclerView = view.findViewById(R.id.chat_recycler_view);
        mMessageEditText = view.findViewById(R.id.message_edittext);
        mSendButton = view.findViewById(R.id.send_button);

        /**
         * Retrieve the selected user's information from
         * the Fragment arguments.
         */
        Bundle arguments = getArguments();

        if (arguments != null)
        {
            receiverUid = arguments.getString("receiverUid");
            receiverName = arguments.getString("receiverName");
        }

        // Set the page title to indicate who the user is chatting with
        mChatTitle.setText("Chat with " + receiverName);

        // Make a LinearLayoutManager to display messages vertically
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(requireContext());

        mRecyclerView.setLayoutManager(layoutManager);

        /**
         * Navigate back to the Chats Fragment when the
         * Back button is clicked.
         */
        mBackButton.setOnClickListener(v ->
        {
            requireActivity()
                    .getSupportFragmentManager()
                    .popBackStack();
        });

        /**
         * Handle the Send button.
         *
         * Firestore message sending will be added after
         * the basic Single Chat page is working.
         */
        mSendButton.setOnClickListener(v ->
        {
            String message =
                    mMessageEditText.getText().toString().trim();

            if (!message.isEmpty())
            {
                // Firestore message sending will go here.
            }
        });

        return view;
    }
}