package com.example.gamersrl_meetup.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

/**
 * Event class
 *
 * Models an Event so that the application can work with
 * Events outside the database.
 */
public class Event implements Parcelable
{
    // Set up attributes for Event
    private int id;
    private String title;
    private String description;
    private String address;
    private String city;
    private String state;
    private String ownerUid;

    /**
     * Construct a new Event with an ID.
     *
     * @param id The Event ID.
     * @param title The Event title.
     * @param description The Event description.
     * @param address The Event street address.
     * @param city The city where the Event takes place.
     * @param state The state where the Event takes place.
     * @param ownerUid The Firebase UID of the user who created the Event.
     */
    public Event(
            int id,
            String title,
            String description,
            String address,
            String city,
            String state,
            String ownerUid)
    {
        this.id = id;
        this.title = title;
        this.description = description;
        this.address = address;
        this.city = city;
        this.state = state;
        this.ownerUid = ownerUid;
    }

    /**
     * Construct a new Event without an ID.
     * The database will generate the ID.
     */
    public Event(
            String title,
            String description,
            String address,
            String city,
            String state,
            String ownerUid)
    {
        this.title = title;
        this.description = description;
        this.address = address;
        this.city = city;
        this.state = state;
        this.ownerUid = ownerUid;
    }

    /**
     * Reconstruct an Event from a Parcel.
     *
     * @param in The Parcel containing the Event data.
     */
    protected Event(Parcel in)
    {
        id = in.readInt();
        title = in.readString();
        description = in.readString();
        address = in.readString();
        city = in.readString();
        state = in.readString();
        ownerUid = in.readString();
    }

    /**
     * Creator used to reconstruct Events from Parcels.
     */
    public static final Creator<Event> CREATOR =
            new Creator<Event>()
            {
                @Override
                public Event createFromParcel(Parcel in)
                {
                    return new Event(in);
                }

                @Override
                public Event[] newArray(int size)
                {
                    return new Event[size];
                }
            };

    @Override
    public int describeContents()
    {
        return 0;
    }

    /**
     * Write the Event data into a Parcel.
     */
    @Override
    public void writeToParcel(
            @NonNull Parcel dest,
            int flags)
    {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(address);
        dest.writeString(city);
        dest.writeString(state);
        dest.writeString(ownerUid);
    }

    // Getters
    public int getId()
    {
        return id;
    }

    public String getTitle()
    {
        return title;
    }

    public String getDescription()
    {
        return description;
    }

    public String getAddress()
    {
        return address;
    }

    public String getCity()
    {
        return city;
    }

    public String getState()
    {
        return state;
    }

    public String getOwnerUid()
    {
        return ownerUid;
    }

    /**
     * Return a short display location for the Events List.
     *
     * @return The Event city and state.
     */
    public String getLocation()
    {
        return city + ", " + state;
    }

    // Setters
    public void setId(int id)
    {
        this.id = id;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public void setCity(String city)
    {
        this.city = city;
    }

    public void setState(String state)
    {
        this.state = state;
    }

    public void setOwnerUid(String ownerUid)
    {
        this.ownerUid = ownerUid;
    }
}