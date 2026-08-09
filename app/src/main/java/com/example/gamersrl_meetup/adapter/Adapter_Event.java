package com.example.gamersrl_meetup.adapter;

import android.content.Context;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.example.gamersrl_meetup.R;
import com.example.gamersrl_meetup.activity.EventDetailsActivity;
import com.example.gamersrl_meetup.model.Event;

import java.util.List;

/**
 * Adapter_Event class
 *
 * Provides an intermediary between Event data
 * and the shared list item views.
 */
public class Adapter_Event extends Adapter
{
    // Set up the Log tag
    private final String LOG_TAG = "EVENT ADAPTER - ";

    // Initialize the list of Events
    private List<Event> events;

    /**
     * Construct a new Event Adapter.
     *
     * @param events The Events to display.
     */
    public Adapter_Event(List<Event> events)
    {
        super();
        this.events = events;
    }

    /**
     * Retrieve the label for the Event location.
     */
    @Override
    public String getLabel1(Context context)
    {
        return "Location:";
    }

    /**
     * Retrieve the label for the Event description.
     */
    @Override
    public String getLabel2(Context context)
    {
        return "Description:";
    }

    /**
     * Retrieve the ID label.
     */
    @Override
    public String getIdLabel(Context context)
    {
        return "ID:";
    }

    @Override
    public String getNameText(int position)
    {
        return events
                .get(position)
                .getTitle();
    }

    @Override
    public String getSubtitle1(int position)
    {
        return events
                .get(position)
                .getLocation();
    }

    @Override
    public String getSubtitle2(int position)
    {
        return events
                .get(position)
                .getDescription();
    }

    @Override
    public String getIdText(int position)
    {
        return String.valueOf(
                events.get(position).getId()
        );
    }

    /**
     * Use a generic calendar icon for Events.
     */
    @Override
    public int getImage(int position)
    {
        return R.drawable.ic_calendar;
    }

    @Override
    public Adapter.ViewHolder onCreateViewHolder(
            ViewGroup parent,
            int viewType)
    {
        Log.d(
                LOG_TAG,
                "Creating View Holder"
        );

        return super.onCreateViewHolder(
                parent,
                viewType
        );
    }

    @Override
    public void onBindViewHolder(
            ViewHolder holder,
            int position)
    {
        Log.d(
                LOG_TAG,
                "Binding Event"
        );

        super.onBindViewHolder(
                holder,
                position
        );
    }

    @Override
    public int getItemCount()
    {
        return events.size();
    }

    /**
     * Retrieve the Activity to open when View is clicked.
     */
    @Override
    public Class<?> getActivityToGoTo()
    {
        return EventDetailsActivity.class;
    }

    /**
     * Retrieve the selected Event.
     */
    @Override
    public Parcelable getItemToDisplayDetailsFor(
            int position)
    {
        return events.get(position);
    }

    /**
     * Events do not require the Game-specific
     * Approved or Min/Max Player information.
     */
    @Override
    public void createExtraListInformation(
            View view,
            int position)
    {
        return;
    }
}