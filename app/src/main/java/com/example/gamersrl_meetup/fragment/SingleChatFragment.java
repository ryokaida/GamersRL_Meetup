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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import com.example.gamersrl_meetup.model.Message;
import com.example.gamersrl_meetup.adapter.Adapter_Message;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * SingleChatFragment class
 *
 * Displays a one-on-one chat between the current user
 * and the selected user.
 */
public class SingleChatFragment extends Fragment
{
    // Initialize the UI elements
    private RecyclerView mRecyclerView;
    private EditText mMessageEditText;
    private Button mSendButton;
    private ImageButton mBackButton;
    private TextView mChatWithName;

    // Initialize the selected user's information
    private String receiverUid;
    private String receiverName;

    // Initialize Firebase
    private FirebaseFirestore db;
    private FirebaseUser currentUser;

    // Initialize the Message list and adapter
    private List<Message> messages;
    private Adapter_Message messageAdapter;

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
        mRecyclerView = view.findViewById(R.id.chat_recycler_view);
        mMessageEditText = view.findViewById(R.id.message_edittext);
        mSendButton = view.findViewById(R.id.send_button);
        mBackButton = view.findViewById(R.id.chat_back_button);
        mChatWithName = view.findViewById(R.id.chat_with_name);
        db = FirebaseFirestore.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

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

        // Display the selected user's name
        mChatWithName.setText("Chatting with " + receiverName);

        /**
         * Navigate back to the previous Fragment when
         * the Back button is clicked.
         */
        mBackButton.setOnClickListener(v ->
        {
            requireActivity()
                    .getSupportFragmentManager()
                    .popBackStack();
        });

        // Make a LinearLayoutManager to display messages vertically
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(requireContext());

        mRecyclerView.setLayoutManager(layoutManager);

        // Initialize the Message list and adapter
        messages = new ArrayList<>();

        messageAdapter = new Adapter_Message(messages);
        mRecyclerView.setAdapter(messageAdapter);

        // Listen for Messages in Firestore
        loadMessages();

        /**
         * Handle the Send button.
         * Create a new Message and save it to Firestore.
         */
        mSendButton.setOnClickListener(v ->
        {
            String text =
                    mMessageEditText.getText().toString().trim();

            if (text.isEmpty())
            {
                return;
            }

            Message message = new Message(
                    currentUser.getUid(),
                    currentUser.getEmail(),
                    text,
                    System.currentTimeMillis()
            );

            db.collection("chats")
                    .document(getChatId())
                    .collection("messages")
                    .add(message)
                    .addOnSuccessListener(documentReference ->
                    {
                        mMessageEditText.setText("");
                    });
        });

        return view;
    }

    /**
     * Retrieve the Messages for the current chat from Firestore.
     * Listen for changes so that new Messages appear automatically.
     */
    private void loadMessages()
    {
        db.collection("chats")
                .document(getChatId())
                .collection("messages")
                .orderBy("timestamp", Query.Direction.ASCENDING)
                .addSnapshotListener((querySnapshot, error) ->
                {
                    if (error != null || querySnapshot == null)
                    {
                        return;
                    }

                    // Clear the current list before rebuilding it
                    messages.clear();

                    /**
                     * Convert each Firestore document into a Message
                     * and add it to the Message list.
                     */
                    querySnapshot.getDocuments().forEach(document ->
                    {
                        Message message =
                                document.toObject(Message.class);

                        if (message != null)
                        {
                            messages.add(message);
                        }
                    });

                    // Inform the adapter that the Message data has changed
                    messageAdapter.notifyDataSetChanged();

                    /**
                     * Scroll to the newest Message after the list updates.
                     */
                    if (!messages.isEmpty())
                    {
                        mRecyclerView.scrollToPosition(
                                messages.size() - 1
                        );
                    }
                });
    }

    /**
     * Build a unique chat ID for two users.
     *
     * @return The chat ID.
     */
    private String getChatId()
    {
        List<String> ids = Arrays.asList(
                currentUser.getUid(),
                receiverUid
        );

        Collections.sort(ids);

        return ids.get(0) + "_" + ids.get(1);
    }
}