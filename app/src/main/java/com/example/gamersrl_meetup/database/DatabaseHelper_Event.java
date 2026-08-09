package com.example.gamersrl_meetup.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.gamersrl_meetup.model.Event;

import java.util.List;

/**
 * DatabaseHelper_Event class
 *
 * Provides an intermediary between the application
 * and the SQLite database for Events.
 */
public class DatabaseHelper_Event extends DatabaseHelper<Event>
{
    // Set up the Log tag
    private final String LOG_TAG = "EVENT DATABASE HELPER - ";

    // Set the table name
    private static final String TABLE_NAME = "event";

    // Set the table columns
    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_ADDRESS = "address";
    private static final String KEY_CITY = "city";
    private static final String KEY_STATE = "state";
    private static final String KEY_OWNER_UID = "owner_uid";

    // Set the Event Member table name
    private static final String MEMBER_TABLE_NAME = "event_member";

    // Set the Event Member table columns
    private static final String KEY_MEMBER_ID = "id";
    private static final String KEY_MEMBER_EVENT_ID = "event_id";
    private static final String KEY_MEMBER_USER_UID = "user_uid";

    /**
     * Construct the Event database helper.
     *
     * Also ensure that the Event table exists because
     * the shared application database may already have
     * been created by another DatabaseHelper.
     *
     * @param context The Context used to access the database.
     */
    public DatabaseHelper_Event(Context context)
    {
        super(context);

        // Ensure the Event table exists in the shared database
        SQLiteDatabase db = getWritableDatabase();

        db.execSQL(createTable());

        db.execSQL(
                "CREATE TABLE IF NOT EXISTS " +
                        MEMBER_TABLE_NAME + "(" +
                        KEY_MEMBER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        KEY_MEMBER_EVENT_ID + " INTEGER, " +
                        KEY_MEMBER_USER_UID + " TEXT" +
                        ")"
        );

        db.close();
    }

    /**
     * Create the Event table if it does not already exist.
     *
     * @return The SQL query used to create the table.
     */
    @Override
    public String createTable()
    {
        String query =
                "CREATE TABLE IF NOT EXISTS " +
                        TABLE_NAME + "(" +
                        KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        KEY_TITLE + " TEXT, " +
                        KEY_DESCRIPTION + " TEXT, " +
                        KEY_ADDRESS + " TEXT, " +
                        KEY_CITY + " TEXT, " +
                        KEY_STATE + " TEXT, " +
                        KEY_OWNER_UID + " TEXT" +
                        ")";

        Log.d(
                LOG_TAG,
                "Created query to create Event table: " + query
        );

        return query;
    }

