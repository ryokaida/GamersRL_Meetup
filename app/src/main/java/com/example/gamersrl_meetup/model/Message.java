package com.example.gamersrl_meetup.model;

/**
 * Message class
 *
 * Represents a single chat message.
 */
public class Message
{
    private String senderUid;
    private String senderName;
    private String text;
    private long timestamp;

    /**
     * Empty constructor required for Firebase.
     */
    public Message()
    {
    }

    /**
     * Construct a new Message.
     *
     * @param senderUid The Firebase UID of the user sending the message.
     * @param senderName The name of the user sending the message.
     * @param text The message text.
     * @param timestamp The time the message was sent.
     */
    public Message(
            String senderUid,
            String senderName,
            String text,
            long timestamp)
    {
        this.senderUid = senderUid;
        this.senderName = senderName;
        this.text = text;
        this.timestamp = timestamp;
    }

    public String getSenderUid()
    {
        return senderUid;
    }

    public void setSenderUid(String senderUid)
    {
        this.senderUid = senderUid;
    }

    public String getSenderName()
    {
        return senderName;
    }

    public void setSenderName(String senderName)
    {
        this.senderName = senderName;
    }

    public String getText()
    {
        return text;
    }

    public void setText(String text)
    {
        this.text = text;
    }

    public long getTimestamp()
    {
        return timestamp;
    }

    public void setTimestamp(long timestamp)
    {
        this.timestamp = timestamp;
    }
}