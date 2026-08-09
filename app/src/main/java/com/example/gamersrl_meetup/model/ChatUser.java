package com.example.gamersrl_meetup.model;

/**
 * ChatUser class
 *
 * Represents a user that can participate in a chat.
 */
public class ChatUser
{
    private String uid;
    private String name;
    private String email;

    /**
     * Empty constructor required for Firebase.
     */
    public ChatUser()
    {
    }

    /**
     * Construct a ChatUser.
     *
     * @param uid The user's Firebase UID
     * @param name The user's name
     * @param email The user's email
     */
    public ChatUser(String uid, String name, String email)
    {
        this.uid = uid;
        this.name = name;
        this.email = email;
    }

    public String getUid()
    {
        return uid;
    }

    public void setUid(String uid)
    {
        this.uid = uid;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }
}