    /**
     * Create the Event table.
     *
     * @param db The database.
     */
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        super.onCreate(db);
    }

    /**
     * Upgrade the database.
     *
     * @param db The database.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */
    @Override
    public void onUpgrade(
            SQLiteDatabase db,
            int oldVersion,
            int newVersion)
    {
        super.onUpgrade(
                db,
                oldVersion,
                newVersion
        );
    }

    /**
     * Determine whether the Event table is empty.
     *
     * @param tableName The table to check.
     *
     * @return Whether the table is empty.
     */
    @Override
    public boolean isTableEmpty(String tableName)
    {
        return super.isTableEmpty(tableName);
    }

    /**
     * Retrieve Events from the database.
     *
     * @param query The SQL query to execute.
     * @param selectionArgs The arguments for the SQL query.
     *
     * @return The Events returned by the query.
     */
    @Override
    public List<Event> getItemsFromDB(
            String query,
            String[] selectionArgs)
    {
        return super.getItemsFromDB(
                query,
                selectionArgs
        );
    }

    /**
     * Construct an Event from the current SQLite row.
     *
     * @param cursor The Cursor positioned on the Event row.
     *
     * @return The Event created from the database row.
     */
    @Override
    public Event constructItemFromDBRow(Cursor cursor)
    {
        Log.d(
                LOG_TAG,
                "Constructing Event from database row"
        );

        return new Event(
                cursor.getInt(0),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4),
                cursor.getString(5),
                cursor.getString(6)
        );
    }

    /**
     * Retrieve every Event from the database.
     *
     * @return All Events.
     */
    public List<Event> getAllEvents()
    {
        String query =
                "SELECT * FROM " +
                        TABLE_NAME;

        Log.d(
                LOG_TAG,
                "Retrieving all Events"
        );

        return getItemsFromDB(
                query,
                null
        );
    }

    /**
     * Retrieve Events for a selected city.
     *
     * @param city The city to filter on.
     *
     * @return Events occurring in the selected city.
     */
    public List<Event> getEventsByCity(String city)
    {
        String query =
                "SELECT * FROM " +
                        TABLE_NAME +
                        " WHERE " +
                        KEY_CITY +
                        " = ?";

        Log.d(
                LOG_TAG,
                "Retrieving Events for city: " + city
        );

        return getItemsFromDB(
                query,
                new String[]{city}
        );
    }

    /**
     * Add an Event to the database.
     *
     * @param event The Event to add.
     */
    public void addToDatabase(Event event)
    {
        // Get the database so it can be written to
        SQLiteDatabase db =
                getWritableDatabase();

        // Initialize the Event values
        ContentValues values =
                new ContentValues();

        values.put(
                KEY_TITLE,
                event.getTitle()
        );

        values.put(
                KEY_DESCRIPTION,
                event.getDescription()
        );

        values.put(
                KEY_ADDRESS,
                event.getAddress()
        );

        values.put(
                KEY_CITY,
                event.getCity()
        );

        values.put(
                KEY_STATE,
                event.getState()
        );

        values.put(
                KEY_OWNER_UID,
                event.getOwnerUid()
        );

        // Insert the Event into the database
        db.insert(
                TABLE_NAME,
                null,
                values
        );

        Log.d(
                LOG_TAG,
                "Added Event: " +
                        event.getTitle()
        );

        db.close();
    }

    /**
     * Update an existing Event.
     *
     * @param id The Event ID.
     * @param title The Event title.
     * @param description The Event description.
     * @param address The Event address.
     * @param city The Event city.
     * @param state The Event state.
     */
    public void updateEvent(
            String id,
            String title,
            String description,
            String address,
            String city,
            String state)
    {
        // Get the database so it can be written to
        SQLiteDatabase db =
                getWritableDatabase();

        // Initialize the updated Event values
        ContentValues values =
                new ContentValues();

        values.put(
                KEY_TITLE,
                title
        );

        values.put(
                KEY_DESCRIPTION,
                description
        );

        values.put(
                KEY_ADDRESS,
                address
        );

        values.put(
                KEY_CITY,
                city
        );

        values.put(
                KEY_STATE,
                state
        );

        // Update the Event using its ID
        db.update(
                TABLE_NAME,
                values,
                KEY_ID + " = ?",
                new String[]{id}
        );

        Log.d(
                LOG_TAG,
                "Updated Event with ID: " + id
        );

        db.close();
    }

    /**
     * Delete an Event from the database.
     *
     * @param id The ID of the Event to delete.
     */
    public void deleteEvent(String id)
    {
        Log.d(
                LOG_TAG,
                "Deleting Event with ID: " + id
        );

        deleteItemFromDB(
                TABLE_NAME,
                id
        );
    }

    /**
     * Retrieve the Event table name.
     *
     * @return The Event table name.
     */
    public String getTableName()
    {
        return TABLE_NAME;
    }

    /**
     * Populate the database with demonstration Events.
     */
    @Override
    public void populateDatabase()
    {
        // Get the database so it can be written to
        SQLiteDatabase db =
                getWritableDatabase();

        // -------------------------
        // Board Game Night
        // -------------------------

        ContentValues values =
                new ContentValues();

        values.put(
                KEY_TITLE,
                "Board Game Night"
        );

        values.put(
                KEY_DESCRIPTION,
                "Meet local gamers for a casual board game night."
        );

        values.put(
                KEY_ADDRESS,
                "43316 Hay Road"
        );

        values.put(
                KEY_CITY,
                "Ashburn"
        );

        values.put(
                KEY_STATE,
                "VA"
        );

        values.put(
                KEY_OWNER_UID,
                "demo"
        );

        db.insert(
                TABLE_NAME,
                null,
                values
        );

        // -------------------------
        // Co-op Gaming Meetup
        // -------------------------

        values =
                new ContentValues();

        values.put(
                KEY_TITLE,
                "Co-op Gaming Meetup"
        );

        values.put(
                KEY_DESCRIPTION,
                "Meet other players for an evening of cooperative games."
        );

        values.put(
                KEY_ADDRESS,
                "12000 Government Center Parkway"
        );

        values.put(
                KEY_CITY,
                "Fairfax"
        );

        values.put(
                KEY_STATE,
                "VA"
        );

        values.put(
                KEY_OWNER_UID,
                "demo"
        );

        db.insert(
                TABLE_NAME,
                null,
                values
        );

        // -------------------------
        // Weekend Gamers Meetup
        // -------------------------

        values =
                new ContentValues();

        values.put(
                KEY_TITLE,
                "Weekend Gamers Meetup"
        );

        values.put(
                KEY_DESCRIPTION,
                "A relaxed weekend meetup for local gamers."
        );

        values.put(
                KEY_ADDRESS,
                "11925 Bowman Towne Drive"
        );

        values.put(
                KEY_CITY,
                "Reston"
        );

        values.put(
                KEY_STATE,
                "VA"
        );

        values.put(
                KEY_OWNER_UID,
                "demo"
        );

        db.insert(
                TABLE_NAME,
                null,
                values
        );

        Log.d(
                LOG_TAG,
                "Finished populating Event database"
        );

        db.close();
    }

    /**
     * Add the current user as a member of an Event.
     *
     * @param eventId The ID of the Event.
     * @param userUid The Firebase UID of the user joining.
     */
    public void joinEvent(
            int eventId,
            String userUid)
    {
        SQLiteDatabase db =
                getWritableDatabase();

        ContentValues values =
                new ContentValues();

        values.put(
                KEY_MEMBER_EVENT_ID,
                eventId
        );

        values.put(
                KEY_MEMBER_USER_UID,
                userUid
        );

        db.insert(
                MEMBER_TABLE_NAME,
                null,
                values
        );

        db.close();
    }

    /**
     * Remove the current user from an Event.
     *
     * @param eventId The ID of the Event.
     * @param userUid The Firebase UID of the user leaving.
     */
    public void leaveEvent(
            int eventId,
            String userUid)
    {
        SQLiteDatabase db =
                getWritableDatabase();

        db.delete(
                MEMBER_TABLE_NAME,
                KEY_MEMBER_EVENT_ID +
                        " = ? AND " +
                        KEY_MEMBER_USER_UID +
                        " = ?",
                new String[]{
                        String.valueOf(eventId),
                        userUid
                }
        );

        db.close();
    }

    /**
     * Determine whether a user has already joined an Event.
     *
     * @param eventId The ID of the Event.
     * @param userUid The Firebase UID of the user.
     *
     * @return Whether the user has joined the Event.
     */
    public boolean isUserJoined(
            int eventId,
            String userUid)
    {
        SQLiteDatabase db =
                getReadableDatabase();

        Cursor cursor =
                db.rawQuery(
                        "SELECT * FROM " +
                                MEMBER_TABLE_NAME +
                                " WHERE " +
                                KEY_MEMBER_EVENT_ID +
                                " = ? AND " +
                                KEY_MEMBER_USER_UID +
                                " = ?",
                        new String[]{
                                String.valueOf(eventId),
                                userUid
                        }
                );

        boolean joined =
                cursor.getCount() > 0;

        cursor.close();
        db.close();

        return joined;
    }
}