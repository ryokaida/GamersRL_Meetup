package com.example.gamersrl_meetup.fragment;

import android.view.View;

import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gamersrl_meetup.R;
import com.example.gamersrl_meetup.adapter.Adapter_ChatUser;
import com.example.gamersrl_meetup.model.ChatUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class ChatsFragment extends Fragment
{
    private RecyclerView mRecyclerView;
    private Adapter_ChatUser adapter;

    private List<ChatUser> users;

    private FirebaseFirestore db;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(
                R.layout.fragment_chats,
                container,
                false
        );

        ImageButton backButton = view.findViewById(R.id.back_button);

        backButton.setOnClickListener(v ->
        {
            requireActivity()
                    .getSupportFragmentManager()
                    .popBackStack();
        });

        mRecyclerView = view.findViewById(R.id.chat_users_recycler_view);

        mRecyclerView.setLayoutManager(
                new LinearLayoutManager(requireContext())
        );

        users = new ArrayList<>();

        adapter = new Adapter_ChatUser(users);

        mRecyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();

        loadUsers();

        return view;
    }

    private void loadUsers()
    {
        FirebaseUser currentUser =
                FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser == null)
        {
            return;
        }

        String currentUid = currentUser.getUid();

        db.collection("users")
                .get()
                .addOnSuccessListener(querySnapshot ->
                {
                    users.clear();

                    querySnapshot.getDocuments().forEach(document ->
                    {
                        // Do not display the currently logged-in user
                        if (!document.getId().equals(currentUid))
                        {
                            String name = document.getString("name");
                            String email = document.getString("email");

                            ChatUser user = new ChatUser(
                                    document.getId(),
                                    name,
                                    email
                            );

                            users.add(user);
                        }
                    });

                    adapter.notifyDataSetChanged();
                });
    }
}
